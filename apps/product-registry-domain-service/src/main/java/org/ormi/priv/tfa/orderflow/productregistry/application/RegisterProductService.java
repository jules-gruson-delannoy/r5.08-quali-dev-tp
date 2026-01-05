package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d’application pour l’enregistrement de nouveaux produits.
 * <p>
 * Cette classe valide la commande {@link RegisterProductCommand},
 * crée le produit, enregistre l’événement de domaine et publie
 * un événement via l’outbox pour les systèmes externes.
 * </p>
 */
@ApplicationScoped
public class RegisterProductService {

    private final ProductRepository repository;
    private final EventLogRepository eventLog;
    private final OutboxRepository outbox;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param repository repository pour gérer les produits
     * @param eventLog repository pour l’enregistrement des événements
     * @param outbox repository pour publier les événements externes
     */
    @Inject
    public RegisterProductService(
        ProductRepository repository,
        EventLogRepository eventLog,
        OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    /**
     * Traite la commande d’enregistrement d’un produit.
     * <p>
     * Vérifie que le SKU n’existe pas déjà, crée un produit,
     * enregistre un événement de domaine dans le log et le publie
     * dans l’outbox.
     * </p>
     *
     * @param cmd commande contenant les informations du produit
     * @return l’identifiant du produit créé
     * @throws IllegalArgumentException si le SKU existe déjà
     */
    @Transactional
    public ProductId handle(RegisterProductCommand cmd) throws IllegalArgumentException {
        if (repository.existsBySkuId(cmd.skuId())) {
            throw new IllegalArgumentException(String.format("SKU already exists: %s", cmd.skuId()));
        }
        Product product = Product.create(
                cmd.name(),
                cmd.description(),
                cmd.skuId());
        // Save domain object
        repository.save(product);
        EventEnvelope<ProductRegistered> evt = EventEnvelope.with(
                new ProductRegistered(product.getId(), product.getSkuId(), cmd.name(), cmd.description()),
                product.getVersion());
        // Appends event to the log
        final EventLogEntity persistedEvent = eventLog.append(evt);
        // Publish outbox
        outbox.publish(OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build());
        return product.getId();
    }
}
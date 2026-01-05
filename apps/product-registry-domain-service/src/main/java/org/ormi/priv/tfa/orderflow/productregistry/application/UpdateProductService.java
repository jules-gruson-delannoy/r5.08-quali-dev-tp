package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductDescriptionCommand;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.UpdateProductNameCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d’application pour mettre à jour les informations d’un produit.
 * <p>
 * Ce service gère la mise à jour du nom et de la description d’un produit,
 * enregistre les événements de domaine et publie les événements via l’outbox.
 * </p>
 */
@ApplicationScoped
public class UpdateProductService {

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
    public UpdateProductService(
        ProductRepository repository,
        EventLogRepository eventLog,
        OutboxRepository outbox
    ) {
        this.repository = repository;
        this.eventLog = eventLog;
        this.outbox = outbox;
    }

    /**
     * Met à jour le nom d’un produit existant.
     *
     * @param cmd commande contenant l’identifiant du produit et le nouveau nom
     * @throws IllegalArgumentException si le produit n’est pas trouvé
     */
    @Transactional
    public void handle(UpdateProductNameCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductNameUpdated> event = product.updateName(cmd.newName());
        repository.save(product);
        final EventLogEntity persistedEvent = eventLog.append(event);
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }

    /**
     * Met à jour la description d’un produit existant.
     *
     * @param cmd commande contenant l’identifiant du produit et la nouvelle description
     * @throws IllegalArgumentException si le produit n’est pas trouvé
     */
    @Transactional
    public void handle(UpdateProductDescriptionCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductDescriptionUpdated> event = product.updateDescription(cmd.newDescription());
        repository.save(product);
        final EventLogEntity persistedEvent = eventLog.append(event);
        outbox.publish(
            OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build()
        );
    }
}

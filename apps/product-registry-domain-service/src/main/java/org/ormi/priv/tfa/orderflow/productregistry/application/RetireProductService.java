package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRetired;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RetireProductCommand;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Service d’application pour retirer un produit existant.
 * <p>
 * Cette classe récupère le produit par son identifiant, 
 * exécute l’opération de retrait, enregistre l’événement
 * de domaine et publie un événement dans l’outbox.
 * </p>
 */
@ApplicationScoped
public class RetireProductService {

    @Inject
    ProductRepository repository;

    @Inject
    EventLogRepository eventLog;

    @Inject
    OutboxRepository outbox;

    /**
     * Retire un produit existant.
     *
     * @param cmd commande contenant l’identifiant du produit à retirer
     * @throws IllegalArgumentException si le produit n’est pas trouvé
     */
    @Transactional
    public void retire(RetireProductCommand cmd) throws IllegalArgumentException {
        Product product = repository.findById(cmd.productId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        EventEnvelope<ProductRetired> evt = product.retire();
        repository.save(product);
        // Append event to the log
        final EventLogEntity persistedEvent = eventLog.append(evt);
        // Publish outbox
        outbox.publish(OutboxEntity.Builder()
                .sourceEvent(persistedEvent)
                .build());
    }
}

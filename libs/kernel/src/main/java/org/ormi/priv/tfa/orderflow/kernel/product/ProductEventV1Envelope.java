package org.ormi.priv.tfa.orderflow.kernel.product;

import java.time.Instant;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRetired;

/**
 * Enveloppe typée pour les événements V1 du domaine {@link Product}.
 * <p>
 * Cette classe abstraite hérite de {@link EventEnvelope} et sert à encapsuler
 * un événement {@link ProductEventV1} avec :
 * <ul>
 *   <li>un identifiant de séquence {@link #sequence()}</li>
 *   <li>un horodatage {@link #timestamp()}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chaque type d'événement métier V1 possède sa propre sous-classe d'enveloppe, permettant
 * de manipuler les événements de manière type-safe lors de la projection ou du traitement
 * dans l'outbox :
 * <ul>
 *   <li>{@link ProductRegisteredEnvelope} : pour {@link ProductRegistered}</li>
 *   <li>{@link ProductRetiredEnvelope} : pour {@link ProductRetired}</li>
 *   <li>{@link ProductNameUpdatedEnvelope} : pour {@link ProductNameUpdated}</li>
 *   <li>{@link ProductDescriptionUpdatedEnvelope} : pour {@link ProductDescriptionUpdated}</li>
 * </ul>
 * </p>
 *
 * <h2>Exemple d’utilisation :</h2>
 * <pre>{@code
 * ProductRegistered registeredEvent = new ProductRegistered(productId, skuId, "Laptop", "Gaming Laptop");
 * ProductEventV1Envelope<ProductRegistered> envelope =
 *     new ProductEventV1Envelope.ProductRegisteredEnvelope(registeredEvent, 1L, Instant.now());
 * 
 * Long sequence = envelope.sequence();
 * Instant timestamp = envelope.timestamp();
 * ProductRegistered payload = envelope.event();
 * UUID aggregateId = envelope.aggregateId();
 * }</pre>
 *
 * @param <E> Type de l'événement V1 encapsulé
 */
public abstract class ProductEventV1Envelope<E extends ProductEventV1> extends EventEnvelope<E> {

    public ProductEventV1Envelope(E event, Long sequence, Instant timestamp) {
        super(event, sequence, timestamp);
    }

    /**
     * Enveloppe pour l'événement {@link ProductRegistered}.
     */
    public static class ProductRegisteredEnvelope extends ProductEventV1Envelope<ProductRegistered> {
        public ProductRegisteredEnvelope(ProductRegistered event, Long sequence, Instant timestamp) {
            super(event, sequence, timestamp);
        }
    }

    /**
     * Enveloppe pour l'événement {@link ProductRetired}.
     */
    public static class ProductRetiredEnvelope extends ProductEventV1Envelope<ProductRetired> {
        public ProductRetiredEnvelope(ProductRetired event, Long sequence, Instant timestamp) {
            super(event, sequence, timestamp);
        }
    }

    /**
     * Enveloppe pour l'événement {@link ProductNameUpdated}.
     */
    public static class ProductNameUpdatedEnvelope extends ProductEventV1Envelope<ProductNameUpdated> {
        public ProductNameUpdatedEnvelope(ProductNameUpdated event, Long sequence, Instant timestamp) {
            super(event, sequence, timestamp);
        }
    }

    /**
     * Enveloppe pour l'événement {@link ProductDescriptionUpdated}.
     */
    public static class ProductDescriptionUpdatedEnvelope extends ProductEventV1Envelope<ProductDescriptionUpdated> {
        public ProductDescriptionUpdatedEnvelope(ProductDescriptionUpdated event, Long sequence, Instant timestamp) {
            super(event, sequence, timestamp);
        }
    }
}

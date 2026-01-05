package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent;
import org.ormi.priv.tfa.orderflow.kernel.Product;

/**
 * Définit les événements métier de la version 1 ({@code V1}) pour l'agrégat {@link Product}.
 * <p>
 * Cette interface est scellée pour représenter de manière exhaustive tous les événements possibles
 * liés à un produit :
 * <ul>
 *   <li>{@link ProductRegistered} : création d'un produit</li>
 *   <li>{@link ProductRetired} : retrait d'un produit</li>
 *   <li>{@link ProductNameUpdated} : mise à jour du nom du produit</li>
 *   <li>{@link ProductDescriptionUpdated} : mise à jour de la description du produit</li>
 * </ul>
 * </p>
 *
 * <p>
 * Chaque événement implémente {@link DomainEvent} et contient :
 * <ul>
 *   <li>Un identifiant d'agrégat {@link #aggregateId()} basé sur {@link ProductId}</li>
 *   <li>Un type d'agrégat {@link #aggregateType()} (toujours "Product")</li>
 *   <li>Une version d'événement {@link #version()} (fixée à 1 pour cette version V1)</li>
 *   <li>Une charge utile {@link #payload()} spécifique à l'événement</li>
 * </ul>
 * </p>
 *
 * <h2>Exemple d’utilisation :</h2>
 * <pre>{@code
 * ProductId productId = ProductId.newId();
 * ProductEventV1 event = new ProductEventV1.ProductRegistered(productId, new SkuId("SKU-123"), "Laptop", "Gaming Laptop");
 * UUID aggregateId = event.aggregateId();
 * int version = event.version(); // toujours 1
 * ProductEventV1.ProductRegisteredPayload payload = event.payload();
 * }</pre>
 */
public sealed interface ProductEventV1 extends DomainEvent {

    /** Version des événements V1 */
    public static final int EVENT_VERSION = 1;

    /** Type d'agrégat associé */
    default String aggregateType() {
        return Product.class.getSimpleName();
    }

    /** Identifiant du produit */
    ProductId productId();

    /** UUID de l'agrégat (dérivé de productId) */
    default UUID aggregateId() {
        return productId().value();
    }

    /** Version de l'événement */
    @Override
    default int version() {
        return EVENT_VERSION;
    }

    /**
     * Interface scellée représentant la charge utile (payload) d'un événement produit.
     * <p>
     * Les payloads V1 peuvent être :
     * <ul>
     *   <li>{@link Empty} : payload vide pour les événements sans données supplémentaires (ex: ProductRetired)</li>
     *   <li>Payload spécifique à chaque événement (enregistrements immuables)</li>
     * </ul>
     * </p>
     */
    public sealed interface ProductEventV1Payload extends DomainEventPayload {
        /** Payload vide (utilisé par exemple pour ProductRetired) */
        public static final class Empty implements ProductEventV1Payload {}
    }

    /**
     * Événement de création d'un produit.
     */
    public final class ProductRegistered implements ProductEventV1 {
        private final ProductId productId;
        private final ProductRegisteredPayload payload;

        public ProductRegistered(ProductId productId, SkuId skuId, String name, String description) {
            this.productId = productId;
            this.payload = new ProductRegisteredPayload(skuId.value(), name, description);
        }

        @Override
        public ProductId productId() { return productId; }

        @Override
        public ProductRegisteredPayload payload() { return payload; }

        /** Payload pour ProductRegistered */
        public static record ProductRegisteredPayload(
                String skuId,
                String name,
                String description) implements ProductEventV1Payload {}
    }

    /**
     * Événement de retrait d'un produit.
     */
    public final class ProductRetired implements ProductEventV1 {
        private final ProductId productId;

        public ProductRetired(ProductId productId) { this.productId = productId; }

        @Override
        public ProductId productId() { return productId; }

        @Override
        public ProductEventV1Payload payload() { return new ProductEventV1Payload.Empty(); }
    }

    /**
     * Événement de mise à jour du nom d'un produit.
     */
    public final class ProductNameUpdated implements ProductEventV1 {
        private final ProductId productId;
        private final ProductNameUpdatedPayload payload;

        public ProductNameUpdated(ProductId productId, String oldName, String newName) {
            this.productId = productId;
            this.payload = new ProductNameUpdatedPayload(oldName, newName);
        }

        @Override
        public ProductId productId() { return productId; }

        @Override
        public ProductNameUpdatedPayload payload() { return payload; }

        /** Payload pour ProductNameUpdated */
        public static record ProductNameUpdatedPayload(
                String oldName,
                String newName) implements ProductEventV1Payload {}
    }

    /**
     * Événement de mise à jour de la description d'un produit.
     */
    public final class ProductDescriptionUpdated implements ProductEventV1 {
        private final ProductId productId;
        private final ProductDescriptionUpdatedPayload payload;

        public ProductDescriptionUpdated(ProductId productId, String oldDescription, String newDescription) {
            this.productId = productId;
            this.payload = new ProductDescriptionUpdatedPayload(oldDescription, newDescription);
        }

        @Override
        public ProductId productId() { return productId; }

        @Override
        public ProductDescriptionUpdatedPayload payload() { return payload; }

        /** Payload pour ProductDescriptionUpdated */
        public static record ProductDescriptionUpdatedPayload(
                String oldDescription,
                String newDescription) implements ProductEventV1Payload {}
    }
}

package org.ormi.priv.tfa.orderflow.kernel.product.views;

import java.time.Instant;
import java.util.List;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductEventV1Payload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

/**
 * Représente la vue complète d’un produit pour la lecture dans le système.
 * <p>
 * Cette classe encapsule toutes les informations d’un produit, y compris :
 * </p>
 * <ul>
 *     <li>Identifiant unique ({@link ProductId})</li>
 *     <li>Version de l’agrégat pour le contrôle d’optimistic locking</li>
 *     <li>Identifiant SKU ({@link SkuId})</li>
 *     <li>Nom et description</li>
 *     <li>Statut du cycle de vie ({@link ProductLifecycle})</li>
 *     <li>Références vers les catalogues associés ({@link ProductViewCatalogRef})</li>
 *     <li>Historique des événements liés au produit ({@link ProductViewEvent})</li>
 *     <li>Dates de création et de dernière mise à jour</li>
 * </ul>
 *
 * <h2>Responsabilités :</h2>
 * <ul>
 *     <li>Servir de projection pour les lectures côté lecture (CQRS)</li>
 *     <li>Fournir un modèle immuable et validé via Jakarta Validation</li>
 *     <li>Fournir un builder pour faciliter la construction sûre des instances</li>
 * </ul>
 *
 * <h2>Exemple d’utilisation :</h2>
 * <pre>{@code
 * ProductView view = ProductView.Builder()
 *      .id(productId)
 *      .version(1L)
 *      .skuId(new SkuId("ABC-12345"))
 *      .name("Produit Exemple")
 *      .description("Description détaillée")
 *      .status(ProductLifecycle.ACTIVE)
 *      .catalogs(List.of(new ProductView.ProductViewCatalogRef(catalogId, "Catalogue A")))
 *      .events(List.of(...))
 *      .createdAt(Instant.now())
 *      .updatedAt(Instant.now())
 *      .build();
 * }</pre>
 *
 * <p>
 * La validation automatique garantit que tous les champs obligatoires (@NotNull, @NotBlank) sont renseignés.
 * En cas de violation, une {@link ConstraintViolationException} est levée.
 * </p>
 */
@Getter
public class ProductView {
    
    @NotNull
    private final ProductId id;
    @NotNull
    private final Long version;
    @NotNull
    private final SkuId skuId;
    @NotBlank
    private final String name;
    @NotNull
    private final String description;
    @NotNull
    private final ProductLifecycle status;
    @NotNull
    private final List<ProductViewCatalogRef> catalogs;
    @NotNull
    private final List<ProductViewEvent> events;
    @NotNull
    private final Instant createdAt;
    @NotNull
    private final Instant updatedAt;

    private ProductView(
        ProductId id,
        Long version,
        SkuId skuId,
        String name,
        String description,
        ProductLifecycle status,
        List<ProductViewCatalogRef> catalogs,
        List<ProductViewEvent> events,
        Instant createdAt,
        Instant updatedAt
    ) {
        this.id = id;
        this.version = version;
        this.skuId = skuId;
        this.name = name;
        this.description = description;
        this.status = status;
        this.catalogs = catalogs;
        this.events = events;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /** Crée un nouveau builder pour construire une instance de {@link ProductView}. */
    public static ProductViewBuilder Builder() {
        return new ProductViewBuilder();
    }

    /**
     * Builder pour {@link ProductView}.
     * <p>
     * Permet de construire de manière fluide une instance validée de {@link ProductView}.
     * </p>
     */
    public static final class ProductViewBuilder {
        private ProductId id;
        private Long version;
        private SkuId skuId;
        private String name;
        private String description;
        private ProductLifecycle status;
        private List<ProductViewCatalogRef> catalogs;
        private List<ProductViewEvent> events;
        private Instant createdAt;
        private Instant updatedAt;

        public ProductViewBuilder id(ProductId id) { this.id = id; return this; }
        public ProductViewBuilder version(Long version) { this.version = version; return this; }
        public ProductViewBuilder skuId(SkuId skuId) { this.skuId = skuId; return this; }
        public ProductViewBuilder name(String name) { this.name = name; return this; }
        public ProductViewBuilder description(String description) { this.description = description; return this; }
        public ProductViewBuilder status(ProductLifecycle status) { this.status = status; return this; }
        public ProductViewBuilder catalogs(List<ProductViewCatalogRef> catalogs) { this.catalogs = catalogs; return this; }
        public ProductViewBuilder events(List<ProductViewEvent> events) { this.events = events; return this; }
        public ProductViewBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public ProductViewBuilder updatedAt(Instant updatedAt) { this.updatedAt = updatedAt; return this; }

        /** Copie les valeurs depuis une autre instance de ProductView. */
        public ProductViewBuilder with(ProductView view) {
            this.id = view.id;
            this.version = view.version;
            this.skuId = view.skuId;
            this.name = view.name;
            this.description = view.description;
            this.status = view.status;
            this.catalogs = view.catalogs;
            this.events = view.events;
            this.createdAt = view.createdAt;
            this.updatedAt = view.updatedAt;
            return this;
        }

        /**
         * Construit l’instance finale de {@link ProductView} après validation.
         *
         * @return une instance validée de {@link ProductView}
         * @throws ConstraintViolationException si l’un des champs obligatoires est manquant ou invalide
         */
        public ProductView build() {
            ProductView view = new ProductView(id, version, skuId, name, description, status, catalogs, events, createdAt, updatedAt);
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            final var violations = validator.validate(view);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return view;
        }
    }

    /**
     * Référence vers un catalogue associé au produit.
     */
    public static class ProductViewCatalogRef {
        @NotNull
        private final ProductId id;
        @NotNull
        private final String name;

        public ProductViewCatalogRef(ProductId id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    /**
     * Événement lié à l’historique du produit dans la vue.
     */
    @Getter
    public static class ProductViewEvent {
        @NotNull
        private final ProductEventType type;
        @NotNull
        private final Instant timestamp;
        @NotNull
        private final Long sequence;
        @NotNull
        private final ProductEventV1Payload payload;

        public ProductViewEvent(ProductEventType type, Instant timestamp, Long sequence, ProductEventV1Payload payload) {
            this.type = type;
            this.timestamp = timestamp;
            this.sequence = sequence;
            this.payload = payload;
        }
    }
}

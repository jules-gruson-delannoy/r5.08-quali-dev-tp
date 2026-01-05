package org.ormi.priv.tfa.orderflow.kernel.product.views;

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
 * Représente un résumé (vue simplifiée) d’un produit pour les lectures et listes paginées.
 * <p>
 * Cette classe encapsule les informations essentielles d’un produit, telles que :
 * </p>
 * <ul>
 *     <li>Identifiant unique du produit ({@link ProductId})</li>
 *     <li>Identifiant SKU ({@link SkuId})</li>
 *     <li>Nom du produit</li>
 *     <li>Statut du cycle de vie ({@link ProductLifecycle})</li>
 *     <li>Nombre de catalogues associés au produit</li>
 * </ul>
 *
 * <h2>Responsabilités :</h2>
 * <ul>
 *     <li>Servir dans les projections de type {@link ProductView} pour les listes simplifiées ou tableaux de bord.</li>
 *     <li>Fournir un modèle immuable validé via Jakarta Validation pour éviter les données incorrectes.</li>
 *     <li>Être facilement construite via le {@link ProductSummaryBuilder} pour garantir la validité.</li>
 * </ul>
 *
 * <h2>Exemple d’utilisation :</h2>
 * <pre>{@code
 * ProductSummary summary = ProductSummary.Builder()
 *      .id(new ProductId(UUID.randomUUID()))
 *      .skuId(new SkuId("ABC-12345"))
 *      .name("Produit Exemple")
 *      .status(ProductLifecycle.ACTIVE)
 *      .catalogs(3)
 *      .build();
 * }</pre>
 *
 * <p>
 * La validation automatique garantit que les champs obligatoires (@NotNull, @NotBlank)
 * sont correctement renseignés. En cas de violation, une {@link ConstraintViolationException} est levée.
 * </p>
 */
@Getter
public class ProductSummary {
    
    @NotNull
    private final ProductId id;
    
    @NotNull
    private final SkuId skuId;
    
    @NotBlank
    private final String name;
    
    @NotNull
    private final ProductLifecycle status;
    
    @NotNull
    private final Integer catalogs;

    private ProductSummary(
        ProductId id,
        SkuId skuId,
        String name,
        ProductLifecycle status,
        Integer catalogs
    ) {
        this.id = id;
        this.skuId = skuId;
        this.name = name;
        this.status = status;
        this.catalogs = catalogs;
    }

    /** Crée un nouveau builder pour construire une instance de {@link ProductSummary}. */
    public static ProductSummaryBuilder Builder() {
        return new ProductSummaryBuilder();
    }

    /**
     * Builder pour {@link ProductSummary}.
     * <p>
     * Garantit la validation des champs avant construction.
     * </p>
     */
    public static final class ProductSummaryBuilder {
        private ProductId id;
        private SkuId skuId;
        private String name;
        private ProductLifecycle status;
        private Integer catalogs;

        public ProductSummaryBuilder id(ProductId id) {
            this.id = id;
            return this;
        }

        public ProductSummaryBuilder skuId(SkuId skuId) {
            this.skuId = skuId;
            return this;
        }

        public ProductSummaryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductSummaryBuilder status(ProductLifecycle status) {
            this.status = status;
            return this;
        }

        public ProductSummaryBuilder catalogs(Integer catalogs) {
            this.catalogs = catalogs;
            return this;
        }

        /**
         * Construit l’instance finale de {@link ProductSummary} après validation.
         *
         * @return une instance validée de {@link ProductSummary}
         * @throws ConstraintViolationException si l’un des champs obligatoires est manquant ou invalide
         */
        public ProductSummary build() {
            ProductSummary summary = new ProductSummary(id, skuId, name, status, catalogs);
            final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
            final var violations = validator.validate(summary);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
            return summary;
        }
    }
}

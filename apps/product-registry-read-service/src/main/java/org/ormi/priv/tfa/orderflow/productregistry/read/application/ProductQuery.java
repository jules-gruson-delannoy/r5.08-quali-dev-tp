package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;

/**
 * Interface scellée représentant les différentes requêtes de lecture
 * sur les produits.
 * <p>
 * Chaque implémentation correspond à un type spécifique de requête
 * pouvant être exécutée pour récupérer des informations produit.
 * </p>
 */
public sealed interface ProductQuery {

    /**
     * Requête pour récupérer un produit par son identifiant.
     *
     * @param productId identifiant du produit
     */
    public record GetProductByIdQuery(ProductId productId) implements ProductQuery {
    }

    /**
     * Requête pour lister les produits avec pagination.
     *
     * @param page numéro de la page (0-based)
     * @param size taille de la page
     */
    public record ListProductQuery(int page, int size) implements ProductQuery {
    }

    /**
     * Requête pour lister les produits dont le SKU correspond à un motif,
     * avec pagination.
     *
     * @param skuIdPattern motif du SKU (ex : "SKU-%")
     * @param page numéro de la page (0-based)
     * @param size taille de la page
     */
    public record ListProductBySkuIdPatternQuery(String skuIdPattern, int page, int size) implements ProductQuery {
    }
}

package org.ormi.priv.tfa.orderflow.productregistry.application;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

/**
 * Interface scellée représentant les commandes liées aux produits
 * dans le domaine Product Registry.
 * <p>
 * Chaque implémentation correspond à une action spécifique
 * pouvant être effectuée sur un produit.
 * </p>
 */
public sealed interface ProductCommand {

    /**
     * Commande pour enregistrer un nouveau produit.
     *
     * @param name nom du produit
     * @param description description du produit
     * @param skuId identifiant SKU unique
     */
    public record RegisterProductCommand(
            String name,
            String description,
            SkuId skuId) implements ProductCommand {
    }

    /**
     * Commande pour retirer un produit existant.
     *
     * @param productId identifiant du produit à retirer
     */
    public record RetireProductCommand(ProductId productId) implements ProductCommand {
    }

    /**
     * Commande pour mettre à jour le nom d’un produit.
     *
     * @param productId identifiant du produit
     * @param newName nouveau nom du produit
     */
    public record UpdateProductNameCommand(ProductId productId, String newName) implements ProductCommand {
    }

    /**
     * Commande pour mettre à jour la description d’un produit.
     *
     * @param productId identifiant du produit
     * @param newDescription nouvelle description du produit
     */
    public record UpdateProductDescriptionCommand(ProductId productId, String newDescription) implements ProductCommand {
    }
}
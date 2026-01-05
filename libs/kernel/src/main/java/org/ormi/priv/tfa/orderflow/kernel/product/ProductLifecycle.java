package org.ormi.priv.tfa.orderflow.kernel.product;

/**
 * Représente le cycle de vie d'un {@link Product}.
 * <p>
 * Un produit peut être soit actif, soit retiré du catalogue :
 * </p>
 * <ul>
 *     <li>{@link #ACTIVE} : le produit est disponible et ses informations peuvent être modifiées.</li>
 *     <li>{@link #RETIRED} : le produit a été retiré, il n'est plus actif et ses informations
 *     ne doivent plus être mises à jour.</li>
 * </ul>
 *
 * <p>
 * Cette information est utilisée dans les projections et la validation des commandes
 * pour garantir que les opérations sur les produits respectent leur état.
 * </p>
 *
 * <h2>Exemple d'utilisation :</h2>
 * <pre>{@code
 * if (product.getStatus() == ProductLifecycle.ACTIVE) {
 *     product.updateName("Nouveau nom");
 * } else {
 *     throw new IllegalStateException("Le produit est retiré et ne peut pas être modifié");
 * }
 * }</pre>
 */
public enum ProductLifecycle {
    ACTIVE,
    RETIRED
}
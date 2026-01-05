package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1;

/**
 * Enumération représentant les versions des événements du produit (ProductEventV1).
 * <p>
 * Cette version permet de gérer l'évolution du schéma des événements dans le temps,
 * et de s'assurer que le traitement des événements dans les projections ou les outbox
 * se fait correctement en fonction de leur version.
 * </p>
 *
 * <h2>Utilisation :</h2>
 * <ul>
 *     <li>Identifier la version d'un événement pour dispatcher ou mapper correctement ses données.</li>
 *     <li>Gérer la compatibilité ascendante lors de l'évolution du modèle d'événements.</li>
 * </ul>
 *
 * <h2>Exemple :</h2>
 * <pre>{@code
 * if (event.getEventVersion() == ProductEventVersion.V1.getValue()) {
 *     ProductEventV1 productEvent = mapper.toProductEventV1(event);
 * }
 * }</pre>
 *
 * @see ProductEventV1
 */
public enum ProductEventVersion {
    /**
     * Version 1 des événements de produit.
     */
    V1(ProductEventV1.EVENT_VERSION);

    private final int value;

    ProductEventVersion(int value) {
        this.value = value;
    }

    /**
     * Retourne la valeur numérique correspondant à la version de l'événement.
     *
     * @return la valeur de la version de l'événement
     */
    public int getValue() {
        return value;
    }
}

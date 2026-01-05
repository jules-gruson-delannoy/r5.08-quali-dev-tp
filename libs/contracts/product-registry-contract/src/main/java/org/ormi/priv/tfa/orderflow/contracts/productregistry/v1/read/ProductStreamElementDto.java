package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.time.Instant;

/**
 * DTO représentant un événement d’un produit transmis via le flux de lecture (stream).
 *
 * <p>
 * Utilisé principalement pour la diffusion en temps réel des changements d’état
 * d’un produit (création, mise à jour, retrait, etc.) vers les consommateurs
 * de la read-side.
 * </p>
 *
 * <p>
 * Chaque élément du flux contient :
 * <ul>
 *   <li>{@code type} : le type de l’événement produit (ex: "REGISTERED", "NAME_UPDATED").</li>
 *   <li>{@code productId} : l’identifiant unique du produit concerné (UUID sous forme de chaîne).</li>
 *   <li>{@code occuredAt} : la date et l’heure à laquelle l’événement s’est produit.</li>
 * </ul>
 * </p>
 *
 * @see org.ormi.priv.tfa.orderflow.productregistry.read.application.ProductEventBroadcaster
 */
public record ProductStreamElementDto(
    String type,
    String productId,
    Instant occuredAt
) {
}


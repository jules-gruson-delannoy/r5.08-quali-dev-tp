package org.ormi.priv.tfa.orderflow.kernel.product;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

/**
 * Identifiant unique pour un {@link Product}.
 * <p>
 * Cette classe est un simple wrapper autour de {@link UUID}, garantissant la
 * validité non-nulle de l'identifiant via l'annotation {@link NotNull}.
 * </p>
 *
 * <p>
 * L'identifiant sert d'aggregateId dans le contexte CQRS/Event Sourcing,
 * et est utilisé pour tracer et lier tous les événements relatifs à un produit.
 * </p>
 *
 * <h2>Exemple d'utilisation :</h2>
 * <pre>{@code
 * ProductId id = ProductId.newId();
 * Product product = Product.create("Laptop", "Gaming Laptop", skuId);
 * UUID rawId = product.id().value();
 * }</pre>
 */
public record ProductId(@NotNull UUID value) {

    /**
     * Génère un nouvel identifiant unique pour un {@link Product}.
     *
     * @return un {@link ProductId} avec une valeur UUID aléatoire
     */
    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }
}

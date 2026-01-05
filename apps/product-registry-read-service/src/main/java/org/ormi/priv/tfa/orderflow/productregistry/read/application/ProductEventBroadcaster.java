package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.concurrent.CopyOnWriteArrayList;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.subscription.MultiEmitter;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Service de diffusion des événements produits vers les consommateurs.
 * <p>
 * Permet de diffuser des {@link ProductStreamElementDto} à tous les
 * abonnés en temps réel, en utilisant des flux réactifs {@link Multi}.
 * </p>
 * <p>
 * Les abonnés sont automatiquement retirés lorsqu’ils se désabonnent,
 * ce qui évite les fuites de mémoire.
 * </p>
 */
@ApplicationScoped
public class ProductEventBroadcaster {

    private final CopyOnWriteArrayList<MultiEmitter<? super ProductStreamElementDto>> emitters = new CopyOnWriteArrayList<>();

    /**
     * Diffuse un élément de flux à tous les abonnés actifs.
     *
     * @param element élément de flux à diffuser
     */
    public void broadcast(ProductStreamElementDto element) {
        emitters.forEach(emitter -> emitter.emit(element));
    }

    /**
     * Fournit un flux réactif {@link Multi} pour recevoir les événements produits.
     * <p>
     * Les nouveaux abonnés sont ajoutés à la liste des émetteurs et
     * seront automatiquement retirés lors de la terminaison de leur souscription.
     * </p>
     *
     * @return flux réactif des éléments de produit
     */
    public Multi<ProductStreamElementDto> stream() {
        return Multi.createFrom().emitter(emitter -> {
            emitters.add(emitter);
            // TODO: log a debug, "New emitter added"

            // Retire automatiquement l’émetteur lors de la terminaison
            emitter.onTermination(() -> emitters.remove(emitter));
        });
    }

    // TODO: implement [Exercice 5]
    // public Multi<ProductStreamElementDto> streamByProductId(String productId) {
    // }

    // TODO: implement [Exercice 5]
    // public Multi<ProductStreamElementDto> streamByProductIds(List<String> productIds) {
    // }
}
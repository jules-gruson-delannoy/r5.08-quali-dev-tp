package org.ormi.priv.tfa.orderflow.productregistry.read.infra.api;

import jakarta.ws.rs.Path;

/**
 * Ressource REST pour le streaming des événements produit.
 * <p>
 * Fournira des endpoints réactifs pour diffuser les événements
 * en temps réel pour un produit spécifique ou pour une liste de produits.
 * </p>
 * <p>
 * Les méthodes sont à implémenter dans le cadre de l’exercice 5.
 * </p>
 */
@Path("/products")
public class ProductStreamResource {

    // TODO: implement [Exercice 5]
    // private final ReadProductService readProductService;
    // private final ProductIdMapper productIdMapper;

    // @Inject
    // public ProductStreamResource(
    //         ReadProductService readProductService,
    //         ProductIdMapper productIdMapper) {
    //     this.readProductService = readProductService;
    //     this.productIdMapper = productIdMapper;
    // }

    // TODO: implement [Exercice 5]
    // @GET
    // @Path("/{id}/pending/stream")
    // @RestStreamElementType(MediaType.APPLICATION_JSON)
    // public Multi<ProductStreamElementDto> streamPendingOutboxMessagesByProdutId(
    //         @PathParam("id") String id) {
    //     throw new UnsupportedOperationException("TODO: implement [Exercice 5]");
    // }
}

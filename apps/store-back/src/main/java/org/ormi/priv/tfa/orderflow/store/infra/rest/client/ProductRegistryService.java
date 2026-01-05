package org.ormi.priv.tfa.orderflow.store.infra.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.PaginatedProductListDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

/**
 * Client REST pour interagir avec le service de lecture (read-side) du Product Registry.
 *
 * <p>
 * Ce client fournit des méthodes pour rechercher et consulter des produits
 * côté lecture. Il s’interface avec le microservice Product Registry Read API.
 * </p>
 *
 * <p>
 * Chaque méthode retourne un {@link RestResponse} permettant de vérifier le code HTTP
 * et d’accéder au DTO retourné.
 * </p>
 *
 * <p>
 * TODO: Implémenter le streaming des événements produits via SmallRye Mutiny pour les notifications en temps réel.
 * </p>
 *
 * @see ProductViewDto
 * @see PaginatedProductListDto
 */
@ApplicationScoped
@Path("/products")
@RegisterRestClient(configKey = "product-registry-read-api")
public interface ProductRegistryService {

    /**
     * Recherche des produits dont le SKU correspond partiellement au motif fourni.
     *
     * @param sku motif de recherche pour le SKU (peut être vide pour récupérer tous les produits)
     * @param page numéro de page (pagination, base 0)
     * @param size taille de page
     * @return {@link RestResponse} contenant la liste paginée des produits correspondants
     */
    @GET
    RestResponse<PaginatedProductListDto> searchProducts(
            @QueryParam("sku") String sku,
            @QueryParam("page") int page,
            @QueryParam("size") int size);

    /**
     * Récupère un produit par son identifiant unique.
     *
     * @param id identifiant UUID du produit
     * @return {@link RestResponse} contenant le DTO du produit si trouvé, sinon statut 404
     */
    @GET
    @Path("/{id}")
    RestResponse<ProductViewDto> getProductById(@PathParam("id") String id);

    // TODO: implement [Exercice 5] (streamProductEventsByProductId) (Utiliser Multi de SmallRye Mutiny)
}

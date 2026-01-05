package org.ormi.priv.tfa.orderflow.store.infra.rest.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductDescriptionParamsDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.UpdateProductNameParamsDto;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/**
 * Client REST pour interagir avec le service de gestion de produits côté domaine.
 *
 * <p>
 * Ce client permet de déclencher les commandes liées au cycle de vie des produits,
 * comme l’enregistrement, la mise à jour ou la mise hors service.
 * </p>
 *
 * <p>
 * Chaque méthode correspond à un endpoint exposé par le microservice Product Registry Domain.
 * Les réponses sont enveloppées dans {@link RestResponse} pour récupérer le statut HTTP.
 * </p>
 *
 * @see RegisterProductCommandDto
 * @see UpdateProductNameParamsDto
 * @see UpdateProductDescriptionParamsDto
 */
@ApplicationScoped
@Path("/products")
@RegisterRestClient(configKey = "product-registry-api")
public interface ProductRegistryDomainService {

    /**
     * Enregistre un nouveau produit dans le système.
     *
     * @param cmd DTO contenant les informations du produit à créer
     * @return {@link RestResponse} avec le statut HTTP, attendu 201 Created en cas de succès
     */
    @POST
    RestResponse<Void> registerProduct(RegisterProductCommandDto cmd);

    /**
     * Met un produit hors service (retire) dans le système.
     *
     * @param productId identifiant du produit à retirer
     * @return {@link RestResponse} avec le statut HTTP, attendu 204 No Content en cas de succès
     */
    @DELETE
    @Path("/{id}")
    RestResponse<Void> retireProduct(@PathParam("id") String productId);

    /**
     * Met à jour le nom d’un produit existant.
     *
     * @param productId identifiant du produit à mettre à jour
     * @param params DTO contenant le nouveau nom
     * @return {@link RestResponse} avec le statut HTTP, attendu 204 No Content en cas de succès
     */
    @PATCH
    @Path("/{id}/name")
    RestResponse<Void> updateProductName(@PathParam("id") String productId, UpdateProductNameParamsDto params);

    /**
     * Met à jour la description d’un produit existant.
     *
     * @param productId identifiant du produit à mettre à jour
     * @param params DTO contenant la nouvelle description
     * @return {@link RestResponse} avec le statut HTTP, attendu 204 No Content en cas de succès
     */
    @PATCH
    @Path("/{id}/description")
    RestResponse<Void> updateProductDescription(@PathParam("id") String productId, UpdateProductDescriptionParamsDto params);
}

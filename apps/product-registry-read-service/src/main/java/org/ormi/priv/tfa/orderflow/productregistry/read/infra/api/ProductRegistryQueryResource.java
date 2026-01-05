package org.ormi.priv.tfa.orderflow.productregistry.read.infra.api;

import java.util.UUID;

import org.jboss.resteasy.reactive.RestResponse;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.PaginatedProductListDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductSummary;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ReadProductService;
import org.ormi.priv.tfa.orderflow.productregistry.read.application.ReadProductService.SearchPaginatedResult;
import org.ormi.priv.tfa.orderflow.productregistry.read.infra.web.dto.ProductSummaryDtoMapper;
import org.ormi.priv.tfa.orderflow.productregistry.read.infra.web.dto.ProductViewDtoMapper;

import jakarta.inject.Inject;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

/**
 * Ressource REST pour les requêtes de lecture sur les produits.
 * <p>
 * Fournit des endpoints pour rechercher des produits et récupérer
 * des informations détaillées sur un produit par son identifiant.
 * </p>
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductRegistryQueryResource {

    private final ReadProductService readProductService;
    private final ProductViewDtoMapper productViewDtoMapper;
    private final ProductSummaryDtoMapper productSummaryDtoMapper;
    private final ProductIdMapper productIdMapper;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param readProductService service de lecture des produits
     * @param productViewDtoMapper mapper pour convertir les vues produit en DTO
     * @param productSummaryDtoMapper mapper pour convertir les résumés produit en DTO
     * @param productIdMapper mapper pour convertir les identifiants produit
     */
    @Inject
    public ProductRegistryQueryResource(
            ReadProductService readProductService,
            ProductViewDtoMapper productViewDtoMapper,
            ProductSummaryDtoMapper productSummaryDtoMapper,
            ProductIdMapper productIdMapper) {
        this.readProductService = readProductService;
        this.productViewDtoMapper = productViewDtoMapper;
        this.productSummaryDtoMapper = productSummaryDtoMapper;
        this.productIdMapper = productIdMapper;
    }

    /**
     * Recherche des produits par motif de SKU avec pagination.
     *
     * @param sku motif du SKU (par défaut : chaîne vide)
     * @param page numéro de la page (0-based)
     * @param size taille de la page
     * @return réponse HTTP contenant la liste paginée de produits
     */
    @GET
    public RestResponse<PaginatedProductListDto> searchProducts(
            @QueryParam("sku") @DefaultValue("") String sku,
            @QueryParam("page") int page,
            @QueryParam("size") int size) {
        final SearchPaginatedResult result = readProductService.searchProducts(sku, page, size);
        final PaginatedProductListDto list = new PaginatedProductListDto(result.page().stream()
                .map(view -> ProductSummary.Builder()
                        .id(view.getId())
                        .skuId(view.getSkuId())
                        .name(view.getName())
                        .status(view.getStatus())
                        .catalogs(view.getCatalogs().size())
                        .build())
                .map(productSummaryDtoMapper::toDto)
                .toList(), page, size, result.total());
        return RestResponse.ok(list);
    }

    /**
     * Récupère un produit par son identifiant.
     *
     * @param id identifiant UUID du produit
     * @return réponse HTTP contenant le produit si trouvé, sinon NOT_FOUND
     */
    @GET
    @Path("/{id}")
    public RestResponse<ProductViewDto> getProductById(@PathParam("id") String id) {
        final var product = readProductService.findById(productIdMapper.map(UUID.fromString(id)));
        if (product.isEmpty()) {
            return RestResponse.status(RestResponse.Status.NOT_FOUND);
        }
        return RestResponse.ok(productViewDtoMapper.toDto(product.get()));
    }
}

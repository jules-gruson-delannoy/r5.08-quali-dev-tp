package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductViewRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Service de lecture des produits.
 * <p>
 * Fournit des méthodes pour récupérer les produits, effectuer des recherches
 * paginées et diffuser les événements produits via des flux réactifs {@link Multi}.
 * </p>
 */
@ApplicationScoped
public class ReadProductService {

    private final ProductViewRepository repository;
    private final ProductEventBroadcaster productEventBroadcaster;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param repository repository des vues produit
     * @param productEventBroadcaster diffuseur d’événements produit
     */
    @Inject
    public ReadProductService(
        ProductViewRepository repository,
        ProductEventBroadcaster productEventBroadcaster) {
        this.repository = repository;
        this.productEventBroadcaster = productEventBroadcaster;
    }

    /**
     * Recherche un produit par son identifiant.
     *
     * @param productId identifiant du produit
     * @return Optional contenant le produit si trouvé, sinon vide
     */
    public Optional<ProductView> findById(ProductId productId) {
        return repository.findById(productId);
    }

    /**
     * Recherche des produits par motif de SKU avec pagination.
     *
     * @param skuIdPattern motif du SKU
     * @param page numéro de la page (0-based)
     * @param size taille de la page
     * @return résultat paginé avec la liste des produits et le total
     */
    public SearchPaginatedResult searchProducts(String skuIdPattern, int page, int size) {
        return new SearchPaginatedResult(
                repository.searchPaginatedViewsOrderBySkuId(skuIdPattern, page, size),
                repository.countPaginatedViewsBySkuIdPattern(skuIdPattern));
    }

    /**
     * Flux réactif des événements d’un produit spécifique.
     *
     * @param productId identifiant du produit
     * @return flux {@link Multi} d’événements produit
     */
    public Multi<ProductStreamElementDto> streamProductEvents(ProductId productId) {
        return productEventBroadcaster.stream()
                .select().where(e -> e.productId().equals(productId.value().toString()));
    }

    /**
     * Flux réactif des événements des produits correspondant à un motif de SKU avec pagination.
     *
     * @param skuIdPattern motif du SKU
     * @param page numéro de la page (0-based)
     * @param size taille de la page
     * @return flux {@link Multi} d’événements produit
     */
    public Multi<ProductStreamElementDto> streamProductListEvents(String skuIdPattern, int page, int size) {
        final List<ProductView> products = searchProducts(skuIdPattern, page, size).page();
        final List<UUID> productIds = products.stream()
                .map(p -> p.getId().value())
                .toList();
        return productEventBroadcaster.stream()
                .select().where(e -> productIds.contains(UUID.fromString(e.productId())));
    }

    /**
     * Résultat paginé d’une recherche de produits.
     *
     * @param page liste des produits de la page
     * @param total nombre total de produits correspondant au critère
     */
    public record SearchPaginatedResult(List<ProductView> page, long total) {
    }
}

package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import java.util.List;
import java.util.Optional;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;

/**
 * Interface représentant le dépôt de persistance des vues de produits ({@link ProductView}).
 * <p>
 * Cette interface est destinée à gérer les projections des produits, utilisées pour
 * la lecture et la recherche optimisée des informations produit. Elle permet d'accéder
 * aux données dérivées du modèle de domaine principal ({@link Product}) de manière efficace.
 * </p>
 *
 * <h2>Responsabilités :</h2>
 * <ul>
 *     <li>Persister ou mettre à jour les {@link ProductView} dans le dépôt.</li>
 *     <li>Récupérer un {@link ProductView} par son identifiant unique {@link ProductId} ou par son {@link SkuId}.</li>
 *     <li>Effectuer des recherches paginées et filtrées par motif SKU.</li>
 *     <li>Fournir un comptage des résultats correspondant à un motif SKU pour la pagination.</li>
 * </ul>
 *
 * <h2>Exemple d’utilisation :</h2>
 * <pre>{@code
 * ProductViewRepository repository = ...;
 *
 * ProductView view = ...;
 * repository.save(view);
 *
 * Optional<ProductView> byId = repository.findById(view.getId());
 * Optional<ProductView> bySku = repository.findBySkuId(new SkuId("ABC-12345"));
 *
 * long total = repository.countPaginatedViewsBySkuIdPattern("ABC-%");
 * List<ProductView> page = repository.searchPaginatedViewsOrderBySkuId("ABC-%", 0, 10);
 * }</pre>
 *
 * <p>
 * Cette interface peut être implémentée à l’aide de différents mécanismes de stockage,
 * tels que JPA/Hibernate, bases NoSQL, ou stockage en mémoire pour des tests unitaires.
 * </p>
 */
public interface ProductViewRepository {

    /**
     * Sauvegarde ou met à jour une projection {@link ProductView}.
     *
     * @param productView la vue du produit à persister
     */
    void save(ProductView productView);

    /**
     * Recherche une vue de produit par son identifiant unique.
     *
     * @param id l'identifiant du produit
     * @return un {@link Optional} contenant la vue si trouvée, ou vide sinon
     */
    Optional<ProductView> findById(ProductId id);

    /**
     * Recherche une vue de produit par son identifiant SKU.
     *
     * @param skuId l'identifiant SKU du produit
     * @return un {@link Optional} contenant la vue si trouvée, ou vide sinon
     */
    Optional<ProductView> findBySkuId(SkuId skuId);

    /**
     * Compte le nombre de vues de produits correspondant à un motif SKU donné.
     * <p>
     * Utile pour la pagination et l’affichage de totaux dans les recherches filtrées.
     * </p>
     *
     * @param skuIdPattern le motif SKU (ex: "ABC-%")
     * @return le nombre de vues correspondant au motif
     */
    long countPaginatedViewsBySkuIdPattern(String skuIdPattern);

    /**
     * Recherche paginée des vues de produits correspondant à un motif SKU, triées par SKU.
     *
     * @param skuIdPattern le motif SKU (ex: "ABC-%")
     * @param page le numéro de page (0-indexé)
     * @param size le nombre d'éléments par page
     * @return la liste des vues correspondant à la page et au motif
     */
    List<ProductView> searchPaginatedViewsOrderBySkuId(String skuIdPattern, int page, int size);
}

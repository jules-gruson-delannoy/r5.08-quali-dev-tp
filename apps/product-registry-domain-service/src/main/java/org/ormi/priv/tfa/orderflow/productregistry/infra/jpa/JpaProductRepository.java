package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.Optional;
import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductRepository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du repository des produits.
 * <p>
 * Utilise Panache et les mappers pour gérer la persistance des objets
 * de domaine {@link Product} dans la base de données.
 * </p>
 */
@ApplicationScoped
public class JpaProductRepository implements PanacheRepositoryBase<ProductEntity, UUID>, ProductRepository {

    private final ProductJpaMapper mapper;
    private final ProductIdMapper productIdMapper;    
    private final SkuIdMapper skuIdMapper;

    /**
     * Constructeur avec injection des mappers nécessaires à la conversion
     * entre entités JPA et objets de domaine.
     *
     * @param mapper mappe les entités JPA vers les objets de domaine
     * @param productIdMapper mappe les identifiants de produit
     * @param skuIdMapper mappe les identifiants SKU
     */
    @Inject
    public JpaProductRepository(ProductJpaMapper mapper, ProductIdMapper productIdMapper, SkuIdMapper skuIdMapper) {
        this.mapper = mapper;
        this.productIdMapper = productIdMapper;
        this.skuIdMapper = skuIdMapper;
    }

    /**
     * Sauvegarde un produit en base.
     * <p>
     * Si le produit existe déjà, l’entité est mise à jour ; sinon,
     * une nouvelle entité est créée.
     * </p>
     *
     * @param product produit à sauvegarder
     */
    @Override
    @Transactional
    public void save(Product product) {
        findByIdOptional(productIdMapper.map(product.getId()))
                .ifPresentOrElse(e -> {
                    mapper.updateEntity(product, e);
                }, () -> {
                    ProductEntity newEntity = mapper.toEntity(product);
                    getEntityManager().merge(newEntity);
                });
    }

    /**
     * Recherche un produit par son identifiant.
     *
     * @param id identifiant du produit
     * @return Optional contenant le produit si trouvé, sinon vide
     */
    @Override
    public Optional<Product> findById(ProductId id) {
        return findByIdOptional(productIdMapper.map(id))
                .map(mapper::toDomain);
    }

    /**
     * Vérifie si un produit existe pour un SKU donné.
     *
     * @param skuId identifiant SKU
     * @return true si un produit avec ce SKU existe, false sinon
     */
    @Override
    public boolean existsBySkuId(SkuId skuId) {
        return count("skuId", skuIdMapper.map(skuId)) > 0;
    }
}

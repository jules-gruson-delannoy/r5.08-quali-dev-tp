package org.ormi.priv.tfa.orderflow.kernel.product.persistence;

import java.util.Optional;

import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuId;

/**
 * Interface représentant le dépôt de persistance des produits.
 * <p>
 * Cette interface définit les opérations de base pour accéder et manipuler
 * les entités {@link Product} dans le stockage sous-jacent (base de données,
 * mémoire, ou autre mécanisme de persistance).
 * </p>
 *
 * <h2>Responsabilités :</h2>
 * <ul>
 *     <li>Enregistrer un produit ou mettre à jour un produit existant.</li>
 *     <li>Récupérer un produit par son identifiant unique {@link ProductId}.</li>
 *     <li>Vérifier l’existence d’un produit à partir de son identifiant SKU {@link SkuId}.</li>
 * </ul>
 *
 * <h2>Exemple d’utilisation :</h2>
 * <pre>{@code
 * ProductRepository repository = ...;
 *
 * ProductId id = ProductId.newId();
 * Product product = Product.create("Laptop", "High-end laptop", new SkuId("ABC-12345"));
 * repository.save(product);
 *
 * Optional<Product> loaded = repository.findById(id);
 * boolean exists = repository.existsBySkuId(new SkuId("ABC-12345"));
 * }</pre>
 *
 * <p>
 * Cette interface peut être implémentée avec différents mécanismes de stockage,
 * tels que JPA/Hibernate, JDBC, ou un stockage en mémoire pour les tests.
 * </p>
 */
public interface ProductRepository {

    /**
     * Sauvegarde ou met à jour un produit dans le dépôt.
     *
     * @param product le produit à persister
     */
    void save(Product product);

    /**
     * Recherche un produit par son identifiant unique.
     *
     * @param id l'identifiant du produit
     * @return un {@link Optional} contenant le produit si trouvé, ou vide sinon
     */
    Optional<Product> findById(ProductId id);

    /**
     * Vérifie l’existence d’un produit à partir de son identifiant SKU.
     *
     * @param skuId l'identifiant SKU du produit
     * @return {@code true} si un produit avec ce SKU existe, {@code false} sinon
     */
    boolean existsBySkuId(SkuId skuId);
}

package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import java.util.UUID;

import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant un produit dans la base de données.
 * <p>
 * Cette classe correspond à la table <code>domain.products</code>
 * et contient les informations persistées d’un produit, 
 * y compris l’identifiant, le nom, la description, le SKU,
 * le statut et la version pour la gestion de concurrence optimiste.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "domain",
    name = "products",
    indexes = {
        @Index(name = "ux_products_sku", columnList = "sku", unique = true)
    })
public class ProductEntity {

    /** Identifiant unique du produit (UUID). */
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    /** Nom du produit. */
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    /** Description du produit. */
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    /** Identifiant SKU unique du produit. */
    @Column(name = "sku_id", nullable = false, updatable = false, length = 9, unique = true, columnDefinition = "varchar(9)")
    private String skuId;

    /** Statut de vie du produit (ex : ACTIVE, RETIRED). */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;

    /** Version de l’entité pour la gestion de concurrence optimiste. */
    @Column(name = "version", nullable = false, columnDefinition = "bigint")
    private Long version;
}

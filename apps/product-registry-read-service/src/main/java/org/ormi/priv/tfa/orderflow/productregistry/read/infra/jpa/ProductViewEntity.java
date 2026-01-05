package org.ormi.priv.tfa.orderflow.productregistry.read.infra.jpa;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductLifecycle;

import com.fasterxml.jackson.databind.JsonNode;

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
 * Entité JPA représentant la vue en lecture d’un produit.
 * <p>
 * Stocke les informations du produit ainsi que les événements applicables
 * pour permettre des projections et des lectures rapides.
 * </p>
 * <p>
 * La table inclut les champs pour la pagination, la gestion des versions,
 * le statut, et les relations JSON pour les événements et catalogues.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "read_product_registry",
    name = "product_view",
    indexes = {
        @Index(name = "idx_prdview_sku", columnList = "sku_id")
    })
public class ProductViewEntity {

    /** Identifiant unique du produit */
    @Id
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID id;

    /** Version de la vue pour le contrôle de concurrence optimiste */
    @Column(name = "_version", nullable = false, columnDefinition = "bigint")
    private Long version;

    /** Identifiant SKU du produit */
    @Column(name = "sku_id", nullable = false, length = 9, unique = true, columnDefinition = "varchar(9)")
    private String skuId;

    /** Nom du produit */
    @Column(name = "name", nullable = false, columnDefinition = "text")
    private String name;

    /** Description du produit */
    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    /** Statut du cycle de vie du produit */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "text")
    private ProductLifecycle status;

    /** Événements JSON associés au produit pour projection */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "events", nullable = false, columnDefinition = "jsonb")
    private JsonNode events;

    /** Catalogues JSON liés au produit */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "catalogs", nullable = false, columnDefinition = "jsonb")
    private JsonNode catalogs;

    /** Date de création de la vue */
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant createdAt;

    /** Date de dernière mise à jour de la vue */
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamptz")
    private Instant updatedAt;
}

package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entité JPA représentant un événement persistant dans le journal des événements (Event Log)
 * pour un système CQRS/ES.
 *
 * <p>
 * Chaque événement encapsule les données métier sous forme JSON, ainsi que les métadonnées
 * nécessaires pour :
 * <ul>
 *   <li>Identifier l’agrégat concerné ({@link #aggregateType}, {@link #aggregateId})</li>
 *   <li>Maintenir l’ordre des événements via {@link #aggregateVersion}</li>
 *   <li>Versionner le schéma de l’événement via {@link #eventVersion}</li>
 *   <li>Traçabilité temporelle via {@link #occurredAt}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Champs JSON :
 * <ul>
 *   <li>{@link #payload} : le contenu complet de l’événement métier sérialisé en JSON</li>
 * </ul>
 * </p>
 *
 * <p>
 * Observabilité et scalabilité :
 * <ul>
 *   <li>correlation : identifiant global pour tracer une requête à travers plusieurs services</li>
 *   <li>causation : permet de lier des événements dépendants ou déclenchés par d’autres</li>
 *   <li>tenant : support multi-tenant pour isolation ou quotas</li>
 *   <li>shardKey : clé pour partitionner les données et distribuer la charge</li>
 * </ul>
 * </p>
 *
 * <p>
 * L’index {@code ix_eventlog_aggregate} permet de rechercher rapidement les événements par
 * agrégat et version, ce qui est crucial pour reconstruire les projections et détecter les
 * éventuels gaps dans l’historique.
 * </p>
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Entity
@Table(
    schema = "eventing",
    name = "event_log",
    indexes = {
        @Index(name = "ix_eventlog_aggregate", columnList = "aggregate_type, aggregate_id, aggregate_version")
    })
public class EventLogEntity {

    /** Identifiant unique de l’événement dans la base (généré automatiquement) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "bigserial")
    private Long id;

    /** Type de l’agrégat concerné (ex: "Product") */
    @Column(name = "aggregate_type", nullable = false, updatable = false, columnDefinition = "text")
    private String aggregateType;

    /** Identifiant unique de l’agrégat */
    @Column(name = "aggregate_id", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID aggregateId;

    /** Version de l’agrégat pour maintenir l’ordre des événements */
    @Column(name = "aggregate_version", nullable = false, updatable = false, columnDefinition = "bigint")
    private Long aggregateVersion;

    /** Type de l’événement métier (ex: "ProductRegistered") */
    @Column(name = "event_type", nullable = false, updatable = false, columnDefinition = "text")
    private String eventType;

    /** Version du schéma de l’événement */
    @Column(name = "event_version", nullable = false, updatable = false, columnDefinition = "int")
    private int eventVersion;

    /** Timestamp indiquant quand l’événement a été produit */
    @Column(name = "occurred_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant occurredAt;

    /** Payload JSON sérialisé contenant les données métier de l’événement */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, updatable = false, columnDefinition = "jsonb")
    private JsonNode payload;

    /**
     * TODO: Implémenter les métadonnées additionnelles pour observabilité et scalabilité :
     * <ul>
     *   <li>correlation : suivi global des requêtes</li>
     *   <li>causation : lier des événements liés</li>
     *   <li>tenant : support multi-tenant</li>
     *   <li>shardKey : partitionnement pour scalabilité</li>
     * </ul>
     */
}

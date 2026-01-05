package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent;
import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent.DomainEventPayload;
import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Mapper MapStruct pour convertir les {@link EventEnvelope} contenant des événements métier
 * en entités JPA {@link EventLogEntity} persistables.
 *
 * <p>
 * Cette classe est utilisée pour la persistance des événements dans le journal
 * (Event Log) dans un système CQRS/ES. Elle sérialise les payloads JSON et
 * déduit automatiquement le type, la version et les métadonnées de l’événement.
 * </p>
 *
 * <p>
 * Fonctionnalités principales :
 * <ul>
 *   <li>Mappe l’ID et le type de l’agrégat</li>
 *   <li>Mappe la version de l’agrégat à la séquence de l’événement</li>
 *   <li>Déduit le type et la version de l’événement métier</li>
 *   <li>Sérialise le payload en {@link JsonNode} pour stockage JSONB</li>
 *   <li>Utilise l’horodatage {@link EventEnvelope#timestamp()} pour {@link EventLogEntity#occurredAt}</li>
 * </ul>
 * </p>
 *
 * <p>
 * MapStruct configuration :
 * <ul>
 *   <li>componentModel = "cdi" : injection CDI des mappers</li>
 *   <li>injectionStrategy = CONSTRUCTOR : injection via constructeur</li>
 *   <li>unmappedTargetPolicy = IGNORE : ignore les champs non mappés pour éviter les warnings</li>
 * </ul>
 * </p>
 */
@Mapper(
    componentModel = "cdi",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface EventLogJpaMapper {

    /**
     * Convertit un {@link EventEnvelope} contenant un événement métier en
     * {@link EventLogEntity} prêt à être persisté.
     *
     * @param envelope L’enveloppe contenant l’événement métier et ses métadonnées.
     * @param objectMapper Mapper Jackson pour sérialiser le payload.
     * @return Une entité JPA représentant l’événement dans la base de données.
     */
    @Mapping(target = "aggregateType", expression = "java(envelope.aggregateType())")
    @Mapping(target = "aggregateId", expression = "java(envelope.aggregateId())")
    @Mapping(target = "aggregateVersion", expression = "java(envelope.sequence())")
    @Mapping(target = "eventType", expression = "java(resolveEventType(envelope.event()))")
    @Mapping(target = "eventVersion", expression = "java(resolveEventVersion(envelope.event()))")
    @Mapping(target = "occurredAt", expression = "java(envelope.timestamp())")
    @Mapping(target = "payload", expression = "java(toJson(envelope.event().payload(), objectMapper))")
    EventLogEntity toEntity(EventEnvelope<?> envelope, @Context ObjectMapper objectMapper);

    /**
     * Résout le type de l’événement métier.
     *
     * @param event L’événement métier.
     * @return Le nom du type d’événement.
     */
    default String resolveEventType(DomainEvent event) {
        return event.eventType();
    }

    /**
     * Résout la version du schéma de l’événement.
     *
     * @param event L’événement métier.
     * @return La version de l’événement.
     */
    default int resolveEventVersion(DomainEvent event) {
        return event.version();
    }

    /**
     * Sérialise le payload de l’événement en {@link JsonNode}.
     *
     * @param payload Le payload de l’événement.
     * @param om Jackson ObjectMapper.
     * @return Payload sérialisé en JSON.
     */
    default JsonNode toJson(DomainEventPayload payload, @Context ObjectMapper om) {
        return om.valueToTree(payload);
    }
}

package org.ormi.priv.tfa.orderflow.cqrs;

import java.time.Instant;
import java.util.UUID;

/**
 * Conteneur ("enveloppe") pour un événement métier (domain event) dans le système CQRS.
 *
 * <p>
 * Cette classe encapsule un {@link DomainEvent} avec des métadonnées supplémentaires :
 * <ul>
 *   <li>{@code sequence} : numéro de séquence global pour l’ordre des événements.</li>
 *   <li>{@code timestamp} : instant auquel l’événement a été produit.</li>
 * </ul>
 * Elle fournit également un accès aux informations de l’agrégat cible :
 * {@link #aggregateId()} et {@link #aggregateType()}.
 * </p>
 *
 * <p>
 * Usage typique : les {@link EventEnvelope} sont utilisés pour dispatcher
 * les événements vers les projections (read-side) ou pour persister les événements
 * dans un outbox/event store.
 * </p>
 *
 * @param <E> type de l’événement métier encapsulé
 */
public class EventEnvelope<E extends DomainEvent> {

    /** L’événement métier encapsulé */
    private final E event;

    /** Numéro de séquence de l’événement */
    private final Long sequence;

    /** Timestamp de création de l’enveloppe */
    private final Instant timestamp;

    /**
     * Crée une nouvelle enveloppe pour un événement métier.
     *
     * @param event l’événement métier
     * @param sequence numéro de séquence
     * @param timestamp moment de création de l’enveloppe
     */
    public EventEnvelope(E event, Long sequence, Instant timestamp) {
        this.event = event;
        this.sequence = sequence;
        this.timestamp = timestamp;
    }

    /** @return l’identifiant de l’agrégat associé à l’événement */
    public UUID aggregateId() {
        return event.aggregateId();
    }

    /** @return le type de l’agrégat associé à l’événement */
    public String aggregateType() {
        return event.aggregateType();
    }

    /** @return l’événement métier encapsulé */
    public E event() {
        return event;
    }

    /** @return le numéro de séquence de l’événement */
    public Long sequence() {
        return sequence;
    }

    /** @return le timestamp de création de l’enveloppe */
    public Instant timestamp() {
        return timestamp;
    }

    /**
     * Crée une enveloppe pour un événement métier avec un timestamp actuel.
     *
     * <p>Le numéro de séquence doit être fourni explicitement.
     *
     * @param event l’événement métier
     * @param sequence numéro de séquence
     * @param <E> type de l’événement
     * @return une nouvelle {@link EventEnvelope}
     */
    public static <E extends DomainEvent> EventEnvelope<E> with(E event, Long sequence) {
        return new EventEnvelope<>(event, sequence, Instant.now());
    }
}

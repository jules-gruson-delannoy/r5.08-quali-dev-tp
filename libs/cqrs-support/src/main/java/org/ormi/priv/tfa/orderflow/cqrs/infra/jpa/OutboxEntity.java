package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Représente un message dans l'outbox pour la publication d'événements.
 *
 * <p>
 * Chaque entrée correspond à un événement persisté dans {@link EventLogEntity} qui
 * doit être diffusé de manière asynchrone vers les consommateurs ou systèmes externes.
 * L’outbox permet de gérer la réémission en cas d’échec, avec un mécanisme de retry.
 * </p>
 *
 * <p>
 * Champs principaux :
 * <ul>
 *   <li>{@code id} : Identifiant unique du message dans l’outbox.</li>
 *   <li>{@code attempts} : Nombre de tentatives de publication.</li>
 *   <li>{@code nextAttemptAt} : Date/heure de la prochaine tentative planifiée.</li>
 *   <li>{@code lastError} : Message de la dernière erreur survenue lors de la tentative de publication.</li>
 *   <li>{@code sourceEvent} : Référence vers l’événement original dans {@link EventLogEntity}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Index :
 * <ul>
 *   <li>{@code ix_outbox_ready} : permet de récupérer rapidement les messages prêts à être traités
 *   en triant par {@code next_attempt_at}.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Notes :
 * <ul>
 *   <li>Construit via le builder {@link #Builder()} pour une initialisation fluide.</li>
 *   <li>Le mapping JPA utilise {@link ManyToOne} avec {@link FetchType#EAGER} pour accéder
 *       directement à l’événement source.</li>
 * </ul>
 * </p>
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(schema = "eventing", name = "outbox", indexes = {
        @Index(name = "ix_outbox_ready", columnList = "next_attempt_at")
})
public class OutboxEntity {
    /** Identifiant unique du message dans l’outbox */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "bigserial")
    private Long id;

    /** Nombre de tentatives effectuées pour publier ce message */
    @Column(name = "attempts", nullable = false, updatable = false, columnDefinition = "int")
    private int attempts;

    /** Date/heure de la prochaine tentative planifiée */
    @Column(name = "next_attempt_at", nullable = false, updatable = false, columnDefinition = "timestamptz")
    private Instant nextAttemptAt;

    /** Message de la dernière erreur survenue lors de la tentative de publication */
    @Column(name = "last_error", nullable = false, updatable = false, columnDefinition = "text")
    private String lastError;

    /** Référence vers l’événement original à publier */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id", nullable = false, updatable = false, columnDefinition = "bigint")
    private EventLogEntity sourceEvent;

    /**
     * Crée un builder pour initialiser un {@link OutboxEntity}.
     *
     * @return nouveau builder
     */
    public static OutboxEntityBuilder Builder() {
        return new OutboxEntityBuilder();
    }

    /** Builder simplifié pour {@link OutboxEntity} */
    public static class OutboxEntityBuilder {
        private EventLogEntity sourceEvent;

        /** Définit l’événement source à publier */
        public OutboxEntityBuilder sourceEvent(EventLogEntity evt) {
            this.sourceEvent = evt;
            return this;
        }

        /** Construit l’instance {@link OutboxEntity} */
        public OutboxEntity build() {
            OutboxEntity entity = new OutboxEntity();
            entity.sourceEvent = sourceEvent;
            return entity;
        }
    }
}

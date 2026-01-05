package org.ormi.priv.tfa.orderflow.cqrs.infra.persistence;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.EventLogEntity;

/**
 * Repository pour persister les événements du domaine dans le journal d’événements (Event Log).
 *
 * <p>
 * Cette interface fait partie de l’infrastructure CQRS/Event Sourcing et permet de stocker
 * de manière immuable chaque événement généré par les agrégats du domaine.
 * </p>
 *
 * <p>
 * Les implémentations doivent assurer :
 * <ul>
 *   <li>La persistance de chaque {@link EventEnvelope} dans {@link EventLogEntity}.</li>
 *   <li>Le maintien de l’ordre des versions des agrégats ({@code aggregateVersion}).</li>
 *   <li>La compatibilité avec les mécanismes d’outbox pour diffusion asynchrone des événements.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Exemple d’utilisation :
 * <pre>{@code
 * EventEnvelope<ProductRegisteredEvent> envelope = EventEnvelope.with(event, sequence);
 * EventLogEntity persisted = eventLogRepository.append(envelope);
 * }</pre>
 * </p>
 */
public interface EventLogRepository {

    /**
     * Persiste un événement du domaine dans le journal d’événements.
     *
     * @param eventLog l’enveloppe contenant l’événement à persister ainsi que son numéro de séquence et timestamp
     * @return l’entité persistée {@link EventLogEntity} représentant l’événement stocké
     */
    EventLogEntity append(EventEnvelope<?> eventLog);
}

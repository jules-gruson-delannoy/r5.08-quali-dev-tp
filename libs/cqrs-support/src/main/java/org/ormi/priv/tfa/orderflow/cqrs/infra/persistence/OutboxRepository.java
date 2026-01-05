package org.ormi.priv.tfa.orderflow.cqrs.infra.persistence;

import java.util.List;

import org.ormi.priv.tfa.orderflow.cqrs.infra.jpa.OutboxEntity;

/**
 * Repository pour la gestion de la table d’outbox dans un système CQRS/Event Sourcing.
 *
 * <p>
 * L’outbox est utilisée pour stocker de manière temporaire les événements qui doivent
 * être diffusés à d’autres systèmes ou services de manière asynchrone.
 * </p>
 *
 * <p>
 * Cette interface définit les opérations essentielles pour :
 * <ul>
 *   <li>Publier un événement dans l’outbox.</li>
 *   <li>Récupérer les événements prêts à être traités pour un type d’agrégat donné.</li>
 *   <li>Supprimer ou marquer comme échoué un événement après traitement.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Exemple d’utilisation :
 * <pre>{@code
 * OutboxEntity outboxEntity = OutboxEntity.Builder()
 *     .sourceEvent(eventLogEntity)
 *     .build();
 * outboxRepository.publish(outboxEntity);
 *
 * List<OutboxEntity> readyEvents = outboxRepository.fetchReadyByAggregateTypeOrderByAggregateVersion(
 *     "PRODUCT", 10, 3
 * );
 * for (OutboxEntity evt : readyEvents) {
 *     try {
 *         dispatcher.dispatch(evt.getSourceEvent());
 *         outboxRepository.delete(evt);
 *     } catch (Exception ex) {
 *         outboxRepository.markFailed(evt, ex.getMessage(), 5000);
 *     }
 * }
 * }</pre>
 * </p>
 */
public interface OutboxRepository {

    /**
     * Publie un événement dans l’outbox pour traitement ultérieur.
     *
     * @param entity l’entité {@link OutboxEntity} à publier
     */
    void publish(OutboxEntity entity);

    /**
     * Récupère les événements prêts à être traités pour un type d’agrégat donné,
     * triés par version de l’agrégat.
     *
     * @param aggregateType le type d’agrégat (ex. "PRODUCT")
     * @param limit le nombre maximum d’événements à récupérer
     * @param maxRetries le nombre maximum de tentatives avant de considérer un événement comme échoué
     * @return la liste des {@link OutboxEntity} prêts à être traités
     */
    List<OutboxEntity> fetchReadyByAggregateTypeOrderByAggregateVersion(String aggregateType, int limit, int maxRetries);

    /**
     * Supprime un événement de l’outbox après traitement réussi.
     *
     * @param entity l’entité {@link OutboxEntity} à supprimer
     */
    void delete(OutboxEntity entity);

    /**
     * Marque un événement comme ayant échoué avec un délai de réessai par défaut.
     *
     * @param entity l’entité {@link OutboxEntity} à marquer comme échouée
     * @param err le message d’erreur à enregistrer
     */
    void markFailed(OutboxEntity entity, String err);

    /**
     * Marque un événement comme ayant échoué avec un délai de réessai personnalisé.
     *
     * @param entity l’entité {@link OutboxEntity} à marquer comme échouée
     * @param err le message d’erreur à enregistrer
     * @param retryAfter délai en millisecondes avant la prochaine tentative
     */
    void markFailed(OutboxEntity entity, String err, int retryAfter);
}

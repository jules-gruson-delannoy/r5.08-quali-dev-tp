package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.OutboxRepository;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du {@link OutboxRepository} utilisant Panache.
 *
 * <p>
 * Cette classe gère la persistance des messages dans l’outbox pour la publication
 * asynchrone d’événements, en suivant le pattern Outbox.
 * </p>
 *
 * <p>
 * Fonctionnalités :
 * <ul>
 *   <li>Persist les messages à publier via {@link #publish(OutboxEntity)}</li>
 *   <li>Récupère les messages prêts à être traités par {@link #fetchReadyByAggregateTypeOrderByAggregateVersion(String, int, int)}</li>
 *   <li>Supprime un message traité via {@link #delete(OutboxEntity)}</li>
 *   <li>Marque un message comme échoué et planifie une nouvelle tentative via {@link #markFailed(OutboxEntity, String, int)}</li>
 *   <li>Chargement dynamique des requêtes SQL natives pour la sélection des messages prêts</li>
 * </ul>
 * </p>
 *
 * <p>
 * Notes techniques :
 * <ul>
 *   <li>Utilise PanacheRepository pour simplifier l’accès à JPA/Hibernate</li>
 *   <li>Transactional sur les opérations de modification/de suppression</li>
 *   <li>Supporte un délai par défaut de 5 secondes pour la planification des nouvelles tentatives</li>
 * </ul>
 * </p>
 */
@ApplicationScoped
@DefaultBean
public class JpaOutboxRepository implements PanacheRepository<OutboxEntity>, OutboxRepository {
    private static final int DEFAULT_DELAY_MS = 5000;
    private static final String SQL_FETCH_QUERY = loadSQLQueryFromFile("/db/queries/findReadyByAggregateTypeOrderByAggregateVersion.sql");

    /**
     * Persiste un message dans l’outbox.
     *
     * @param entity Le message à publier
     */
    @Override
    public void publish(OutboxEntity entity) {
        persist(entity);
    }

    /**
     * Récupère les messages prêts à être traités pour un type d’agrégat donné,
     * triés par version d’agrégat croissante.
     *
     * @param aggregateType Type d’agrégat
     * @param limit Nombre maximal de messages à récupérer
     * @param maxRetries Nombre maximal de tentatives autorisées
     * @return Liste des messages prêts
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<OutboxEntity> fetchReadyByAggregateTypeOrderByAggregateVersion(String aggregateType, int limit, int maxRetries) {
        return (List<OutboxEntity>) getEntityManager()
                .createNativeQuery(SQL_FETCH_QUERY, OutboxEntity.class)
                .setParameter("aggregateTypes", aggregateType)
                .setParameter("maxAttempts", maxRetries)
                .setMaxResults(limit)
                .getResultList();
    }

    /**
     * Supprime un message de l’outbox après traitement.
     *
     * @param entity Message à supprimer
     */
    @Transactional
    @Override
    public void delete(OutboxEntity entity) {
        deleteById(entity.getId());
    }

    /**
     * Marque un message comme échoué avec le délai par défaut.
     *
     * @param entity Message échoué
     * @param err Description de l’erreur
     */
    @Override
    public void markFailed(OutboxEntity entity, String err) {
        markFailed(entity, err, DEFAULT_DELAY_MS);
    }

    /**
     * Marque un message comme échoué et planifie la prochaine tentative.
     *
     * @param entity Message échoué
     * @param err Description de l’erreur
     * @param delayMs Délai avant la prochaine tentative en millisecondes
     */
    @Transactional
    @Override
    public void markFailed(OutboxEntity entity, String err, int delayMs) {
        update("lastError = ?1, nextAttemptAt = ?2, attempts = attempts + 1 WHERE id = ?3",
                err, Instant.now().plusMillis(delayMs), entity.getId());
    }

    /**
     * Charge une requête SQL depuis un fichier classpath.
     *
     * @param classpath Chemin vers le fichier SQL
     * @return Contenu de la requête SQL
     */
    private static String loadSQLQueryFromFile(String classpath) {
        try (InputStream is = JpaOutboxRepository.class.getResourceAsStream(classpath)) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

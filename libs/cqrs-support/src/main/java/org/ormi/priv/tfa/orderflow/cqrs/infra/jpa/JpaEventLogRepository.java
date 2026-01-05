package org.ormi.priv.tfa.orderflow.cqrs.infra.jpa;

import org.ormi.priv.tfa.orderflow.cqrs.EventEnvelope;
import org.ormi.priv.tfa.orderflow.cqrs.infra.persistence.EventLogRepository;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.arc.DefaultBean;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implémentation JPA du {@link EventLogRepository} utilisant Panache pour
 * persister les événements métier dans la base de données.
 *
 * <p>
 * Chaque événement est converti en {@link EventLogEntity} via le {@link EventLogJpaMapper}
 * et stocké dans la table {@code eventing.event_log}.
 * </p>
 *
 * <p>
 * Caractéristiques :
 * <ul>
 *   <li>Supporte la persistance transactionnelle via {@link Transactional}</li>
 *   <li>Utilise l’injection CDI pour le mapper {@link EventLogJpaMapper} et l’ObjectMapper Jackson</li>
 *   <li>Extends {@link PanacheRepository} pour bénéficier des méthodes utilitaires de Panache</li>
 *   <li>Annoté {@link DefaultBean} pour permettre un remplacement éventuel par une autre implémentation</li>
 * </ul>
 * </p>
 */
@ApplicationScoped
@DefaultBean
public class JpaEventLogRepository implements PanacheRepository<EventLogEntity>, EventLogRepository {

    private final EventLogJpaMapper mapper;
    private final ObjectMapper objectMapper;

    @Inject
    public JpaEventLogRepository(EventLogJpaMapper mapper, ObjectMapper objectMapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }

    /**
     * Persiste un événement métier dans le journal (Event Log).
     *
     * <p>
     * Convertit d’abord l’{@link EventEnvelope} en {@link EventLogEntity} via le mapper,
     * puis le persiste en base de données via Panache.
     * </p>
     *
     * @param eventLog L’événement métier enveloppé à persister
     * @return L’entité {@link EventLogEntity} persistée
     */
    @Override
    @Transactional
    public EventLogEntity append(EventEnvelope<?> eventLog) {
        EventLogEntity entity = mapper.toEntity(eventLog, objectMapper);
        persist(entity);
        return entity;
    }
}


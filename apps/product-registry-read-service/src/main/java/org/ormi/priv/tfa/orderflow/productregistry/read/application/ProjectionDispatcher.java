package org.ormi.priv.tfa.orderflow.productregistry.read.application;

import java.util.Optional;

import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductStreamElementDto;
import org.ormi.priv.tfa.orderflow.cqrs.Projector.ProjectionResult;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1Envelope;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductId;
import org.ormi.priv.tfa.orderflow.kernel.product.persistence.ProductViewRepository;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.productregistry.read.projection.ProductViewProjector;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.resource.spi.IllegalStateException;
import jakarta.transaction.Transactional;

/**
 * Dispatcher qui applique les événements produits aux projections de lecture.
 * <p>
 * Cette classe écoute les événements du produit et utilise les projecteurs
 * pour mettre à jour les vues {@link ProductView}. Elle diffuse ensuite
 * les changements via {@link ProductEventBroadcaster}.
 * </p>
 */
@ApplicationScoped
public class ProjectionDispatcher {

    private static final String PRODUCT_AGGREGATE_TYPE = Product.class.getSimpleName();

    private final Instance<ProductViewProjector> productViewProjector;
    private final ProductViewRepository productViewRepository;
    private final ProductEventBroadcaster productEventBroadcaster;

    /**
     * Constructeur avec injection des dépendances.
     *
     * @param productViewProjector instance des projecteurs de vues produit
     * @param productViewRepository repository des vues produit
     * @param productEventBroadcaster diffuseur d’événements produit
     */
    @Inject
    public ProjectionDispatcher(
            Instance<ProductViewProjector> productViewProjector,
            ProductViewRepository productViewRepository,
            ProductEventBroadcaster productEventBroadcaster) {
        this.productViewProjector = productViewProjector;
        this.productViewRepository = productViewRepository;
        this.productEventBroadcaster = productEventBroadcaster;
    }

    /**
     * Applique un événement produit à sa projection correspondante.
     * <p>
     * Si le type d’agrégat correspond à un produit, la projection est
     * mise à jour, persistée et l’événement est diffusé via le broadcaster.
     * </p>
     *
     * @param event événement produit enveloppé
     * @return résultat de la projection {@link ProjectionResult}
     * @throws IllegalStateException si le type d’agrégat ne correspond pas à un produit
     */
    @Transactional
    public ProjectionResult<ProductView> dispatch(ProductEventV1Envelope<?> event) throws IllegalStateException {
        if (event.aggregateType().equals(PRODUCT_AGGREGATE_TYPE)) {
            final Optional<ProductView> currentView = productViewRepository
                    .findById(new ProductId(event.aggregateId()));
            final ProjectionResult<ProductView> result = productViewProjector.get().project(currentView, event);
            if (result.isFailure()) {
                // TODO: Hey ! Log the failure. It is not a normal case
                return result;
            }
            if (result.isNoOp()) {
                // TODO: Log info. It may happen if ordering is temporarily broken
            }
            if (result.isSuccess()) {
                productViewRepository.save(result.getProjection());
                productEventBroadcaster.broadcast(new ProductStreamElementDto(
                    event.event().eventType(),
                    event.aggregateId().toString(),
                    event.timestamp()
                ));
            }
            return result;
        }
        throw new IllegalStateException("Unmatched aggregate type: " + event.aggregateType());
    }
}

package org.ormi.priv.tfa.orderflow.productregistry.read.infra.web.dto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewDtoCatalog;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewDtoEvent;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewDtoEventType;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewEventDtoPayload;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewEventDtoPayload.ProductDescriptionUpdatedPayloadDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewEventDtoPayload.ProductNameUpdatedPayloadDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewEventDtoPayload.ProductRegisteredPayloadDto;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read.ProductViewDto.ProductViewEventDtoPayload.ProductRetiredPayloadDto;
import org.ormi.priv.tfa.orderflow.cqrs.DomainEvent.DomainEventPayload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductDescriptionUpdated.ProductDescriptionUpdatedPayload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductEventV1Payload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductEventV1Payload.Empty;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductNameUpdated.ProductNameUpdatedPayload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductEventV1.ProductRegistered.ProductRegisteredPayload;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductEventType;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewCatalogRef;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewEvent;

/**
 * Mapper MapStruct pour convertir les vues de produit {@link ProductView} vers les DTOs
 * REST {@link ProductViewDto} exposés par l’API de lecture.
 * <p>
 * Gère la conversion des événements et des références de catalogues, ainsi que des types
 * d’événements, pour fournir une représentation cohérente côté API.
 * </p>
 */
@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = {
        ProductIdMapper.class,
        SkuIdMapper.class,
        ProductViewDtoMapper.ProductViewDtoEventMapper.class,
        ProductViewDtoMapper.ProductViewDtoCatalogMapper.class
    },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ProductViewDtoMapper {

    /** Convertit une vue domaine {@link ProductView} en DTO {@link ProductViewDto} */
    ProductViewDto toDto(ProductView productView);

    /**
     * Mapper pour les événements d’un produit {@link ProductViewEvent} vers {@link ProductViewDtoEvent}.
     */
    @Mapper(
        componentModel = "cdi",
        builder = @Builder(disableBuilder = false),
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = { ProductEventTypeMapper.class, ProductViewDtoEventMapper.ProductViewDtoPayloadMapper.class }
    )
    interface ProductViewDtoEventMapper {

        /** Convertit un événement domaine en DTO */
        ProductViewDtoEvent toDto(ProductViewEvent event);

        /**
         * Mapper pour les payloads d’événements vers les DTOs correspondants.
         */
        @Mapper(componentModel = "cdi", unmappedTargetPolicy = ReportingPolicy.IGNORE)
        interface ProductViewDtoPayloadMapper {

            ProductRegisteredPayloadDto toDto(ProductRegisteredPayload payload);

            ProductNameUpdatedPayloadDto toDto(ProductNameUpdatedPayload payload);

            ProductDescriptionUpdatedPayloadDto toDto(ProductDescriptionUpdatedPayload payload);

            ProductRetiredPayloadDto toDto(Empty payload);

            /**
             * Dispatcher pour MapStruct permettant de mapper une interface générique
             * {@link DomainEventPayload} vers le DTO spécifique correspondant.
             */
            default ProductViewEventDtoPayload map(DomainEventPayload payload) {
                if (payload == null) return null;
                if (payload instanceof ProductRegisteredPayload p) return toDto(p);
                if (payload instanceof ProductNameUpdatedPayload p) return toDto(p);
                if (payload instanceof ProductDescriptionUpdatedPayload p) return toDto(p);
                if (payload instanceof ProductEventV1Payload.Empty p) return toDto(p);
                throw new IllegalArgumentException("Unknown payload type: " + payload.getClass());
            }
        }
    }

    /**
     * Mapper pour les références de catalogues {@link ProductViewCatalogRef} vers DTO {@link ProductViewDtoCatalog}.
     */
    @Mapper(
        componentModel = "cdi",
        builder = @Builder(disableBuilder = false),
        unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    interface ProductViewDtoCatalogMapper {
        ProductViewDtoCatalog toDto(ProductViewCatalogRef catalog);
    }

    /**
     * Mapper pour les types d’événements {@link ProductEventType} vers {@link ProductViewDtoEventType}.
     */
    @Mapper(
        componentModel = "cdi",
        builder = @Builder(disableBuilder = false),
        unmappedTargetPolicy = ReportingPolicy.IGNORE
    )
    interface ProductEventTypeMapper {

        @ValueMappings({
            @ValueMapping(source = "PRODUCT_REGISTERED", target = "REGISTERED"),
            @ValueMapping(source = "PRODUCT_NAME_UPDATED", target = "NAME_UPDATED"),
            @ValueMapping(source = "PRODUCT_DESCRIPTION_UPDATED", target = "DESCRIPTION_UPDATED"),
            @ValueMapping(source = "PRODUCT_RETIRED", target = "RETIRED")
        })
        ProductViewDtoEventType toDto(ProductEventType event);
    }
}

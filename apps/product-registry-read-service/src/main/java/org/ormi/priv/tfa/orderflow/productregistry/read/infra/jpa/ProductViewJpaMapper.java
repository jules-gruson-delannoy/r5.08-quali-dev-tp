package org.ormi.priv.tfa.orderflow.productregistry.read.infra.jpa;

import java.io.IOException;
import java.util.List;

import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewCatalogRef;
import org.ormi.priv.tfa.orderflow.kernel.product.views.ProductView.ProductViewEvent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Mapper MapStruct pour convertir entre {@link ProductView} et {@link ProductViewEntity}.
 * <p>
 * Permet de sérialiser les listes d’événements et de références de catalogues en JSON
 * pour la persistance, et de les désérialiser lors de la lecture.
 * </p>
 * <p>
 * Utilise {@link ProductIdMapper} et {@link SkuIdMapper} pour gérer les identifiants.
 * L’instance de {@link ObjectMapper} est injectée via le contexte MapStruct.
 * </p>
 */
@Mapper(
    componentModel = "cdi",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    uses = {ProductIdMapper.class, SkuIdMapper.class }
)
public interface ProductViewJpaMapper {

    /**
     * Convertit une vue domaine {@link ProductView} en entité JPA {@link ProductViewEntity}.
     *
     * @param productView vue domaine
     * @param objectMapper mapper JSON pour sérialisation
     * @return entité JPA correspondante
     */
    @Mapping(target = "events", expression = "java(productViewEventListToJsonNode(productView.getEvents(), objectMapper))")
    @Mapping(target = "catalogs", expression = "java(productViewCatalogRefListToJsonNode(productView.getCatalogs(), objectMapper))")
    ProductViewEntity toEntity(ProductView productView, @Context ObjectMapper objectMapper);

    /**
     * Convertit une entité JPA {@link ProductViewEntity} en vue domaine {@link ProductView}.
     *
     * @param entity entité JPA
     * @param objectMapper mapper JSON pour désérialisation
     * @return vue domaine correspondante
     */
    @Mapping(target = "events", expression = "java(jsonNodeToProductViewEventList(entity.getEvents(), objectMapper))")
    @Mapping(target = "catalogs", expression = "java(jsonNodeToProductViewCatalogRefList(entity.getCatalogs(), objectMapper))")
    ProductView toDomain(ProductViewEntity entity, @Context ObjectMapper objectMapper);

    /**
     * Met à jour une entité JPA existante à partir d’une vue domaine.
     *
     * @param productView vue domaine
     * @param entity entité JPA à mettre à jour
     * @param objectMapper mapper JSON pour sérialisation
     */
    @Mapping(target = "events", expression = "java(productViewEventListToJsonNode(productView.getEvents(), objectMapper))")
    @Mapping(target = "catalogs", expression = "java(productViewCatalogRefListToJsonNode(productView.getCatalogs(), objectMapper))")
    void updateEntity(ProductView productView, @MappingTarget ProductViewEntity entity, @Context ObjectMapper objectMapper);

    // === JSON helpers ===

    default JsonNode productViewEventListToJsonNode(List<ProductViewEvent> events, @Context ObjectMapper om) {
        return om.valueToTree(events);
    }

    default JsonNode productViewCatalogRefListToJsonNode(List<ProductViewCatalogRef> catalogRefs, @Context ObjectMapper om) {
        return om.valueToTree(catalogRefs);
    }

    default List<ProductViewEvent> jsonNodeToProductViewEventList(JsonNode node, @Context ObjectMapper om) {
        try {
            return om.readValue(
                om.treeAsTokens(node),
                new TypeReference<List<ProductViewEvent>>() {}
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize ProductViewEvent list", e);
        }
    }

    default List<ProductViewCatalogRef> jsonNodeToProductViewCatalogRefList(JsonNode node, @Context ObjectMapper om) {
        try {
            return om.readValue(
                om.treeAsTokens(node),
                new TypeReference<List<ProductViewCatalogRef>>() {}
            );
        } catch (IOException e) {
            throw new IllegalStateException("Failed to deserialize ProductViewCatalogRef list", e);
        }
    }
}

package org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.read;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * DTO représentant la vue en lecture d’un produit.
 *
 * <p>
 * Contient l’ensemble des informations d’un produit ainsi que l’historique
 * des événements qui ont modifié son état. Ce DTO est destiné à être
 * exposé via l’API de lecture (read-side) du Product Registry.
 * </p>
 *
 * <p>
 * Les champs incluent :
 * <ul>
 *   <li>{@code id} : identifiant unique du produit (UUID sous forme de chaîne).</li>
 *   <li>{@code skuId} : code SKU du produit.</li>
 *   <li>{@code name} : nom actuel du produit.</li>
 *   <li>{@code status} : statut actuel du produit (ex. ACTIVE, RETIRED).</li>
 *   <li>{@code description} : description actuelle du produit.</li>
 *   <li>{@code catalogs} : liste des catalogues associés au produit.</li>
 *   <li>{@code events} : liste des événements appliqués au produit.</li>
 *   <li>{@code createdAt} et {@code updatedAt} : timestamps de création et dernière mise à jour.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Les événements sont typés et peuvent être désérialisés automatiquement
 * grâce aux annotations {@link JsonTypeInfo} et {@link JsonSubTypes}.
 * </p>
 *
 * @see ProductViewDto.ProductViewDtoEvent
 * @see ProductViewDto.ProductViewDtoEventType
 */
public record ProductViewDto(
        String id,
        String skuId,
        String name,
        String status,
        String description,
        List<ProductViewDtoCatalog> catalogs,
        List<ProductViewDtoEvent> events,
        String createdAt,
        String updatedAt) {

    /**
     * DTO représentant un catalogue lié à un produit.
     */
    public static record ProductViewDtoCatalog(
            String id,
            String name) {
    }

    /**
     * Interface scellée représentant le payload d’un événement de produit.
     *
     * <p>
     * Utilisée pour la désérialisation polymorphique des événements
     * via {@link JsonTypeInfo} et {@link JsonSubTypes}.
     * </p>
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductRegisteredPayloadDto.class, name = "ProductRegistered"),
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductNameUpdatedPayloadDto.class, name = "ProductNameUpdated"),
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductDescriptionUpdatedPayloadDto.class, name = "ProductDescriptionUpdated"),
        @JsonSubTypes.Type(value = ProductViewEventDtoPayload.ProductRetiredPayloadDto.class, name = "ProductRetired")
    })
    public sealed interface ProductViewEventDtoPayload {

        /**
         * Payload pour un produit nouvellement enregistré.
         */
        public record ProductRegisteredPayloadDto(
                String skuId,
                String name,
                String description) implements ProductViewEventDtoPayload {
        }

        /**
         * Payload pour la mise à jour du nom d’un produit.
         */
        public record ProductNameUpdatedPayloadDto(
                String oldName,
                String newName) implements ProductViewEventDtoPayload {
        }

        /**
         * Payload pour la mise à jour de la description d’un produit.
         */
        public record ProductDescriptionUpdatedPayloadDto(
                String oldDescription,
                String newDescription) implements ProductViewEventDtoPayload {
        }

        /**
         * Payload pour le retrait d’un produit.
         */
        public record ProductRetiredPayloadDto() implements ProductViewEventDtoPayload {
        }
    }

    /**
     * DTO représentant un événement sur le produit.
     *
     * @param type type de l’événement
     * @param timestamp date et heure de l’événement
     * @param sequence numéro de séquence de l’événement
     * @param payload payload spécifique à l’événement
     */
    public static record ProductViewDtoEvent(
            ProductViewDtoEventType type,
            String timestamp,
            Long sequence,
            ProductViewEventDtoPayload payload) {
    }

    /**
     * Enum des types d’événements de produit.
     *
     * <p>
     * Utilisé pour sérialiser/désérialiser l’événement dans les DTO.
     * </p>
     */
    public static enum ProductViewDtoEventType {
        REGISTERED("ProductRegistered"),
        NAME_UPDATED("ProductNameUpdated"),
        DESCRIPTION_UPDATED("ProductDescriptionUpdated"),
        RETIRED("ProductRetired");

        private final String value;

        ProductViewDtoEventType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @JsonCreator
        public static ProductViewDtoEventType fromValue(String value) {
            for (ProductViewDtoEventType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Unknown " + ProductViewDtoEventType.class.getSimpleName() + ": " + value);
        }
    }
}

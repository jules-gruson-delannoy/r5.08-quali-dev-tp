package org.ormi.priv.tfa.orderflow.store.infra.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * DTO représentant une requête de mise à jour d'un produit.
 *
 * <p>
 * Contient l'identifiant du produit et un tableau d'opérations de mise à jour.
 * Chaque opération peut être de type {@link UpdateNameOperation} ou
 * {@link UpdateDescriptionOperation}.
 * </p>
 *
 * <p>
 * Utilisé par {@link org.ormi.priv.tfa.orderflow.store.infra.api.ProductRpcResource#updateProduct(UpdateProductDto)}
 * pour envoyer plusieurs mises à jour dans un seul appel.
 * </p>
 *
 * @param id identifiant unique du produit à mettre à jour
 * @param operations tableau d'opérations de mise à jour
 */
public record UpdateProductDto(String id, UpdateOperation[] operations) {

    /**
     * Interface de base pour toutes les opérations de mise à jour.
     * <p>
     * Les sous-classes sont discriminées par la propriété JSON {@code type}.
     * </p>
     */
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = true
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = UpdateNameOperation.class, name = "UpdateProductName"),
        @JsonSubTypes.Type(value = UpdateDescriptionOperation.class, name = "UpdateProductDescription")
    })
    public interface UpdateOperation {
        /**
         * Type de l'opération.
         */
        UpdateProductOperationType type();
    }

    /**
     * Opération de mise à jour du nom du produit.
     *
     * @param type type de l'opération (toujours {@link UpdateProductOperationType#UPDATE_NAME})
     * @param payload payload contenant le nouveau nom
     */
    @JsonTypeName("UpdateProductName")
    public record UpdateNameOperation(UpdateProductOperationType type, UpdateNameOperationPayload payload)
            implements UpdateOperation {
        /**
         * Payload pour {@link UpdateNameOperation}.
         *
         * @param name nouveau nom du produit
         */
        public record UpdateNameOperationPayload(String name) {}
    }

    /**
     * Opération de mise à jour de la description du produit.
     *
     * @param type type de l'opération (toujours {@link UpdateProductOperationType#UPDATE_DESCRIPTION})
     * @param payload payload contenant la nouvelle description
     */
    @JsonTypeName("UpdateProductDescription")
    public record UpdateDescriptionOperation(UpdateProductOperationType type, UpdateDescriptionOperationPayload payload)
            implements UpdateOperation {
        /**
         * Payload pour {@link UpdateDescriptionOperation}.
         *
         * @param description nouvelle description du produit
         */
        public record UpdateDescriptionOperationPayload(String description) {}
    }

    /**
     * Enumération des types d'opérations de mise à jour de produit.
     */
    public enum UpdateProductOperationType {
        /** Mise à jour du nom du produit */
        @JsonProperty("UpdateProductName")
        UPDATE_NAME,

        /** Mise à jour de la description du produit */
        @JsonProperty("UpdateProductDescription")
        UPDATE_DESCRIPTION;
    }
}


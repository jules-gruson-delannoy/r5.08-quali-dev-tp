package org.ormi.priv.tfa.orderflow.productregistry.infra.web.dto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.productregistry.application.ProductCommand.RegisterProductCommand;
import org.ormi.priv.tfa.orderflow.contracts.productregistry.v1.write.RegisterProductCommandDto;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * Mapper MapStruct pour convertir entre les DTOs HTTP et les commandes du domaine.
 * <p>
 * Fournit des méthodes pour transformer un {@link RegisterProductCommandDto}
 * reçu via l’API en {@link RegisterProductCommand} du domaine, et inversement.
 * </p>
 * <p>
 * Utilise {@link SkuIdMapper} pour mapper les identifiants SKU.
 * </p>
 */
@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = true),
    uses = { SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CommandDtoMapper {

    /**
     * Convertit un DTO HTTP en commande de domaine.
     *
     * @param dto DTO d’enregistrement de produit
     * @return commande de domaine correspondante
     */
    RegisterProductCommand toCommand(RegisterProductCommandDto dto);

    /**
     * Convertit une commande de domaine en DTO HTTP.
     *
     * @param command commande de domaine
     * @return DTO correspondant
     */
    RegisterProductCommandDto toDto(RegisterProductCommand command);
}

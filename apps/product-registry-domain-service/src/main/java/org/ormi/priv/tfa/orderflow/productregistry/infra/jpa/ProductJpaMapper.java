package org.ormi.priv.tfa.orderflow.productregistry.infra.jpa;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.ormi.priv.tfa.orderflow.kernel.Product;
import org.ormi.priv.tfa.orderflow.kernel.product.ProductIdMapper;
import org.ormi.priv.tfa.orderflow.kernel.product.SkuIdMapper;

/**
 * Mapper MapStruct pour convertir entre {@link ProductEntity} et
 * l’objet de domaine {@link Product}.
 * <p>
 * Fournit des méthodes pour transformer une entité JPA en domaine,
 * mettre à jour une entité existante à partir d’un objet de domaine,
 * et créer une entité à partir d’un objet de domaine.
 * </p>
 * <p>
 * Utilise {@link ProductIdMapper} et {@link SkuIdMapper} pour mapper
 * les identifiants de produit et les SKU.
 * </p>
 */
@Mapper(
    componentModel = "cdi",
    builder = @Builder(disableBuilder = false),
    uses = { ProductIdMapper.class, SkuIdMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class ProductJpaMapper {

    /**
     * Convertit une entité JPA en objet de domaine.
     *
     * @param entity entité JPA
     * @return objet de domaine correspondant
     */
    public abstract Product toDomain(ProductEntity entity);

    /**
     * Met à jour une entité JPA existante à partir d’un objet de domaine.
     *
     * @param product objet de domaine
     * @param entity entité JPA à mettre à jour
     */
    public abstract void updateEntity(Product product, @MappingTarget ProductEntity entity);

    /**
     * Crée une entité JPA à partir d’un objet de domaine.
     *
     * @param product objet de domaine
     * @return entité JPA correspondante
     */
    public abstract ProductEntity toEntity(Product product);
}

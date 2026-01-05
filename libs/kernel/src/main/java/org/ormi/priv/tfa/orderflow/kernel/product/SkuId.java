package org.ormi.priv.tfa.orderflow.kernel.product;

import jakarta.validation.constraints.NotNull;

/**
 * Représente l'identifiant unique d'un produit sous forme de SKU (Stock Keeping Unit).
 * <p>
 * Le SKU est utilisé pour identifier de manière unique un produit dans le catalogue
 * et dans toutes les opérations du domaine (enregistrements, mises à jour, projections, etc.).
 * </p>
 *
 * <h2>Format attendu :</h2>
 * <p>
 * Le SKU doit respecter le format suivant : <code>[A-Z]{3}-[0-9]{5}</code>
 * </p>
 * <ul>
 *     <li>Trois lettres majuscules</li>
 *     <li>Un tiret "-"</li>
 *     <li>Cinq chiffres</li>
 * </ul>
 * <p>
 * Exemples valides : <code>ABC-12345</code>, <code>XYZ-00001</code>
 * </p>
 *
 * <h2>Validation :</h2>
 * <p>
 * La validation est effectuée dans le constructeur du record. Si la valeur ne correspond pas
 * au format attendu, une {@link IllegalArgumentException} est levée.
 * </p>
 *
 * <h2>Usage :</h2>
 * <pre>{@code
 * SkuId sku = new SkuId("ABC-12345");
 * product.setSkuId(sku);
 * }</pre>
 */
public record SkuId(@NotNull String value) {
    private static final java.util.regex.Pattern SKU_PATTERN =
        java.util.regex.Pattern.compile("^[A-Z]{3}-\\d{5}$");

    public SkuId {
        if (!SKU_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Invalid SKU format, expected [Alpha]{3}-[Digit]{5}, got: " + value
            );
        }
    }
}

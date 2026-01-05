package org.ormi.priv.tfa.orderflow.productregistry.read;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d’entrée principal du module Product Registry Read.
 * <p>
 * Cette classe démarre l’application Quarkus dédiée à la lecture
 * des données du Product Registry.
 * </p>
 * <p>
 * Le démarrage effectif est délégué à {@link ProductRegistryReadApplication},
 * qui maintient l’application active jusqu’à son arrêt.
 * </p>
 */
@QuarkusMain
public class Main {

    /**
     * Méthode principale appelée au lancement du module.
     *
     * @param args arguments de ligne de commande
     */
    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryReadApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    /**
     * Implémentation de {@link QuarkusApplication} pour la lecture.
     * <p>
     * Bloque le thread principal jusqu’à l’arrêt de l’application Quarkus.
     * </p>
     */
    public static class ProductRegistryReadApplication implements QuarkusApplication {

        /**
         * Exécute l’application Quarkus et attend sa terminaison.
         *
         * @param args arguments de démarrage
         * @return code de sortie (0 si succès)
         * @throws Exception en cas d’erreur
         */
        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}

package org.ormi.priv.tfa.orderflow.productregistry;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d’entrée principal de l’application Quarkus
 * Product Registry Domain.
 */
@QuarkusMain
public class Main {

    /**
     * Démarre l’application Quarkus.
     *
     * @param args arguments de ligne de commande
     */
    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryDomainApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    /**
     * Application Quarkus maintenant le processus actif
     * jusqu’à son arrêt.
     */
    public static class ProductRegistryDomainApplication implements QuarkusApplication {

        /**
         * Exécute l’application et attend sa terminaison.
         *
         * @param args arguments de démarrage
         * @return code de sortie
         * @throws Exception en cas d’erreur
         */
        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}
package org.ormi.priv.tfa.orderflow.store;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

/**
 * Point d'entrée principal pour le module {@code store} de l'application.
 * 
 * <p>
 * Cette classe démarre l'application Quarkus pour le domaine Product Registry.
 * Elle initialise le contexte Quarkus et attend la fin du processus.
 * </p>
 *
 * <p>
 * <b>Structure :</b>
 * <ul>
 *   <li>La méthode {@link #main(String...)} lance Quarkus et exécute
 *       {@link ProductRegistryDomainApplication}.</li>
 *   <li>La classe interne {@link ProductRegistryDomainApplication} implémente
 *       {@link QuarkusApplication} et attend la terminaison de l'application.</li>
 * </ul>
 * </p>
 */
@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(
            ProductRegistryDomainApplication.class,
            (exitCode, exception) -> {},
            args);
    }

    /**
     * Application Quarkus pour le domaine Product Registry.
     * 
     * <p>
     * Implémente {@link QuarkusApplication} pour fournir un point d'entrée contrôlé
     * par Quarkus. La méthode {@link #run(String...)} bloque jusqu'à la fin du
     * processus.
     * </p>
     */
    public static class ProductRegistryDomainApplication implements QuarkusApplication {

        @Override
        public int run(String... args) throws Exception {
            Quarkus.waitForExit();
            return 0;
        }
    }
}

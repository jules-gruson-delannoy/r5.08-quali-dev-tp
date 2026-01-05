# **EXO 1:**

## Tache 1 Question

**Q1** - les domains principaux sont : Panier d'achat et Traitement des commandes

**Q2** - L'application order flow est composée de plusieurs services implémentant les domaines selon un modèle CQRS et piloté par les événements.

**Q3** - apps/store-back : Backend de la boutique (API panier, commandes, clients, etc.).​

apps/store-front : Frontend de la boutique (UI catalogue, panier, checkout).​

apps/product-registry-domain-service : Service de commande pour gérer le cycle de vie métier des produits (création, modification, règles).​

apps/product-registry-read-service : Service de lecture offrant des vues rapides et optimisées du catalogue produits.​

libs/kernel : Noyau commun avec types de base, erreurs et utilitaires partagés.​

libs/bom-platform : Logique partagée autour des Bills of Materials et de la composition de produits.​

libs/cqrs-support : Infrastructure générique pour commandes, requêtes et événements (CQRS).​

libs/sql : Accès SQL mutualisé (connexions, helpers, mapping, migrations).

## Tache 2 Question

**Q1** - Les concepts principaux sont la DDD (agrégats, bounded contexts), le CQRS (séparation lecture/écriture) et l’EDA (événements de domaine entre microservices).

**Q2** - DDD : logique métier et agrégats dans les services de domaine comme product-registry-domain-service, réutilisant des libs comme bom-platform et kernel.​

CQRS : commandes côté services de domaine, vues de lecture matérialisées dans product-registry-read-service consommées par store-back / store-front.​

EDA : changements d’état publiés en événements de domaine depuis les services métier, projetés en vues via les modules de lecture et supportés par libs/cqrs-support.

**Q3** - libs/cqrs-support fournit l’infrastructure générique CQRS : interfaces/abstractions pour commandes et requêtes, bus de dispatch, gestion des handlers et de la publication d’événements.​

Les modules métier (comme product-registry-domain-service ou store-back) implémentent leurs CommandHandlers/QueryHandlers en se basant sur ces abstractions, ce qui aligne directement la structure du code (handlers, pipelines, projections) avec les concepts métier de commandes, lectures et événements.

## Tache 3 

Il ne faut pas faire cette tâche

# EXO 2 :

## Tache 1

Il faut compléter la javadoc (pas fait)

## Tache 2
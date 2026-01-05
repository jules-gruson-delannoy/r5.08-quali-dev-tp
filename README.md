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

Javadoc faite

## Tache 5 Question

**Q1** 
Tests unitaires : vérifient qu’une fonction ou classe seule fonctionne correctement. Rapides et isolés (souvent avec des mocks).

Tests d’intégration : vérifient que plusieurs modules ou composants fonctionnent correctement ensemble. Plus lents et moins isolés.

En résumé : unitaires = code isolé, intégration = interaction entre modules.

**Q2**
Non, viser 100 % de couverture n’est pas pertinent :

Couverture n'est pas égal à qualité : exécuter du code ne garantit pas qu’il est bien testé.

Coût élevé : tester du code trivial prend du temps pour peu de valeur.

Maintenance difficile : tests inutiles deviennent un fardeau.

Priorisation : mieux vaut se concentrer sur les parties critiques du code.

**Q3**
Une architecture en couches d’oignon apporte plusieurs avantages pour les tests :

Isolation facile : chaque couche peut être testée séparément (unitaires).

Tests d’intégration ciblés : les dépendances sont inversées, donc on peut simuler facilement les couches externes.

Maintenance simplifiée : les changements dans une couche ont un impact minimal sur les tests des autres couches.

En bref, plus de modularité c'est des tests plus simples et fiables.

**Q4**
infra : infrastructure technique (accès à la base, services externes, configuration).

application : logique métier orchestrant les cas d’usage, souvent sans dépendances directes à la persistance ou au web.

jpa : classes liées à la persistance via JPA/Hibernate (entités, repositories).

web : interface web / API REST (contrôleurs, endpoints).

client : code pour consommer des services externes (API externes, microservices).

model : objets métier ou DTO partagés (entités, data transfer objects).

En résumé : infra = technique, application = logique, jpa/web/client = interface, model = données.

# EXO 4 :
## Tache 1 Questions sur la base de code

**Q1**

**Q2**

**Q3**

**Q4**

**Q5**

**Q6**

## Tache 2 Questions concernant l'Outboxing

**Q1**

**Q2**

**Q3**

**Q4**

## Tache 3 Questions concernant le journal d'évènements

**Q1**

**Q2**

**Q3**

## Tache 4 Limites de CQRS

**Q1**

**Q2**

**Q3**

**Q4**

**Q5**
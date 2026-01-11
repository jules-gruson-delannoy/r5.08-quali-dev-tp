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
Elle définit le contrat de transformation des événements métier en vues matérialisées. C'est le composant "Read Model" du CQRS qui permet de reconstruire un état à partir d'un historique d'événements.

**Q2**
Il représente le type générique de l'état cible. C'est la structure de données finale après application de la logique de projection.

**Q4**
L'interface permet de respecter le principe "Open/Closed" : on peut ajouter de nouvelles vues (ex: statistiques, facturation, inventaire) sans modifier le code qui émet les événements. Cela facilite aussi le "replay" (recalcul des vues) en changeant l'implémentation.

**Q5**
Il sert de conteneur de retour sécurisé. Plutôt que de renvoyer l'objet S directement (qui pourrait être null) ou de lever une exception, il transporte explicitement le succès, l'échec ou l'absence de changement (no-op), tout en conservant le contexte (message d'erreur).

**Q6**
La monade traite l'erreur comme une donnée et non comme une interruption brutale. Avantages : Permet de chainer les operations via flatMap sans blocs try-catch, ameliore la lisibilité en rendant le flux declaratif et evite les NullPointerException en forçant la verification du resultat.

## Tache 2 Questions concernant l'Outboxing

**Q1**
Elle gère une zone de stockage temporaire (tampon) pour les événements. Elle assure que l'enregistrement d'une modification métier et la préparation de l'événement associé sont liés de façon atomique.

**Q2**
Le pattern Outbox garantit une livraison "au moins une fois" (At-Least-Once). En insérant l'événement dans la base de données locale au sein de la même transaction que l'opération métier, on élimine le risque d'avoir un état mis à jour sans événement produit (ou inversement) en cas de crash réseau ou applicatif.

**Q3**
(flemme de faire un schéma en readme je sais pas comment faire monsieur)
Transaction : L'application écrit l'état métier (schéma domain) et l'événement (schéma eventing via publish) dans une seule transaction.

Extraction : Un worker appelle fetchReadyByAggregateType... pour récupérer les messages non traités, triés par version pour respecter la chronologie.

Dispatch : Le dispatcher tente d'envoyer l'événement aux abonnés.

Validation : En cas de succès, delete supprime l'entrée. En cas d'échec, markFailed la conserve pour un futur réessai.

**Q4**
Le système utilise la persistance pour gérer la résilience. Les erreurs sont logguées directement dans la table d'outbox (via err). Le mécanisme de retryAfter permet de ne pas surcharger le système en boucle : un événement qui échoue est "mis de côté" temporairement avant d'être à nouveau éligible via fetchReady, jusqu'à atteindre maxRetries.

## Tache 3 Questions concernant le journal d'évènements

**Q1**
Le journal d'événements sert de source de vérité unique et immuable retraçant l'historique complet du système. Contrairement aux tables métier qui stockent l'état présent, il archive chaque changement sous forme de faits indiscutables.

**Q2**
L'interface ne possède qu'une méthode append car le journal est par définition un flux à ajout uniquement. Supprimer un événement falsifierait l'histoire du système, tandis que la lecture est ici déléguée à d'autres composants pour séparer les responsabilités d'écriture transactionnelle et de consultation.

**Q3**
Cette conception garantit l'intégrité des données grâce à la contrainte d'unicité sur la version de l'agrégat, empêchant tout conflit lors de modifications simultanées. Au-delà de la persistance, ce journal permet l'audit complet des actions utilisateurs, le rejeu des événements pour reconstruire des vues corrompues et une analyse précise des bugs grâce au stockage du payload au format JSON.

## Tache 4 Limites de CQRS

**Q1**
Les principales limites de CQRS dans cette application sont la complexité accrue du code et la cohérence éventuelle. Puisque la vue est mise à jour après l'événement, un utilisateur peut ne pas voir son changement immédiatement.

**Q2**
L'application compense déjà la limite de perte de données et d'atomicité grâce au pattern Outbox. En liant la persistance de l'événement à la transaction métier, elle garantit que le modèle de lecture finira toujours par être synchronisé avec le modèle d'écriture sans perte d'information.

**Q3**
Cette mise en œuvre introduit toutefois une nouvelle limite liée à la scalabilité du worker de l'outbox. Si le volume d'événements devient trop important, le polling de la base de données pour dépiler les messages peut devenir un goulot d'étranglement et augmenter la latence de manière critique.

**Q4**
Dans le cas d'une projection multiple, un seul événement pourrait déclencher plusieurs mises à jour de vues différentes. Si l'une échoue, le système doit être capable de reprendre la file sans dupliquer les succès, ce qui complexifie la gestion des index de progression (offsets) pour chaque projecteur.

**Q5 (BONUS)**
Pour régler ça, on pourrait peut-être juste supprimer le journal d'événements une fois qu'il est lu pour gagner de la place. On pourrait aussi forcer le système à attendre que toutes les vues soient prêtes avant de répondre à l'utilisateur, comme ça il n'y a plus de décalage.
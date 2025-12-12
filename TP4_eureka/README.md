# TP 4 – Service Registry & Discovery avec Spring Cloud Eureka

## 1. Objectif du TP

L'objectif de ce TP est de :

- Comprendre le principe de Service Registry et Service Discovery
- Mettre en place un Eureka Server
- Enregistrer automatiquement des microservices auprès d'Eureka
- Découvrir dynamiquement les services disponibles
- Gérer plusieurs instances d'un même microservice
- Préparer l'architecture pour le load balancing et l'API Gateway

## 2. Architecture du projet

L'architecture repose sur les composants suivants :

1. **Spring Cloud Config Server** : Configuration centralisée
2. **Eureka Server (Service Registry)** : Annuaire central des services
3. **Microservice Produits** : 2 instances déployées

### Fonctionnement :
1. Eureka Server agit comme un annuaire central
2. Les microservices s'enregistrent automatiquement auprès d'Eureka
3. Eureka maintient la liste des instances disponibles
4. Les clients peuvent découvrir dynamiquement les services sans connaître leurs adresses physiques

## 3. Architecture globale (logique)

```
┌─────────────────┐
│  Config Server  │
│    (port 9101)  │
└────────┬────────┘
         │
┌────────▼────────┐
│  Eureka Server  │
│   (port 9102)   │
└────────┬────────┘
         │
    ┌────▼─────┬─────────┐
    │ Instance │ Instance │
    │  9001    │  9011   │
    └──────────┴─────────┘
    Microservice Produits
```

## 4. Structure des projets

### 4.1 Eureka Server
```
com.mcommerce.eurekaserver
 └── EurekaserverApplication.java

resources
 ├── bootstrap.properties
 └── eureka-server.properties
```

### 4.2 Microservice Produits
```
com.mproduits
 ├── MproduitsApplication.java
 ├── controller
 │   └── ProductController.java
 ├── service
 │   └── ProductService.java
 ├── dao
 │   └── ProductDao.java
 └── model
     └── Product.java

resources
 ├── bootstrap.properties
 └── microservice-produits.properties
```

## 5. Description des fichiers principaux

### EurekaserverApplication.java
- Classe principale du serveur Eureka
- Annotée avec `@EnableEurekaServer`
- Lance le serveur de découverte de services

### bootstrap.properties (Eureka Server)
- Définit le nom de l'application : `eureka-server`
- Indique l'URL du Config Server
- Charge la configuration externe depuis GitHub

### eureka-server.properties
- Définit le port du serveur Eureka : `9102`
- Désactive l'auto-enregistrement d'Eureka sur lui-même
- Paramètre le mode standalone

### MproduitsApplication.java
- Classe principale du microservice Produits
- Annotée avec `@EnableDiscoveryClient`
- Permet l'enregistrement automatique auprès d'Eureka

### microservice-produits.properties
- Configuration H2 pour la base de données
- URL du serveur Eureka pour l'enregistrement
- Paramètres Actuator pour le monitoring
- Configuration personnalisée (`limitDeProduits`)

## 6. Mise en œuvre des instances

### Démarrage des instances du microservice Produits
**Première instance (port 9001) :**
**Deuxième instance (port 9011) :**
**Comportement :**
- Les deux instances s'enregistrent automatiquement auprès d'Eureka
- Chaque instance a un identifiant unique
- Eureka détecte automatiquement les défaillances (heartbeat)

## 7. Tests réalisés

### 7.1 Accès à la console Eureka
```
GET : http://localhost:9102/
```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

**Résultat :**
 Eureka Server est opérationnel  
 Les deux instances du microservice Produits apparaissent  
 Statut `UP` pour les deux instances  
 Informations détaillées sur chaque instance

### 7.2 Vérification via Config Server
```
GET : http://localhost:9101/eureka-server/master
```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>


**Résultat :** Configuration Eureka retournée au format JSON

### 7.3 Vérification du microservice Produits
```
GET : http://localhost:9001/Produits
GET : http://localhost:9011/Produits
```

<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>


**Résultat :** Les deux instances répondent correctement avec les données de produits

### 7.4 Vérification des endpoints Actuator
```
GET : http://localhost:9001/actuator/health
GET : http://localhost:9011/actuator/health
GET : http://localhost:9001/actuator/info
GET : http://localhost:9011/actuator/info
```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>


## 8. Résultat obtenu

 **Eureka Server fonctionne correctement**  
 **Les microservices s'enregistrent automatiquement**  
 **Plusieurs instances d'un même service sont gérées**  
 **Découverte dynamique des services opérationnelle**  
 **Le système est prêt pour le load balancing et l'API Gateway**  
 **Haute disponibilité assurée par les instances multiples**

## 9. Utilité du TP

Ce TP permet de comprendre :

- Le rôle d'un Service Registry dans une architecture microservices
- L'enregistrement et la découverte dynamique des services
- La gestion de plusieurs instances d'un microservice
- L'importance du découplage entre client et services
- Les bases nécessaires pour mettre en place une API Gateway


## 11. Commandes d'exécution

### Ordre de démarrage recommandé :
```bash
# 1. Démarrer le Config Server
mvn spring-boot:run -pl config-server

# 2. Démarrer Eureka Server
mvn spring-boot:run -pl eureka-server

# 3. Démarrer les instances du microservice Produits
# Instance 1 (port 9001)
mvn spring-boot:run -pl microservice-produits -Dserver.port=9001

# Instance 2 (port 9011) - dans un autre terminal
mvn spring-boot:run -pl microservice-produits -Dserver.port=9011
```

### Vérification du fonctionnement :
1. **Accéder à Eureka Dashboard :** `http://localhost:9102`
2. **Vérifier l'inscription des services :** `Application -> MICROSERVICE-PRODUITS`
3. **Compter les instances :** 2 instances avec statut `UP`
4. **Tester chaque instance :**
   - `http://localhost:9001/Produits`
   - `http://localhost:9011/Produits`
5. **Tester la configuration :** `http://localhost:9101/microservice-produits/default`

## 12. Points techniques clés

### @EnableEurekaServer
- Transforme l'application en serveur Eureka
- Fournit l'interface web de gestion
- Gère le registre des services

### @EnableDiscoveryClient
- Active l'enregistrement automatique auprès d'Eureka
- Permet la découverte dynamique des services
- Intègre le client Eureka dans le microservice

### Service Discovery Pattern
- **Registration** : Les services s'enregistrent auprès du registre
- **Discovery** : Les clients découvrent les services disponibles
- **Heartbeat** : Vérification périodique de la disponibilité
- **Load Balancing** : Distribution automatique des requêtes

## 13. Avantages de l'approche Eureka

### Pour les microservices :
- Enregistrement automatique
- Détection de défaillance automatique
- Réplication transparente

### Pour les clients :
- Découverte dynamique des services
- Abstraction des adresses physiques
- Load balancing intégré

### Pour l'infrastructure :
- Centralisation de la gestion des services
- Surveillance globale du système
- Préparation pour la scalabilité

## 14. Prochaines étapes (préparation pour TP 5)

Ce TP prépare l'architecture pour :

1. **API Gateway** : Routage centralisé via la passerelle
2. **Load Balancing** : Distribution automatique des requêtes
3. **Resilience** : Gestion des pannes avec Circuit Breaker
4. **Monitoring** : Supervision centralisée des instances



---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  
**Technologies** : Spring Cloud Eureka, Spring Boot, Service Discovery, Microservices

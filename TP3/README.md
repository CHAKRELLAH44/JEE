# TP3.1 + T3.2
# TP 3.1 – Spring Cloud Config

## Externalisation et centralisation de la configuration des microservices

### 1. Objectif du TP

L'objectif de ce TP est de :

- Comprendre le principe d'externalisation de la configuration
- Mettre en place un Spring Cloud Config Server
- Centraliser les fichiers de configuration dans un repository GitHub
- Connecter un microservice à un serveur de configuration externe
- Modifier le comportement d'un microservice sans recompiler le code

### 2. Architecture du projet

L'architecture repose sur trois composants principaux :

1. **Config Server (Spring Cloud Config)**
   - Sert de serveur centralisé de configuration.

2. **Repository GitHub**
   - Contient les fichiers `.properties` de tous les microservices.

3. **Microservice Produits**
   - Récupère sa configuration dynamiquement depuis le Config Server.

#### Flux de fonctionnement :
1. Le Config Server lit les fichiers depuis GitHub
2. Le microservice contacte le Config Server au démarrage
3. Les paramètres sont chargés depuis l'extérieur de l'application

### 3. Structure des projets

#### 3.1 Microservice Produits
```
com.mproduits
 ├── MproduitsApplication.java
 ├── configurations
 │   └── ApplicationPropertiesConfiguration.java
 ├── dao
 │   └── ProductDao.java
 ├── model
 │   └── Product.java
 ├── web.controller
 │   └── ProductController.java
 └── web.exceptions
     └── ProductNotFoundException.java

resources
 ├── bootstrap.properties
 ├── data.sql
 └── schema.sql
```

#### 3.2 Config Server
```
com.mcommerce.config.server
 └── Application.java

resources
 └── application.properties
```

### 4. Description des fichiers principaux

#### bootstrap.properties
- Remplace `application.properties`
- Contient le nom du microservice
- Définit l'URL du Config Server

#### microservice-produits.properties (dans GitHub)
- Contient les paramètres externalisés
- Exemple : `mes-configs.limitDeProduits=3`
- Utilisé par le Config Server pour alimenter le microservice

#### ApplicationPropertiesConfiguration.java
- Classe de mapping des propriétés
- Utilise `@ConfigurationProperties`
- Permet d'accéder aux valeurs externalisées

#### ProductController.java
- Expose les endpoints REST
- Utilise la valeur externalisée pour limiter le nombre de produits affichés

#### Application.java (Config Server)
- Annotée avec `@EnableConfigServer`
- Active le serveur de configuration centralisé

### 5. Tests réalisés

#### Test du Config Server
```
GET : http://localhost:9101/microservice-produits/default
GET : http://localhost:9101/microservice-produits/default/master
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp3/1.png" width="700"/>
</p>

**Résultat :** Les configurations sont retournées au format JSON

#### Test du microservice Produits
```
GET : http://localhost:9001/Produits
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp3/2.png" width="700"/>
</p>

**Résultat :** Le nombre de produits affichés correspond à la valeur définie dans GitHub

### 6. Résultat obtenu

 **La configuration n'est plus stockée dans le code**  
 **Les paramètres sont centralisés dans GitHub**  
 **Le microservice consomme dynamiquement sa configuration**  
 **Toute modification se fait sans recompilation**

### 7. Utilité du TP

Ce TP permet de comprendre :

- La gestion centralisée des configurations
- Les bonnes pratiques en architecture microservices
- L'intérêt de découpler le code et la configuration
- Le rôle de Spring Cloud Config dans des systèmes distribués

---
--- 
# TP 3.2 – Spring Cloud Actuator

## Monitoring et rechargement dynamique des microservices

### 1. Objectif du TP

L'objectif de ce TP est de :

- Rendre un microservice monitorable
- Utiliser Spring Boot Actuator
- Exposer les endpoints de supervision
- Recharger la configuration à chaud
- Vérifier et personnaliser l'état de santé du microservice

### 2. Architecture du projet

L'architecture est basée sur :

- **Microservice Produits**
- **Spring Cloud Config** (optionnel mais utilisé ici)
- **Spring Boot Actuator** pour le monitoring

Le microservice expose des endpoints techniques pour :
- L'état de santé
- Le rafraîchissement de configuration
- Les métriques internes

### 3. Configuration Actuator

#### Fichier de configuration
```properties
management.endpoints.web.exposure.include=*
```

Cette configuration permet :
- D'exposer tous les endpoints Actuator
- D'accéder aux services de monitoring

### 4. Fonctionnalités mises en œuvre

#### 4.1 Endpoint Actuator
```
GET : http://localhost:9001/actuator
```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p> 

**Résultat :** Affiche la liste complète des endpoints disponibles

#### 4.2 Rafraîchissement dynamique
1. Modification de `mes-configs.limitDeProduits` dans GitHub
2. Envoi d'une requête POST :
   ```
   POST : http://localhost:9001/actuator/refresh
   ```
   <p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>

3. Le microservice recharge la configuration sans redémarrage

#### 4.3 État de santé
```
GET : http://localhost:9001/actuator/health
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp3/3.png" width="700"/>
</p>

- Utilisation de l'interface `HealthIndicator`
- État `UP` ou `DOWN` selon la disponibilité des produits

### 5. Tests réalisés

1. **Vérification du nombre de produits affichés :**
   ```
   GET : http://localhost:9001/Produits
   ```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>

2. **Modification de la configuration :**
   - Modification dans GitHub
   - POST sur `/actuator/refresh`
   - Nouvelle requête sur `/Produits`

3. **Vérification de l'état du service :**
   ```
   GET : http://localhost:9001/actuator/health
   ```
   <p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>


### 6. Résultat obtenu

 **Rechargement dynamique de la configuration**  
 **Supervision complète du microservice**  
 **Indication claire de l'état de santé**  
 **Aucun redémarrage nécessaire**

### 7. Utilité du TP

Ce TP permet de comprendre :

- Le monitoring des microservices
- La supervision applicative
- Le rechargement à chaud des configurations
- L'importance des endpoints techniques en production

## 8. Commandes d'exécution

### Démarrage du Config Server
```bash
cd config-server
mvn spring-boot:run
# Démarre sur le port 9101
```

### Démarrage du microservice Produits
```bash
cd microservice-produits
mvn spring-boot:run
# Démarre sur le port 9001
```

### Vérification du fonctionnement
1. Vérifier que le Config Server fonctionne :
   ```
   http://localhost:9101/microservice-produits/default
   ```


2. Vérifier que le microservice récupère bien sa configuration :
   ```
   http://localhost:9001/Produits
   ```


3. Tester l'Actuator :
   ```
   http://localhost:9001/actuator
   http://localhost:9001/actuator/health
   ```

4. Tester le rafraîchissement dynamique :
   ```bash
   # Modifier le fichier dans GitHub
   curl -X POST http://localhost:9001/actuator/refresh
   ```



## 10. Détails techniques

### Spring Cloud Config
- **@EnableConfigServer** : Active le serveur de configuration
- **spring.cloud.config.server.git.uri** : URL du repo Git
- **bootstrap.properties** : Chargé avant application.properties

### Spring Boot Actuator
- **/actuator** : Liste des endpoints
- **/actuator/health** : État de santé
- **/actuator/refresh** : Recharge la configuration
- **/actuator/info** : Informations sur l'application
- **/actuator/metrics** : Métriques de l'application

---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  

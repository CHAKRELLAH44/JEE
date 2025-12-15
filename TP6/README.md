# TP – Communication entre microservices avec Spring Cloud OpenFeign

## 1. Objectif du TP

L'objectif de ce TP est de :

- Mettre en place une **application distribuée basée sur plusieurs microservices**
- Comprendre la **communication inter-microservices** via HTTP
- Utiliser **Spring Cloud OpenFeign** pour simplifier les appels REST
- Mettre en œuvre les annotations `@EnableFeignClients` et `@FeignClient`
- Orchestrer les appels entre microservices à partir d'un point d'entrée unique
- Construire une application microservices cohérente de bout en bout

## 2. Architecture du projet

L'application **MCommerce** est basée sur **4 microservices distincts** :

1. **Microservice Client (ClientUI)** : Interface utilisateur et orchestration
2. **Microservice Produits** : Gestion des produits
3. **Microservice Commandes** : Gestion des commandes
4. **Microservice Paiement** : Gestion des paiements

Le microservice **Client** joue le rôle de **point d'entrée de l'application** et orchestre les appels vers les autres microservices via OpenFeign.

### Flux global :
- L'utilisateur interagit uniquement avec le microservice Client
- Client appelle les autres microservices via des clients Feign
- Chaque microservice reste indépendant et expose ses propres API REST

## 3. Schéma des échanges entre microservices

```
┌─────────────────┐    1. Liste produits    ┌─────────────────┐
│                 │────────────────────────►│  Microservice    │
│                 │                         │    Produits     │
│                 │◄────────────────────────│   (port 9001)   │
│                 │   2. Détails produit    │                 │
│                 │                         └─────────────────┘
│                 │
│                 │    3. Création commande ┌─────────────────┐
│   Microservice  │────────────────────────►│  Microservice   │
│     Client      │                         │   Commandes     │
│   (port 8080)   │◄────────────────────────│   (port 9002)   │
│                 │   4. Confirmation       │                 │
│                 │                         └─────────────────┘
│                 │
│                 │    5. Traitement paiement┌─────────────────┐
│                 │────────────────────────►│  Microservice   │
│                 │                         │    Paiement     │
│                 │◄────────────────────────│   (port 9003)   │
│                 │   6. Confirmation       │                 │
└─────────────────┘                         └─────────────────┘
```

## 4. Architecture de mise en œuvre

L'architecture technique repose sur :

- **Spring Boot** : Framework principal
- **Spring Cloud OpenFeign** : Communication inter-services
- **REST APIs** : Communication HTTP
- **Base de données H2** : Persistance pour chaque microservice
- **Thymeleaf** : Interface utilisateur du microservice Client

## 5. Structure des microservices

### 5.1 Microservice Client (ClientUI)

```
com.clientui
 ├── ClientUiApplication.java
 ├── controller
 │   └── ClientController.java
 ├── proxies
 │   ├── MicroserviceProduitsProxy.java
 │   ├── MicroserviceCommandeProxy.java
 │   └── MicroservicePaiementProxy.java
 ├── beans
 │   ├── ProductBean.java
 │   ├── CommandeBean.java
 │   └── PaiementBean.java
 └── templates
     ├── Accueil.html
     ├── FicheProduit.html
     ├── Paiement.html
     └── Confirmation.html
```

### 5.2 Microservice Produits
- Port : 9001
- Expose `/Produits` et `/Produits/{id}`
- Gère les informations produits
- Base H2 intégrée

### 5.3 Microservice Commandes
- Port : 9002
- Crée et stocke les commandes
- Expose des endpoints REST
- Base H2 intégrée

### 5.4 Microservice Paiement
- Port : 9003
- Gère les paiements
- Retourne un statut HTTP indiquant le succès ou l'échec
- Base H2 intégrée

## 6. Description des fichiers clés

### ClientUiApplication.java
```java
@SpringBootApplication
@EnableFeignClients("com.clientui.proxies")
public class ClientUiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClientUiApplication.class, args);
    }
}
```
- Classe principale du microservice Client
- Annotée avec `@EnableFeignClients`
- Active la détection des interfaces Feign

### Interfaces Feign (Proxies)

**MicroserviceProduitsProxy.java :**
```java
@FeignClient(name = "microservice-produits", url = "localhost:9001")
public interface MicroserviceProduitsProxy {
    
    @GetMapping(value = "/Produits")
    List<ProductBean> listeDesProduits();
    
    @GetMapping(value = "/Produits/{id}")
    ProductBean recupererUnProduit(@PathVariable("id") int id);
}
```
- Annotée avec `@FeignClient`
- Déclare les méthodes correspondant aux endpoints REST
- OpenFeign génère automatiquement les appels HTTP

### ClientController.java
- Contrôleur MVC principal
- Orchestre tous les appels aux microservices
- Gère les étapes de l'application

## 7. Tests réalisés

### Tests des microservices individuellement

**Microservice Produits :**
```
GET : http://localhost:9001/Produits
GET : http://localhost:9001/Produits/1
Console H2 : http://localhost:9001/h2-console/
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp6/1.png" width="700"/>
</p>
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp6/1-2.png" width="700"/>
</p>


**Microservice Commandes :**
```
Console H2 : http://localhost:9002/h2-console/
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp6/2.png" width="700"/>
</p>

**Microservice Paiement :**
```
Console H2 : http://localhost:9003/h2-console/
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp6/3.png" width="700"/>
</p>


### Test de l'application Client (orchestration)

**Application principale :**
```
GET : http://localhost:8080/
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp6/t.png" width="700"/>
</p>

**Scénarios testés :**
1. Affichage de la liste des produits
2. Consultation des détails d'un produit
3. Passage d'une commande
4. Simulation du paiement
5. Confirmation du paiement
6. Vérification des données dans H2 (commandes et paiements)

## 8. Résultat obtenu

 **Communication inter-microservices fonctionnelle**  
 **Orchestration centralisée via le microservice Client**  
 **Appels REST simplifiés grâce à OpenFeign**  
 **Séparation claire des responsabilités**  
 **Application distribuée cohérente et stable**

## 9. Utilité du TP

Ce TP permet de comprendre :

- Le principe de communication entre microservices
- L'intérêt d'OpenFeign par rapport aux appels HTTP classiques
- L'orchestration des microservices
- La construction d'une application distribuée complète
- Les bonnes pratiques de découplage entre services



## 11. Commandes d'exécution

### Ordre de démarrage :
```bash
# 1. Démarrer Microservice Produits
mvn spring-boot:run -pl microservice-produits

# 2. Démarrer Microservice Commandes
mvn spring-boot:run -pl microservice-commandes

# 3. Démarrer Microservice Paiement
mvn spring-boot:run -pl microservice-paiement

# 4. Démarrer Microservice Client
mvn spring-boot:run -pl client-ui
```

### Vérification du fonctionnement :
1. Accéder à l'application : `http://localhost:8080/`
2. Naviguer à travers les fonctionnalités
3. Vérifier les bases de données via les consoles H2

## 12. Points techniques clés

### Spring Cloud OpenFeign
- **Déclaration déclarative** des clients REST
- **Intégration avec Ribbon** pour le load balancing
- **Gestion automatique** des conversions JSON
- **Configuration centralisée** des timeouts et retry

### Avantages par rapport à RestTemplate
- **Moins de code boilerplate**
- **Découplage accru** entre les services
- **Maintenance simplifiée**
- **Meilleure lisibilité**

### Pattern de communication
- **Client-Side Discovery** : Le client connaît les services
- **Synchronous HTTP** : Communication synchrone
- **API Gateway Pattern** (implémenté par le microservice Client)

## 13. Flux détaillé de l'application

### Étape 1 : Consultation des produits
1. Utilisateur accède à `/`
2. Client appelle `microservice-produits` via Feign
3. Affichage de la liste des produits

### Étape 2 : Détails d'un produit
1. Utilisateur clique sur un produit
2. Client appelle `/Produits/{id}` via Feign
3. Affichage des détails du produit

### Étape 3 : Création de commande
1. Utilisateur passe commande
2. Client envoie la commande à `microservice-commandes`
3. Création de la commande en base

### Étape 4 : Paiement
1. Utilisateur procède au paiement
2. Client envoie la demande à `microservice-paiement`
3. Traitement et confirmation du paiement



## 13. Conclusion

Ce TP démontre comment :

1. **Découper** une application monolithique en microservices
2. **Orchestrer** les communications entre services
3. **Simplifier** le code avec OpenFeign
4. **Maintenir** l'indépendance des services
5. **Construire** une architecture scalable et maintenable

---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  
**Technologies** : Spring Cloud OpenFeign, Spring Boot, Microservices, REST APIs

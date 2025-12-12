# TP 1 – Spring Boot + RESTful API + JAR (CRUD)

## 1. Objectif du TP

L’objectif de ce TP est de :

- Comprendre les bases de Spring Boot
- Développer une API RESTful
- Implémenter les opérations CRUD
- Packager l’application sous forme de JAR exécutable
- Tester les services REST avec Postman
- Comprendre la gestion des profils Spring (dev, prod, integration) et des fichiers de configuration

## 2. Architecture du projet

L’architecture adoptée est une architecture en couches (Layered Architecture) :

- **Controller** : expose les endpoints REST
- **Service** : contient la logique métier
- **Model** : représente les objets métiers
- **Configuration** : gère les profils et paramètres de l’application

Le serveur embarqué Tomcat est utilisé par Spring Boot pour exposer l’API HTTP.

## 3. Structure du projet

```
src/main/java
 └── ma.cigma.rest
     ├── RestApplication.java
     ├── controller
     │   ├── HelloController.java
     │   └── ProductController.java
     ├── service
     │   ├── IProductService.java
     │   └── ProductServiceImpl.java
     └── model
         └── Product.java

src/main/resources
 ├── application.properties
 ├── application-prod.properties
 └── application-integration.properties
```

## 4. Description des fichiers principaux

### RestApplication.java
- Point d’entrée de l’application
- Annotée avec `@SpringBootApplication`
- Lance le serveur Tomcat embarqué

### HelloController.java
- Contrôleur REST simple
- Permet de tester le bon démarrage de l’API
- Retourne un message texte via `/hello`

### Product.java
- Classe modèle
- Représente un produit avec `id` et `name`

### IProductService / ProductServiceImpl
- Couche métier
- Implémente les règles de gestion des produits
- Utilise une liste statique comme source de données

### ProductController.java
- Expose les endpoints REST CRUD
- Gère les requêtes HTTP GET, POST, PUT, DELETE

### application.properties et profiles
- Gestion des ports selon l’environnement
- Activation des profils Spring (dev, prod, integration)

## 5. Tests réalisés

### Tests REST via Postman

#### GET - Récupérer tous les produits
```
GET : http://localhost:8080/products
```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>


#### GET - Récupérer un produit spécifique
```
GET : http://localhost:8080/products/1
```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>


#### POST - Créer un nouveau produit
```
POST : http://localhost:8080/products
Body JSON :
{
  "id": 5,
  "name": "Nouveau produit"
}
```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>

#### PUT - Mettre à jour un produit
```
PUT : http://localhost:8080/products/1
```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>

#### DELETE - Supprimer un produit
```
DELETE : http://localhost:8080/products/5
```
<p align="center">
  <img src="chemin/vers/image.png"  width="700"/>
</p>

### Test du format XML
- Header : `Accept: application/xml`
- Endpoint : `/products`

## 6. Résultat obtenu

 **API REST fonctionnelle**  
 **Données retournées en JSON et XML**  
 **Application exécutable via fichier JAR**  
 **Port configurable selon l'environnement**

## 7. Utilité du TP

Ce TP permet de comprendre comment :

- Créer une API REST professionnelle avec Spring Boot
- Séparer les responsabilités dans une application
- Tester une API indépendamment du client
- Gérer les environnements (dev, prod)
- Packager et déployer une application Java

## 8. Commandes d'exécution

### Compilation et exécution avec Maven
```bash
# Exécution avec le profil dev (par défaut)
mvn spring-boot:run

# Exécution avec le profil prod
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Exécution avec le profil integration
mvn spring-boot:run -Dspring-boot.run.profiles=integration
```

### Création du JAR exécutable
```bash
# Build du projet
mvn clean package

# Exécution du JAR (profil dev)
java -jar target/rest-api-demo.jar

# Exécution du JAR avec profil prod
java -jar -Dspring.profiles.active=prod target/rest-api-demo.jar
```

## 9. Points techniques clés

- **Spring Boot Starter Web** : fournit l'infrastructure web
- **@RestController** : simplifie la création de services REST
- **@GetMapping/@PostMapping/@PutMapping/@DeleteMapping** : annotations pour mapper les méthodes HTTP
- **@RequestBody/@PathVariable** : annotations pour gérer les paramètres de requête
- **application.properties** : centralisation de la configuration
- **@Profile** : activation conditionnelle des beans selon l'environnement

---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  

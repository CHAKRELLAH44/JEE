# TP 2 – WebApp Spring Boot + Microservice REST + H2 + Thymeleaf

## 1. Objectif du TP

L'objectif de ce TP est de :

- Développer un microservice REST
- Développer une application web MVC
- Mettre en place une communication REST entre deux applications
- Utiliser une base de données H2
- Exploiter RestTemplate pour l'appel d'API
- Utiliser Thymeleaf pour l'interface web

## 2. Architecture du projet

Architecture microservices + MVC composée de deux applications :

1. **Microservice EmployeesMngt** (API REST + H2)
2. **WebApp** (MVC + Thymeleaf)

La WebApp consomme l'API REST du microservice via HTTP.

## 3. Architecture globale

```
┌─────────────────┐     HTTP/REST      ┌─────────────────────┐
│   Microservice  │◄──────────────────►│      WebApp         │
│   (port 9000)   │   RestTemplate     │    (port 9001)      │
│   ┌───────────┐ │                    │   ┌──────────────┐  │
│   │   H2 DB   │ │                    │   │  Thymeleaf   │  │
│   └───────────┘ │                    │   │   Templates  │  │
└─────────────────┘                    └─────────────────────┘
```

## 4. Structure du microservice (EmployeesMngt)

```
com.myHR.api_sb
 ├── controller
 │   └── EmployeeController.java
 ├── service
 │   ├── EmployeeService.java
 │   └── EmployeeServiceImpl.java
 ├── repository
 │   └── EmployeeRepository.java
 └── model
     └── Employee.java
```

### Employee.java
- Entité JPA
- Représente un employé
- Mappée à la table `employees`

### EmployeeRepository
- Interface Spring Data JPA
- Fournit automatiquement les méthodes CRUD

### EmployeeService
- Contient la logique métier
- Fait le lien entre controller et repository

### EmployeeController
- Expose l'endpoint REST `/employees`
- Retourne les données en JSON

## 5. Tests du microservice

### Accès à l'API REST
```
GET : http://localhost:9000/employees
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp2/1.png" width="700"/>
</p>

### Console H2
```
URL : http://localhost:9000/h2-console
```
<p align="center">
  <img src="https://raw.githubusercontent.com/CHAKRELLAH44/JEE/master/README_assets/tp2/2.png" width="700"/>
</p>


**Paramètres de connexion H2 :**
- JDBC URL : `jdbc:h2:mem:testdb`
- Username : `sa`
- Password : (vide)

## 6. Structure de la WebApp

```
com.emplyees.webapp
 ├── controller
 │   └── EmployeeController.java
 ├── service
 │   └── EmployeeProxy.java
 ├── model
 │   └── Employee.java
 └── config
     └── RestTemplateConfig.java

src/main/resources
 ├── templates
 │   ├── home.html
 │   ├── formEmployee.html
 │   └── formUpdateEmployee.html
 └── application.properties
```

## 7. Communication WebApp ↔ API

### RestTemplate Configuration
```java
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

### EmployeeProxy
- Classe qui gère la communication avec le microservice
- URL de l'API configurée via `application.properties`
- Injection via `@ConfigurationProperties`

### Configuration properties
```properties
# application.properties de la WebApp
api.url=http://localhost:9000
```

## 8. Tests de la WebApp

### Page principale
```
http://localhost:9001/
```

### Fonctionnalités disponibles
1. **Liste des employés** : Affichage de tous les employés
2. **Ajout d'employé** : Formulaire avec validation
3. **Modification** : Édition des informations d'un employé
4. **Suppression** : Suppression d'un employé

### Règle métier appliquée
- Nom en majuscule automatiquement lors de l'enregistrement

## 9. Résultat obtenu

 **Application web fonctionnelle**  
 **Données partagées via microservice REST**  
 **Interface dynamique avec Thymeleaf**  
 **Séparation claire entre backend et frontend**

## 10. Utilité du TP

Ce TP permet de comprendre :

- Le principe des microservices
- La communication REST entre applications
- L'architecture MVC avec Spring Boot
- L'utilisation de bases In-Memory
- La construction d'une application web complète

## 11. Commandes d'exécution

### Démarrage du microservice
```bash
cd EmployeesMngt
mvn spring-boot:run
# Le service démarre sur le port 9000
```

### Démarrage de la WebApp
```bash
cd WebApp
mvn spring-boot:run
# L'application démarre sur le port 9001
```

### Vérification du fonctionnement
1. Accéder au microservice : `http://localhost:9000/employees`
2. Accéder à la WebApp : `http://localhost:9001/`
3. Tester les opérations CRUD via l'interface web

## 12. Points techniques clés

### Microservice
- **Spring Data JPA** : Persistence avec H2
- **@Entity** : Mapping objet-relationnel
- **@Repository** : Abstraction des opérations CRUD
- **@RestController** : Exposition des endpoints REST

### WebApp
- **Thymeleaf** : Moteur de templates HTML
- **RestTemplate** : Client HTTP pour appels REST
- **@Controller** : Gestion des requêtes HTTP
- **@GetMapping/@PostMapping** : Routing des requêtes
- **@ModelAttribute** : Liaison des données du formulaire



---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  

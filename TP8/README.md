# TP  – Application Microservices complète avec Spring Cloud et Résilience

## 1. Objectif du TP

L'objectif de ce TP est de :

- Mettre en œuvre une architecture Microservices (MSA) complète
- Orchestrer les microservices à l'aide de Spring Cloud
- Centraliser la configuration avec Spring Cloud Config
- Assurer la découverte des services avec Spring Cloud Eureka
- Mettre en place une API Gateway avec Spring Cloud Gateway
- Permettre la communication inter-microservices via Spring Cloud OpenFeign
- Gérer la résilience et les pannes à l'aide de Resilience4J
- Tester dynamiquement la configuration et le comportement en cas de panne

## 2. Rappel de l'architecture MSA

Dans une architecture MSA :
- Chaque microservice est indépendant
- Chaque requête client passe par une API Gateway
- Les services sont enregistrés dynamiquement via Eureka
- Les configurations sont externalisées (GitHub)
- Les services communiquent via REST + OpenFeign
- La résilience est assurée par Circuit Breaker
- La supervision est assurée par Spring Boot Actuator

## 3. Architecture de l'application

L'architecture finale comprend les microservices suivants :

1. **Config Service** : Configuration centralisée
2. **Discovery Service (Eureka Server)** : Service Registry
3. **Gateway Service** : Point d'entrée unique
4. **Customer Service** : Gestion des clients
5. **Account Service** : Gestion des comptes bancaires

### Flux global :
1. Le client envoie les requêtes vers Gateway
2. Gateway consulte Eureka pour localiser les services
3. Gateway redirige vers Customer ou Account Service
4. Account Service communique avec Customer Service via OpenFeign
5. En cas de panne, Resilience4J déclenche un fallback

## 4. Description des microservices

### 4.1 Config Service
- **Rôle** : Centraliser la configuration de tous les microservices
- **Port** : 8888
- **Test** : `http://localhost:8888/application/default`

### 4.2 Discovery Service (Eureka Server)
- **Rôle** : Enregistrer et maintenir la liste des microservices actifs
- **Port** : 8761
- **URL** : `http://localhost:8761`

### 4.3 Gateway Service
- **Rôle** : Point d'entrée unique pour les clients, routage dynamique
- **Port** : 9999
- **Accès via Gateway** :
  ```
  http://localhost:9999/CUSTOMER-SERVICE/customers
  http://localhost:9999/ACCOUNT-SERVICE/api/accounts
  ```
  <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

  **Remarque** : Le nom du service dans l'URL doit être en MAJUSCULE.

### 4.4 Customer Service
- **Rôle** : Gestion des clients, exposition des API CRUD
- **Port** : 8084
- **Endpoints** :
  - `/customers`
  - `/customers/{id}`
  - `/configTest`

### 4.5 Account Service
- **Rôle** : Gestion des comptes bancaires, communication avec Customer Service
- **Port** : 8083
- **Endpoint principal** : `/api/accounts`
- **Résilience** : Protection via Resilience4J Circuit Breaker

## 5. Résilience avec Resilience4J

Dans ce TP :
- OpenFeign est utilisé pour appeler Customer Service
- Resilience4J protège les appels via un Circuit Breaker
- Une méthode fallback est définie

**Scénario testé :**
1. Arrêt volontaire du Customer Service
2. Appel via Gateway vers Account Service
3. Déclenchement automatique du fallback
4. Retour d'un client par défaut

**Cela garantit la continuité du service même en cas de panne.**

## 6. Tests réalisés

### 6.1 Vérification des services dans Eureka
```
GET : http://localhost:8761
```
  <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>
**Résultat** : Tous les services doivent apparaître avec le statut `UP`.

### 6.2 Accès direct aux services
```
GET : http://localhost:8084/customers
GET : http://localhost:8083/api/accounts
```
  <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

### 6.3 Accès via Gateway
```
GET : http://localhost:9999/CUSTOMER-SERVICE/customers
GET : http://localhost:9999/ACCOUNT-SERVICE/api/accounts
```
  <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

### 6.4 Test du Circuit Breaker
1. **Arrêter customer-service**
2. **Appeler** :
   ```
   GET : http://localhost:9999/ACCOUNT-SERVICE/api/accounts/{id}
   ```
     <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

3. **Résultat** :
   - Fallback exécuté
   - Données client par défaut retournées

### 6.5 Test du rafraîchissement de configuration
1. **Modifier** la configuration dans GitHub
2. **Exécuter** :
   ```
   POST : http://localhost:8084/actuator/refresh
   ```
     <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

3. **Vérifier** :
   ```
   GET : http://localhost:8084/configTest
   ```
     <p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>
   **Résultat** : Les nouvelles valeurs sont chargées sans redémarrage.

## 7. Résultat obtenu

 **Architecture MSA complète et fonctionnelle**  
 **Découverte dynamique des services**  
 **Routage centralisé via Gateway**  
 **Communication inter-microservices fiable**  
 **Résilience assurée avec Circuit Breaker**  
 **Configuration dynamique sans redémarrage**

## 8. Utilité pédagogique du TP

Ce TP permet de maîtriser :
- L'architecture Microservices moderne
- L'écosystème Spring Cloud
- La configuration centralisée
- Le Service Discovery
- L'API Gateway
- La résilience et la tolérance aux pannes
- Les bonnes pratiques en environnement distribué

## 9. Architecture détaillée

```
┌─────────────────┐
│     Client      │
└────────┬────────┘
         │
┌────────▼────────┐
│    Gateway      │
│   (port 9999)   │
└────────┬────────┘
         │
┌────────▼────────┐
│   Eureka Server │
│   (port 8761)   │
└────────┬────────┘
         │
    ┌────▼─────┬───────────┐
    │ Customer │  Account  │
    │ Service  │  Service  │
    │ (8084)   │  (8083)   │
    └──────────┴───────────┘
         │           │
         └─────┬─────┘
               │
        ┌──────▼──────┐
        │ Config      │
        │ Server      │
        │ (port 8888) │
        └─────────────┘
```



## 11. Implémentation Resilience4J

### Account Service - CustomerProxy avec Circuit Breaker
```java
@FeignClient(name = "customer-service")
public interface CustomerProxy {
    
    @CircuitBreaker(name = "customerService", fallbackMethod = "getDefaultCustomer")
    @GetMapping("/customers/{id}")
    Customer getCustomerById(@PathVariable Long id);
    
    default Customer getDefaultCustomer(Long id, Exception e) {
        Customer defaultCustomer = new Customer();
        defaultCustomer.setId(id);
        defaultCustomer.setName("Default Customer");
        defaultCustomer.setEmail("default@example.com");
        return defaultCustomer;
    }
}
```

## 12. Commandes d'exécution

### Ordre de démarrage :
```bash
# 1. Démarrer Config Service
mvn spring-boot:run -pl config-service

# 2. Démarrer Eureka Server
mvn spring-boot:run -pl discovery-service

# 3. Démarrer Customer Service
mvn spring-boot:run -pl customer-service

# 4. Démarrer Account Service
mvn spring-boot:run -pl account-service

# 5. Démarrer Gateway Service
mvn spring-boot:run -pl gateway-service
```

### Vérification du fonctionnement :
1. **Vérifier Eureka** : `http://localhost:8761`
2. **Tester Customer Service** : `http://localhost:8084/customers`
3. **Tester Account Service** : `http://localhost:8083/api/accounts`
4. **Tester via Gateway** : `http://localhost:9999/CUSTOMER-SERVICE/customers`
5. **Tester la résilience** : Arrêter Customer Service et appeler Account Service via Gateway

## 13. Points techniques clés

### Spring Cloud Config
- **Externalisation** : Configuration hors du code
- **Centralisation** : Un seul point de gestion
- **Dynamique** : Rafraîchissement sans redémarrage
- **Versioning** : Historique via Git

### Spring Cloud Eureka
- **Service Registry** : Annuaire des services
- **Heartbeat** : Détection automatique des défaillances
- **Load Balancing** : Distribution intelligente
- **Haute Disponibilité** : Réplication possible

### Spring Cloud Gateway
- **Routing dynamique** : Basé sur Eureka
- **Filtres** : Transformation des requêtes/réponses
- **Performances** : Architecture réactive
- **Sécurité** : Point unique de contrôle

### Resilience4J
- **Circuit Breaker** : Protection contre les cascades de pannes
- **Fallback** : Dégradation gracieuse
- **Time Limiter** : Contrôle des timeouts
- **Retry** : Tentatives automatiques



## 16. Dépannage courant

### Problème : Services non visibles dans Eureka
**Solution :**
- Vérifier que Eureka est démarré
- Vérifier les propriétés `eureka.client.service-url.defaultZone`
- Vérifier les logs de démarrage des services

### Problème : Gateway ne route pas les requêtes
**Solution :**
- Vérifier que les noms de service sont en MAJUSCULES
- Vérifier la configuration de Gateway
- Tester l'accès direct aux services

### Problème : Circuit breaker ne se déclenche pas
**Solution :**
- Vérifier la configuration Resilience4J
- S'assurer que le fallback est correctement défini
- Tester avec un timeout artificiel

## 17. Évolution possible

### Ajout de fonctionnalités :
1. **Sécurité** : Spring Security + OAuth2
2. **Tracing** : Sleuth + Zipkin
3. **Monitoring** : Prometheus + Grafana
4. **Logging** : ELK Stack
5. **Containerisation** : Docker + Kubernetes

### Améliorations architecturales :
1. **Event Sourcing** : CQRS avec Axon Framework
2. **Saga Pattern** : Gestion des transactions distribuées
3. **API Versioning** : Gestion des versions d'API
4. **Feature Flags** : Déploiement progressif

---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  
**Technologies** : Spring Cloud, Microservices, Resilience4J, Eureka, Spring Cloud Gateway

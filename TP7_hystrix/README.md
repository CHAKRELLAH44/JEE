# TP  – Résilience des microservices avec Spring Cloud Hystrix

## 1. Objectif du TP

L'objectif de ce TP est de :

- Comprendre la notion de résilience dans une architecture microservices
- Mettre en œuvre le Circuit Breaker Pattern avec Spring Cloud Hystrix
- Gérer les timeouts et les défaillances d'un microservice
- Implémenter un mécanisme de repli (fallback)
- Exploiter le Hystrix Dashboard pour la supervision
- Observer le comportement d'un service en situation de panne

## 2. Contexte et cas d'étude

Ce TP est basé sur le TP 2 (WebApp + API Employees).

**Scénario étudié :**
- Le microservice Employee (backend) devient lent
- Un timeout est volontairement simulé
- Hystrix détecte la défaillance
- Le traitement est redirigé vers une méthode de secours (fallback)

**Objectif :** Garantir la continuité du service malgré la défaillance du backend.

## 3. Architecture du projet

L'architecture repose sur :
- **Microservice Employee** : API REST
- **Base de données H2** : Persistance des données
- **Spring Cloud Hystrix** : Gestion de la résilience
- **Spring Boot Actuator** : Exposition des métriques
- **Hystrix Dashboard** : Visualisation en temps réel

### Flux de fonctionnement :
1. Le client appelle un endpoint REST
2. Hystrix intercepte l'appel
3. En cas de timeout, le circuit est ouvert
4. La méthode fallback est exécutée
5. Le client reçoit une réponse alternative

## 4. Structure du projet

```
com.myHR.api_sb
 ├── ApiSbApplication.java
 ├── controller
 │   └── EmployeeController.java
 ├── service
 │   └── EmployeeService.java
 ├── repository
 │   └── EmployeeRepository.java
 └── model
     └── Employee.java

resources
 ├── application.properties
 ├── data.sql
 └── schema.sql
```

## 5. Description des fichiers principaux

### ApiSbApplication.java
- Classe principale de l'application
- Démarre le microservice Employee
- Initialise le contexte Spring Boot

### EmployeeController.java
- Expose les endpoints REST du microservice
- Annotations importantes :
  ```java
  @EnableCircuitBreaker    // Active Hystrix
  @EnableHystrixDashboard  // Active le dashboard Hystrix
  ```


## 7. Tests réalisés

### Test 2 : Simulation de timeout
```
GET : http://localhost:9000/myMessage
```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

**Résultat attendu :**
- Hystrix interrompt l'exécution après 1 seconde (timeout configuré)
- Le fallback est déclenché automatiquement
- Réponse reçue : `"Fallback: Service unavailable, please try again later"`

### Test 3 : Accès au Hystrix Dashboard
1. **Accéder au dashboard :**
   ```
   GET : http://localhost:9000/hystrix
   ```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

2. **Configurer le stream :**
   ```
   Stream URL : http://localhost:9000/actuator/hystrix.stream
   Title : Employee Service
   ```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

3. **Observer le comportement :**
   - Lancer des requêtes sur `/myMessage`
   - Visualiser les métriques en temps réel
   - Observer l'état du circuit breaker

## 8. Résultat obtenu

 **Le timeout est correctement détecté**  
 **Le circuit breaker fonctionne comme prévu**  
 **Le fallback est exécuté automatiquement**  
 **Le microservice reste disponible malgré la défaillance**  
 **Le comportement est visible dans le dashboard Hystrix**

## 9. Utilité du TP

Ce TP permet de comprendre :

- L'importance de la résilience dans les microservices
- Le fonctionnement du Circuit Breaker Pattern
- La gestion des pannes et des lenteurs réseau
- L'intérêt des mécanismes de fallback
- La supervision des services distribués

## 10. Principe du Circuit Breaker

### Les trois états d'un Circuit Breaker :
1. **CLOSED** : Circuit fermé, les appels passent normalement
2. **OPEN** : Circuit ouvert, les appels sont redirigés vers le fallback
3. **HALF-OPEN** : État intermédiaire pour tester la récupération du service



## 11. Commandes d'exécution

### Démarrage du microservice
```bash
mvn spring-boot:run
# Le service démarre sur le port 9000
```

### Vérification du fonctionnement
1. **Tester le service normal :**
   ```bash
   curl http://localhost:9000/employees
   ```

2. **Tester le timeout :**
   ```bash
   curl http://localhost:9000/myMessage
   ```

3. **Accéder au dashboard :**
   - Ouvrir : `http://localhost:9000/hystrix`
   - Entrer : `http://localhost:9000/actuator/hystrix.stream`
   - Observer les métriques

## 12. Points techniques clés

### Annotations Hystrix
- **`@EnableCircuitBreaker`** : Active Hystrix dans l'application
- **`@EnableHystrixDashboard`** : Active l'interface de monitoring
- **`@HystrixCommand`** : Protège une méthode avec le circuit breaker

### Actuator Endpoints
- **`/actuator/hystrix.stream`** : Flux de données pour le dashboard
- **`/actuator/health`** : État de santé avec informations Hystrix
- **`/actuator/metrics`** : Métriques détaillées

### Dashboard Hystrix
- **Visualisation temps réel** des circuits
- **Statistiques** sur les succès/échecs
- **Monitoring** des performances

## 13. Comportement observé

### En cas de succès :
- Le circuit reste **CLOSED**
- Les requêtes passent normalement
- Le fallback n'est pas appelé

### En cas d'échec répété :
- Le circuit passe en **OPEN**
- Les requêtes sont immédiatement redirigées vers le fallback
- Le service défaillant n'est plus sollicité

### Pendant la période de test :
- Après un délai configuré, le circuit passe en **HALF-OPEN**
- Une requête test est envoyée au service
- Si elle réussit, le circuit revient en **CLOSED**
- Si elle échoue, le circuit reste en **OPEN**

## 14. Avantages de l'approche Hystrix

### Pour l'application :
- **Tolérance aux pannes** améliorée
- **Dégradation gracieuse** du service
- **Meilleure expérience utilisateur**

### Pour l'infrastructure :
- **Protection** contre les cascades de défaillance
- **Isolement** des problèmes
- **Monitoring** centralisé

### Pour le développement :
- **Déclaration simple** via annotations
- **Configuration flexible** des comportements
- **Intégration transparente** avec Spring Boot



**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  
**Technologies** : Spring Cloud Hystrix, Circuit Breaker Pattern, Spring Boot, Microservices Resilience

# TP 5 – API Gateway (Zuul & Spring Cloud Gateway)

## 1. Objectif du TP

L'objectif de ce TP est de :

- Comprendre le rôle d'une API Gateway dans une architecture microservices
- Mettre en place une passerelle d'entrée unique pour les microservices
- Implémenter le Service Discovery via Eureka
- Gérer le routage, le load balancing et l'intermédiation des requêtes
- Comparer deux solutions d'API Gateway : Spring Cloud Zuul et Spring Cloud Gateway

## 2. Architecture du projet

L'architecture globale repose sur les composants suivants :

1. **Spring Cloud Config Server** : Configuration centralisée
2. **Eureka Server (Service Registry)** : Découverte des services
3. **Microservice Produits** : Deux instances exécutées
4. **API Gateway** : Passerelle unique (Zuul et Spring Cloud Gateway)

### Flux général :
1. Le client envoie ses requêtes vers l'API Gateway
2. L'API Gateway interroge Eureka pour localiser les microservices
3. Les requêtes sont redirigées vers une instance disponible du microservice
4. Le load balancing est géré automatiquement

## 3. Technologies mises en œuvre

- **Spring Cloud Eureka** : Service Discovery
- **Spring Cloud Config** : Configuration centralisée
- **Spring Cloud Zuul** : API Gateway traditionnelle
- **Spring Cloud Gateway** : API Gateway moderne
- **Spring Boot** : Framework principal
- **REST API** : Communication entre services
- **Postman** : Tests des endpoints

## 4. Réalisation technique

### 4.1 Implémentation de Zuul

**Mise en place d'un serveur Zuul :**
- Annotation `@EnableZuulProxy`
- Enregistrement de Zuul auprès d'Eureka
- Routage des requêtes vers les microservices
- Gestion du load balancing via Ribbon

**Implémentation de filtres Zuul :**
- Filtre pre pour l'interception des requêtes
- Filtre post pour la modification des réponses HTTP

**Note :** Cette partie a permis de comprendre le fonctionnement historique de Zuul dans Spring Cloud.

### 4.2 Implémentation de Spring Cloud Gateway

**Mise en place d'une API Gateway moderne :**
- Basée sur Spring WebFlux (réactif)
- Configuration des routes dynamiques
- Intégration avec Eureka pour la découverte des services
- Routage automatique vers les instances du microservice Produits
- Load balancing natif

**Importance :** Spring Cloud Gateway est utilisé comme solution principale de test, car il représente la solution recommandée dans les versions récentes de Spring Cloud.

## 5. Précision importante sur les tests

**Approche adoptée :**
- Les deux solutions Zuul et Spring Cloud Gateway ont été implémentées
- Les tests fonctionnels présentés ont été réalisés uniquement avec **Spring Cloud Gateway**
- Zuul a été mis en place à des fins pédagogiques et de comparaison

**Justification :**
Cette approche est conforme aux bonnes pratiques actuelles, Spring Cloud Gateway étant le successeur officiel de Zuul dans l'écosystème Spring Cloud.

## 6. Tests réalisés (Spring Cloud Gateway)

### Accès direct au microservice (sans Gateway)
```
GET : http://localhost:9001/Produits
```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>


### Accès via API Gateway (Spring Cloud Gateway)
```
GET : http://localhost:9004/microservice-produits/Produits
```
<p align="center">
  <img src="chemin/vers/image.png" width="700"/>
</p>

### Résultats obtenus :
 **Les requêtes sont correctement routées**  
 **Les appels successifs sont distribués entre les différentes instances**  
 **Le mécanisme de load balancing fonctionne correctement**  
 **La découverte dynamique via Eureka est opérationnelle**

## 7. Résultat obtenu

 **Mise en place d'une API Gateway fonctionnelle**  
 **Découverte dynamique des services via Eureka**  
 **Routage centralisé des requêtes**  
 **Répartition automatique de la charge**  
 **Séparation claire entre client et microservices**  
 **Comparaison des deux approches Zuul/Gateway**

## 8. Utilité du TP

Ce TP permet de comprendre :

- Le rôle central d'une API Gateway dans une architecture microservices
- Les mécanismes de Service Discovery et de load balancing
- La différence entre Zuul et Spring Cloud Gateway
- Pourquoi Spring Cloud Gateway est aujourd'hui privilégié en production
- Comment sécuriser et centraliser l'accès aux microservices

## 9. Architecture détaillée

```
┌─────────────────┐
│     Client      │
└────────┬────────┘
         │
┌────────▼────────┐
│   API Gateway   │
│  (port 9004)    │
│   ┌─────────┐   │
│   │Routing  │   │
│   │ Engine  │   │
│   └─────────┘   │
└────────┬────────┘
         │
    ┌────▼─────┐
    │  Eureka  │
    │ (port 8761)│
    └────┬─────┘
         │
    ┌────▼─────┬─────────┐
    │ Micro-   │ Micro-  │
    │ service  │ service │
    │ Produits │ Produits│
    │ (port 9001)│ (port 9002)│
    └──────────┴─────────┘
```



## 12. Commandes d'exécution

### Ordre de démarrage :
```bash
# 1. Démarrer le serveur de configuration (si utilisé)
mvn spring-boot:run -pl config-server

# 2. Démarrer Eureka Server
mvn spring-boot:run -pl eureka-server

# 3. Démarrer les instances du microservice Produits
mvn spring-boot:run -pl microservice-produits -Dserver.port=9001
mvn spring-boot:run -pl microservice-produits -Dserver.port=9002

# 4. Démarrer Spring Cloud Gateway
mvn spring-boot:run -pl api-gateway
```

### Vérification du fonctionnement :
1. **Accéder à Eureka :** `http://localhost:8761`
2. **Vérifier les services enregistrés**
3. **Tester l'accès direct :** `http://localhost:9001/Produits`
4. **Tester via Gateway :** `http://localhost:9004/produits/Produits`

## 13. Points techniques clés

### Spring Cloud Gateway
- **Programmation réactive** avec WebFlux
- **Prédicats** pour le routage conditionnel
- **Filtres** pour transformer les requêtes/réponses
- **Load balancing** intégré avec Eureka
- **Haute performance** pour les architectures modernes

### Zuul vs Spring Cloud Gateway
| **Caractéristique** | **Zuul** | **Spring Cloud Gateway** |
|---------------------|----------|--------------------------|
| **Architecture** | Servlet blocking | WebFlux non-blocking |
| **Performance** | Bonne | Excellente |
| **Actif** | Maintenance | Développement actif |
| **Recommandation** | Déprécié | Solution officielle |



## 14. Conclusion

Ce TP a permis de :
- Mettre en place une architecture microservices complète
- Comparer deux générations d'API Gateway
- Comprendre l'importance du Service Discovery
- Maîtriser le routage et le load balancing
- Préparer le terrain pour la sécurité et le monitoring

---

**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  
**Technologies** : Spring Cloud Gateway, Eureka, Spring Boot, Microservices

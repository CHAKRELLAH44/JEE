# TP JMS – Communication asynchrone avec Spring Boot, JMS et ActiveMQ (Artemis)

## Mode Publish / Subscribe

### 1. Objectif du TP

L'objectif de ce TP est de :

- Comprendre le principe de la **communication asynchrone**
- Mettre en œuvre **JMS (Java Message Service)** avec Spring Boot
- Utiliser un **Broker de messages** de type ActiveMQ (Artemis)
- Implémenter le modèle **Publish / Subscribe** avec une **Topic**
- Développer un **Producer JMS**
- Développer un **Consumer JMS asynchrone**
- Observer le comportement du broker via la **console d'administration Artemis**

### 2. Architecture du projet

L'architecture repose sur les composants suivants :

1. **Producer JMS** (Spring Boot Application)
2. **Consumer(s) JMS** (Spring Boot Application)
3. **Broker ActiveMQ Artemis** (externe)
4. **Topic JMS** servant de canal de diffusion

#### Fonctionnement :
- Le Producer publie des messages dans une Topic
- Le Broker stocke et diffuse les messages
- Tous les Consumers abonnés reçoivent **une copie du message**
- Les messages sont persistés tant que les Consumers ne sont pas actifs

### 3. Modèle de données échangé

Les messages JMS transportent des **objets Java sérialisés** :

**Company**
- `name` : String
- `products` : List<Product>

**Product**
- `name` : String

La sérialisation JSON est assurée par **Jackson**.

### 4. Structure des projets

#### 4.1 Projet Producer (SpringActiveMqTopicProducer)

```
com.the.basic.tech.info.activemq
 ├── SpringActiveMqTopicProducerApplication.java
 ├── config
 │   └── ConnectionFactoryConfig.java
 ├── jms
 │   └── MyJmsPublisher.java
 └── models
     ├── Company.java
     └── Product.java

resources
 └── application.properties
```

#### 4.2 Projet Consumer (SpringActiveMqTopicConsumer)

```
com.the.basic.tech.info.activemq
 ├── SpringActiveMqTopicConsumerApplication.java
 ├── config
 │   └── ConnectionFactoryConfig.java
 ├── jms
 │   └── JmsSubscriber.java
 └── models
     ├── Company.java
     └── Product.java

resources
 └── application.properties
```

### 5. Description des fichiers principaux

#### application.properties
```properties
# Configuration ActiveMQ Artemis
spring.artemis.host=localhost
spring.artemis.port=61616
spring.artemis.user=admin
spring.artemis.password=admin

# Configuration JMS
spring.jms.pub-sub-domain=true
jms.topic.name=company.topic
```

#### ConnectionFactoryConfig.java
```java
@Configuration
public class ConnectionFactoryConfig {
    
    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, 
                                   MessageConverter messageConverter) {
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(messageConverter);
        jmsTemplate.setPubSubDomain(true);  // Active le mode Topic
        return jmsTemplate;
    }
    
    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        return converter;
    }
}
```

#### MyJmsPublisher.java (Producer)
```java
@Component
public class MyJmsPublisher {
    
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Value("${jms.topic.name}")
    private String topicName;
    
    public void sendCompany(Company company) {
        jmsTemplate.convertAndSend(topicName, company);
        System.out.println("Message sent to topic: " + company);
    }
}
```

#### JmsSubscriber.java (Consumer)
```java
@Component
public class JmsSubscriber {
    
    @JmsListener(destination = "${jms.topic.name}")
    public void receiveMessage(Company company) {
        System.out.println("Received message from topic: " + company);
        // Traitement du message reçu
    }
}
```

#### Classes Company et Product
```java
public class Company implements Serializable {
    private String name;
    private List<Product> products;
    // Constructeurs, getters, setters
}

public class Product implements Serializable {
    private String name;
    // Constructeurs, getters, setters
}
```

### 6. Tests réalisés

#### 6.1 Démarrage du Broker Artemis
1. **Lancement du broker ActiveMQ Artemis**
2. **Accès à la console d'administration :**
   ```
   URL : http://localhost:8161
   Login : admin
   Password : admin
   ```

#### 6.2 Exécution du Producer
1. **Lancer `SpringActiveMqTopicProducerApplication`**
2. **Le Producer publie plusieurs messages :**
   ```java
   Company company = new Company("TechCorp", Arrays.asList(
       new Product("Laptop"),
       new Product("Smartphone")
   ));
   myJmsPublisher.sendCompany(company);
   ```
3. **Vérification dans la console Artemis :**
   - Les messages sont stockés dans la Topic
   - Les messages restent non consommés si aucun Consumer n'est actif

#### 6.3 Exécution du Consumer
1. **Lancer `SpringActiveMqTopicConsumerApplication`**
2. **Les messages précédemment publiés sont consommés**
3. **Affichage dans la console :**
   ```
   Received message from topic: Company{name='TechCorp', products=[...]}
   ```

#### 6.4 Test multi-consumers
1. **Lancer plusieurs instances du Consumer** (ports différents)
   ```bash
   # Instance 1
   mvn spring-boot:run -Dserver.port=8081
   
   # Instance 2  
   mvn spring-boot:run -Dserver.port=8082
   ```
2. **Relancer le Producer**
3. **Résultat :**
   - Tous les Consumers reçoivent les mêmes messages
   - Comportement conforme au modèle Publish / Subscribe

### 7. Résultat obtenu

✅ **Communication asynchrone fonctionnelle**  
✅ **Publication et consommation via Topic**  
✅ **Persistance des messages dans le broker**  
✅ **Diffusion des messages à tous les abonnés**  
✅ **Visualisation complète dans la console Artemis**

### 8. Utilité du TP

Ce TP permet de comprendre :

- Les architectures événementielles
- Le découplage entre producteurs et consommateurs
- Le fonctionnement des brokers de messages
- La différence entre Queue et Topic
- L'intérêt du Publish / Subscribe dans les systèmes distribués

## 9. Architecture détaillée

```
┌─────────────────┐    Publie    ┌─────────────────┐
│                 │──────────────►│                 │
│   Producer      │              │    Broker       │
│  (Spring Boot)  │              │  ActiveMQ       │
│                 │◄──────────────│   Artemis      │
└─────────────────┘    Acknowledge └────────┬────────┘
                                            │
                                            │  Diffuse
                                      ┌─────▼─────┬────────┐
                                      │ Consumer 1│Consumer│
                                      │(Spring Boot│   2   │
                                      └───────────┴────────┘
```

## 10. Configuration ActiveMQ Artemis

### Installation et démarrage :
```bash
# Télécharger ActiveMQ Artemis
wget https://archive.apache.org/dist/activemq/activemq-artemis/2.26.0/apache-artemis-2.26.0-bin.zip

# Décompresser
unzip apache-artemis-2.26.0-bin.zip

# Créer une instance
./apache-artemis-2.26.0/bin/artemis create mybroker --user admin --password admin --allow-anonymous

# Démarrer le broker
./mybroker/bin/artemis run
```

### Ports utilisés :
- **Console Web** : 8161
- **Port JMS** : 61616
- **Port AMQP** : 5672
- **Port MQTT** : 1883

## 11. Différence Queue vs Topic

### Queue (Point-to-Point) :
- **Un seul consommateur** reçoit le message
- Message supprimé après consommation
- Utilisé pour le traitement distribué

### Topic (Publish/Subscribe) :
- **Tous les consommateurs** reçoivent le message
- Message diffusé à tous les abonnés
- Utilisé pour la diffusion d'événements

## 12. Commandes d'exécution

### Ordre de démarrage :
```bash
# 1. Démarrer ActiveMQ Artemis
cd mybroker
./bin/artemis run

# 2. Démarrer le Producer
cd SpringActiveMqTopicProducer
mvn spring-boot:run

# 3. Démarrer un ou plusieurs Consumers
cd SpringActiveMqTopicConsumer
mvn spring-boot:run -Dserver.port=8081
# Dans un autre terminal :
mvn spring-boot:run -Dserver.port=8082
```

### Vérification :
1. **Console Artemis** : `http://localhost:8161`
2. **Vérifier les topics** : Onglet "Topics"
3. **Observer les messages** : Entrants et sortants

## 13. Points techniques clés

### JMS avec Spring Boot
- **`JmsTemplate`** : Template simplifiant l'envoi de messages
- **`@JmsListener`** : Annotation pour consommer des messages
- **`MessageConverter`** : Conversion automatique Java ↔ JSON

### Configuration Spring
```java
@Configuration
@EnableJms  // Active la prise en charge JMS
public class JmsConfig {
    // Configuration JMS
}
```

### Gestion des erreurs
```java
@JmsListener(destination = "company.topic", 
             containerFactory = "jmsListenerContainerFactory")
public void receiveMessage(Company company) {
    try {
        // Traitement
    } catch (Exception e) {
        // Gestion des erreurs
    }
}
```

## 14. Avantages du modèle Pub/Sub

### Découplage temporel :
- Producteur et consommateur ne communiquent pas directement
- Pas besoin d'être actifs simultanément

### Scalabilité :
- Ajout de consommateurs sans modifier le producteur
- Distribution de charge

### Résilience :
- Messages persistés en cas d'indisponibilité
- Relecture possible des messages

## 15. Cas d'utilisation typiques

### 1. Notification d'événements
- Nouvelle commande passée
- Mise à jour de stock
- Modification de profil utilisateur

### 2. Intégration système
- Synchronisation entre microservices
- Réplication de données
- Logging distribué

### 3. Traitement parallèle
- Calcul distribué
- Génération de rapports
- Envoi d'emails en batch

## 16. Bonnes pratiques

### Configuration des messages :
```java
// Définir un TTL (Time To Live)
jmsTemplate.convertAndSend(destination, message, messagePostProcessor -> {
    messagePostProcessor.setJMSExpiration(60000); // 60 secondes
    return messagePostProcessor;
});
```

### Gestion des abonnements durables :
```java
@JmsListener(destination = "company.topic", 
             subscription = "myDurableSubscription",
             containerFactory = "jmsTopicListenerContainerFactory")
public void receiveDurableMessage(Company company) {
    // Consumer durable
}
```

### Monitoring et métriques :
```properties
# Activer les métriques JMS
management.metrics.enable.jms=true

# Journalisation détaillée
logging.level.org.springframework.jms=DEBUG
logging.level.org.apache.activemq=INFO
```



**Auteurs** : IMANE CHAKRELLAH & YASSINE ECH-CHAOUI  
**Classe** : 5IIR-11  
**Technologies** : Spring Boot, JMS, ActiveMQ Artemis, Publish/Subscribe, Messaging Asynchrone

package ma.formations.jms;





import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;



public class JmsQueueProducer {
    private final String destinationName;
    private final String brockerUrl;



    public JmsQueueProducer(String destinationName, String brockerUrl) {
        this.destinationName = destinationName;
        this.brockerUrl = brockerUrl;
    }



    public void send(String message) throws JMSException {
        // Créer une connexion à ActiveMQ
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brockerUrl);
        Connection connection = connectionFactory.createConnection();
        connection.start();



        // Créer une session et une queue
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(destinationName);



        // Créer un producteur pour envoyer un message
        MessageProducer producer = session.createProducer(destination);



        // Créer un message et l'envoyer
        TextMessage messageObject = session.createTextMessage(message);
        producer.send(messageObject);
        System.out.println("Message envoyé : " + messageObject.getText());
        // Fermer la connexion
        producer.close();
        session.close();
        connection.close();
    }
}

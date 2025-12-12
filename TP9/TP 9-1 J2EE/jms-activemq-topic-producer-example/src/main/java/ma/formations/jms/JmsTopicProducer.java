package ma.formations.jms;





import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;



public class JmsTopicProducer {
    private final String destinationName;
    private final String brockerUrl;



    public JmsTopicProducer(String destinationName, String brockerUrl) {
        this.destinationName = destinationName;
        this.brockerUrl = brockerUrl;
    }



    public void send(Article article) throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brockerUrl);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(destinationName);
        MessageProducer producer = session.createProducer(destination);
        ObjectMessage messageObject = session.createObjectMessage(article);
        producer.send(messageObject);
        System.out.println("Message envoyé : " + messageObject.getObject());
        producer.close();
        session.close();
        connection.close();
    }
}
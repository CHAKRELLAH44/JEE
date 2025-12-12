package ma.formations.jms;
import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import java.io.IOException;



public class JmsTopicConsumer {
    private final String destinationName;
    private final String brockerUrl;



    public JmsTopicConsumer(String destinationName, String brockerUrl) {
        this.destinationName = destinationName;
        this.brockerUrl = brockerUrl;
    }
    public void receive() throws JMSException, IOException {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brockerUrl);
        Connection connection = connectionFactory.createConnection();
        connection.setClientID("142");
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic destination = session.createTopic(destinationName);
        TopicSubscriber subscriber = session.createDurableSubscriber(destination, "subscriber1");
        TopicSubscriber subscriber2 = session.createDurableSubscriber(destination, "subscriber2");
        subscriber.setMessageListener(msg -> {
            if (msg instanceof ObjectMessage objectMessage) {
                try {
                    Article article = (Article) objectMessage.getObject();
                    System.out.println("Message reçu par subscriber 1 : " + article);



                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }
            }
        });



        subscriber2.setMessageListener(msg -> {
            if (msg instanceof ObjectMessage objectMessage) {
                try {
                    Article article = (Article) objectMessage.getObject();
                    System.out.println("Message reçu par subscriber 2 : " + article);
                } catch (JMSException e) {
                    throw new RuntimeException(e);
                }



            }
        });
        // Le programme restera en écoute, tant que la connexion est ouverte.
        System.in.read();



        subscriber.close();
        subscriber2.close();
        session.close();
        connection.close();
    }
}
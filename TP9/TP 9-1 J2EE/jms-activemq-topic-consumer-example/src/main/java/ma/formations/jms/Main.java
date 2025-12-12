package ma.formations.jms;
import jakarta.jms.JMSException;
import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        JmsTopicConsumer consumer = new JmsTopicConsumer(IConstants.TOPIC_NAME, IConstants.BROCKER_URL);
        try {
            consumer.receive();
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }}}
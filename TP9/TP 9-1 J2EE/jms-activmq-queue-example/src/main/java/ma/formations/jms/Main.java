package ma.formations.jms;



import jakarta.jms.JMSException;



public class Main {
    public static void main(String[] args) {
        String brockerUrl = "tcp://localhost:61616";
        String destinationName = "queue1";
        JmsQueueProducer producer = new JmsQueueProducer(destinationName, brockerUrl);
        JmsQueueConsumer consumer = new JmsQueueConsumer(destinationName, brockerUrl);
        try {
            producer.send("premier msg");
            consumer.receive();
            producer.send("deuxième msg");
            consumer.receive();
            producer.send("....");
            consumer.receive();
            producer.send("other message");



        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
package ma.formations.jms;



import jakarta.jms.JMSException;



public class Main {
    public static void main(String[] args) {



        JmsTopicProducer producer = new JmsTopicProducer(IConstants.TOPIC_NAME, IConstants.BROCKER_URL);
        try {
            producer.send(Article.builder().id(1L).description("ARTICLE_1").price(1522.0).build());
            producer.send(Article.builder().id(2L).description("ARTICLE_2").price(2100.0).build());
            producer.send(Article.builder().id(3L).description("ARTICLE_3").price(15000.0).build());
            producer.send(Article.builder().id(4L).description("ARTICLE_4").price(900.0).build());





        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
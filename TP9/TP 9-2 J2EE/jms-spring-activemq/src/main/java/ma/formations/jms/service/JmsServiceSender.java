package ma.formations.jms.service;

import ma.formations.jms.model.Employee;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsServiceSender {
    private final JmsTemplate jmsTemplate;

    public JmsServiceSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendMessage(String destination, Employee employee) {
        jmsTemplate.convertAndSend(destination, employee);
    }
}

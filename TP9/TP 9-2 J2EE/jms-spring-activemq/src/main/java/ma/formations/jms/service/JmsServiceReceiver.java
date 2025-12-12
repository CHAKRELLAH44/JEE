package ma.formations.jms.service;

import ma.formations.jms.model.Employee;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class JmsServiceReceiver {

    private final Sinks.Many<Employee> messageSink;

    public JmsServiceReceiver() {
        this.messageSink = Sinks.many().multicast().onBackpressureBuffer();
    }

    @JmsListener(destination = "test-queue")
    public void onMessage(Employee employee) {
        messageSink.tryEmitNext(employee);
    }

    public Flux<Employee> getMessages() {
        return messageSink.asFlux();
    }
}

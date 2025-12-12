package ma.formations.msa.customer_service;



import ma.formations.msa.customer_service.entities.Customer;
import ma.formations.msa.customer_service.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;



@SpringBootApplication
public class CustomerServiceApplication {



    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }



    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository){
        return args -> {
            customerRepository.save(Customer.builder()
                    .firstName("Mohammadi")
                    .lastName("Imane")
                    .email("imane@gmail.com")
                    .build());
            customerRepository.save(Customer.builder()
                    .firstName("Ismaili")
                    .lastName("Aymane")
                    .email("aymane@gmail.com")
                    .build());
        };
    }



}
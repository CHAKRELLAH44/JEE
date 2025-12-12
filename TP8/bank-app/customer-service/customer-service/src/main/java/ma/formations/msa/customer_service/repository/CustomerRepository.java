package ma.formations.msa.customer_service.repository;



import ma.formations.msa.customer_service.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;



public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
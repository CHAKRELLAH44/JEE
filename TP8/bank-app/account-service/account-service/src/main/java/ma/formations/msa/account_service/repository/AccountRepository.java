package ma.formations.msa.account_service.repository;



import ma.formations.msa.account_service.entities.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;



public interface AccountRepository extends JpaRepository<BankAccount,String> {
}
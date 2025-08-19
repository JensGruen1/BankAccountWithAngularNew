package BankingApp.repository;

import BankingApp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
Account findAccountByAccountNumber(String accountNumber);
}

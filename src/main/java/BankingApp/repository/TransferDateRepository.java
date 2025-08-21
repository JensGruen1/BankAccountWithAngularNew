package BankingApp.repository;

import BankingApp.entity.TransferDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferDateRepository extends JpaRepository<TransferDate, Integer> {
}

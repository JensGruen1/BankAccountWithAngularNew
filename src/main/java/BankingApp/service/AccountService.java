package BankingApp.service;


import BankingApp.entity.Account;
import BankingApp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {


    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public void saveAccountToDatabase (Account account) {
accountRepository.save(account);
}

}

package BankingApp.service;


import BankingApp.entity.Account;
import BankingApp.entity.User;
import BankingApp.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AccountService {


    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    public void saveAccountToDatabase (Account account) {
accountRepository.save(account);
}

public List<Account> getAllAccountsFromUser (User user) {
        return user.getAccounts();
}

public Account getAccountByAccountNumber (String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
}

public void updateAccount (Account account) { accountRepository.save(account);}


}


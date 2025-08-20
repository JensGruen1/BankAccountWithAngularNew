package BankingApp.controller;


import BankingApp.dto.AccountRequest;
import BankingApp.dto.DepositRequest;
import BankingApp.dto.WithdrawRequest;
import BankingApp.entity.Account;
import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import BankingApp.service.AccountService;
import BankingApp.service.UserService;
import BankingApp.util.AccountNumberGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200") // allow Angular-Port
@RequestMapping("/api/users")
public class AccountController {


    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService, UserService userService) {

        this.accountService = accountService;
        this.userService = userService;
    }


        @PostMapping("/account/createAccount")
        public ResponseEntity<?>  newAccount(@RequestBody AccountRequest accountRequest) {
            Account account = new Account();
            account.setAccountNumber( AccountNumberGenerator.generateAccountNumber());
            account.setBalance(accountRequest.getBalance());
            account.setUser(userService.getLoggedInUser());
            account.setAccountType(accountRequest.getAccountType());
            accountService.saveAccountToDatabase(account);
            System.out.println(userService.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("Account created successfully");
        }

        @GetMapping("/showAccounts")
        public ResponseEntity<?> getAccounts() {
            User user = userService.getLoggedInUser();
            List<Account> accounts = user.getAccounts();
        return ResponseEntity.ok(accounts);

        }

        @PostMapping("/deposit")
        public ResponseEntity<?> deposit(@RequestBody DepositRequest depositRequest) {
        Account account = accountService.getAccountByAccountNumber(depositRequest.getAccountNumber());
           double balance = account.getBalance() + depositRequest.getAmount();
           account.setBalance(balance);
           accountService.updateAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body("Deposit money successfully");
        }

        @PostMapping("/withdraw")
        public ResponseEntity<?> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
           Account account = accountService.getAccountByAccountNumber(withdrawRequest.getAccountNumber());
           double balance = account.getBalance() - withdrawRequest.getAmount();
           account.setBalance(balance);
           accountService.updateAccount(account);
        return ResponseEntity.status(HttpStatus.OK).body("withdraw money successfully");
        }


}

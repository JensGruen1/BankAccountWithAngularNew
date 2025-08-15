package BankingApp.controller;


import BankingApp.dto.AccountRequest;
import BankingApp.entity.Account;
import BankingApp.repository.UserRepository;
import BankingApp.service.AccountService;
import BankingApp.service.UserService;
import BankingApp.util.AccountNumberGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
            System.out.println("im here");
            Account account = new Account();
            account.setAccountNumber( AccountNumberGenerator.generateAccountNumber());
            account.setBalance(accountRequest.getBalance());
            account.setUser(userService.getLoggedInUser());
            account.setAccountType(accountRequest.getAccountType());
            accountService.saveAccountToDatabase(account);
            System.out.println(userService.getLoggedInUser());
            return ResponseEntity.status(HttpStatus.OK).body("Account created successfully");
        }


}

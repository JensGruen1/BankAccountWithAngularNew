package BankingApp.controller;


import BankingApp.dto.AccountRequest;
import BankingApp.dto.DepositRequest;
import BankingApp.dto.TransferRequest;
import BankingApp.dto.WithdrawRequest;
import BankingApp.entity.Account;
import BankingApp.entity.Transfer;
import BankingApp.entity.User;
import BankingApp.repository.UserRepository;
import BankingApp.service.AccountService;
import BankingApp.service.UserService;
import BankingApp.util.AccountNumberGenerator;
import BankingApp.util.DepositTransactionException;
import BankingApp.util.WithdrawalTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

        @GetMapping("/listOfAccountNumbers")
        public ResponseEntity<?> getListOfAccountNumbers() {
            User user = userService.getLoggedInUser();
            List<String> listOfAccountNumbers = new ArrayList<>();
            List<Account> accounts = user.getAccounts();

            if(accounts != null) {
                for (Account account : accounts) {
                    String accountNumber = account.getAccountNumber();
                    listOfAccountNumbers.add(accountNumber);
                }
                return ResponseEntity.ok(listOfAccountNumbers);
            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No accounts found");
            }
        }

        @GetMapping("/getAccountDetails/{accountNumber}")
        public ResponseEntity<?> getAccountDetails(@PathVariable String accountNumber) {

             if (accountService.getAccountByAccountNumber(accountNumber) != null) {
                 Account account = accountService.getAccountByAccountNumber(accountNumber);
                 return ResponseEntity.ok(account);
             } else  {
                 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account does not exist");
             }

        }

        @GetMapping("/getDateTransferMap/{accountNumber}")
        public ResponseEntity<?> getDateTransferMap(@PathVariable String accountNumber) {
            Map<String,List<Transfer>> transfersForEachDate = null;

            if (accountService.getAccountByAccountNumber(accountNumber) != null) {
                transfersForEachDate = accountService.createMapOfDatesAndListsOfTransfers(accountNumber);

                return ResponseEntity.ok(transfersForEachDate);
            } else  {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account does not exist");
            }
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


//    @GetMapping("/transactions/transfer")
//    public String getTransferMoney (Model model, @RequestParam(required = false) String accountNumber) {
//
//        List<String> listAccountNumbers = userService.getListOfAccountNumbersFromAccountsFromLoggedInUser();
//        Account account = accountService.getAccountByAccountNumber(accountNumber);
//        model.addAttribute("users", listAccountNumbers);
//
//        return "transfer";}



    @PostMapping("/transfer")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest transferRequest) {
        try {
            accountService.transferAccount(transferRequest.getAccountNumber(), transferRequest.getTransferredMoney(), transferRequest.getAccountNumberReceiver());

        } catch (DepositTransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Deposit to account failed: " + e.getMessage());
        } catch (WithdrawalTransactionException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Withdrawal from account failed: " + e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Transfer successful");
    }




}

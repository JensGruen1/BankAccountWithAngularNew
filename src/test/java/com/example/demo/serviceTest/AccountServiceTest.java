package com.example.demo.serviceTest;

import BankingApp.controller.AccountController;
import BankingApp.entity.Account;
import BankingApp.entity.Transfer;
import BankingApp.entity.TransferDate;
import BankingApp.entity.User;
import BankingApp.repository.AccountRepository;
import BankingApp.repository.TransferDateRepository;
import BankingApp.repository.TransferRepository;
import BankingApp.service.AccountService;
import BankingApp.util.DepositTransactionException;
import BankingApp.util.SessionProvider;
import BankingApp.util.WithdrawalTransactionException;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import BankingApp.util.AccountNumberGenerator;
import org.mockito.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

@Mock
AccountRepository accountRepository;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private TransferDateRepository transferDateRepository;

    @Mock
    private Session session;

    @Mock
    private Transaction transaction;

    @Mock
    private SessionProvider sessionProvider;

@InjectMocks
AccountService accountService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        when(sessionProvider.getSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
    }



@Test
void transferAccount_savesTransferAsExpected () {
    //given
    User userSource = new User();
    userSource.setUsername("userSource");
    User userTarget = new User();
    userTarget.setUsername("userTarget");

    Account source = new Account();
    source.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
    source.setBalance(400.00);
    source.setUser(userSource);
    source.setTransfers(new ArrayList<>());

    Account target = new Account();
    target.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
    target.setBalance(100.00);
    target.setUser(userTarget);
    target.setTransfers(new ArrayList<>());

    when(accountRepository.findAccountByAccountNumber(source.getAccountNumber())).thenReturn(source);
    when(accountRepository.findAccountByAccountNumber(target.getAccountNumber())).thenReturn(target);

    ArgumentCaptor<Transfer> transferCaptor = ArgumentCaptor.forClass(Transfer.class);
    ArgumentCaptor<TransferDate> transferDateCaptor = ArgumentCaptor.forClass(TransferDate.class);
    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);



    //when
    accountService.transferAccount(source.getAccountNumber(), 50.00,target.getAccountNumber());


    //then
    assertEquals(350.00, source.getBalance());
    assertEquals(150.00,target.getBalance());

    //there are 2 transfers, one for source, one for target
    verify(transferRepository, times(2)).save(transferCaptor.capture());
    List<Transfer> savedTransfers = transferCaptor.getAllValues();
    assertEquals("-50.0", savedTransfers.getFirst().getTransferredMoney());
    assertEquals("userTarget", savedTransfers.getFirst().getAccountInfo());
    assertEquals(userSource, savedTransfers.getFirst().getAccount().getUser());

    assertEquals("+50.0", savedTransfers.getLast().getTransferredMoney());
    assertEquals("userSource",savedTransfers.getLast().getAccountInfo() );
    assertEquals(userTarget, savedTransfers.getLast().getAccount().getUser());

    verify(transferDateRepository, times(2)).save(transferDateCaptor.capture());
    List<TransferDate> savedTransferDates = transferDateCaptor.getAllValues();
    String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MMMM"));
    assertEquals(expectedDate, savedTransferDates.getFirst().getTransferDate());
    assertEquals(expectedDate, savedTransferDates.getLast().getTransferDate());


    //Check if TransferList is attached to Account
    verify(accountRepository, atLeast(2)).save(accountCaptor.capture());
    List<Account> updatedAccounts = accountCaptor.getAllValues();
   assertThat(source.getTransfers()).contains(savedTransfers.getFirst());
    assertThat(target.getTransfers()).contains(savedTransfers.getLast());
    verify(transaction,times(2)).commit();
    verify(session,times(2)).close();



}

@Test
void transferAccount_shouldThrowWithdrawalException_whenSourceAccountIsNull () {
    User userSource = new User();
    userSource.setUsername("userSource");
    User userTarget = new User();
    userTarget.setUsername("userTarget");

    Account source = new Account();
    source.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
    source.setBalance(400.00);
    source.setUser(userSource);
    source.setTransfers(new ArrayList<>());

    Account target = new Account();
    target.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
    target.setBalance(100.00);
    target.setUser(userTarget);
    target.setTransfers(new ArrayList<>());
    when(accountRepository.findAccountByAccountNumber(source.getAccountNumber())).thenReturn(null);


    assertThrows(WithdrawalTransactionException.class, () ->
            accountService.transferAccount(source.getAccountNumber(), 50.0, target.getAccountNumber())
    );

    verify(transaction).rollback();
    verify(session).close();

}

    @Test
    void transferAccount_shouldThrowDepositException_whenTargetAccountIsNull () {
        User userSource = new User();
        userSource.setUsername("userSource");
        User userTarget = new User();
        userTarget.setUsername("userTarget");

        Account source = new Account();
        source.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        source.setBalance(400.00);
        source.setUser(userSource);
        source.setTransfers(new ArrayList<>());

        Account target = new Account();
        target.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        target.setBalance(100.00);
        target.setUser(userTarget);
        target.setTransfers(new ArrayList<>());
        when(accountRepository.findAccountByAccountNumber(source.getAccountNumber())).thenReturn(source);
        when(accountRepository.findAccountByAccountNumber(target.getAccountNumber())).thenReturn(null);


        assertThrows(DepositTransactionException.class, () ->
                accountService.transferAccount(source.getAccountNumber(), 50.0, target.getAccountNumber())
        );


        verify(transaction).rollback();
        verify(session).close();

    }



@Test
void saveAccountToDatabase_shouldSaveAccountToDb () {
        //given
    User user = new User();
    user.setUsername("testUser");
    Account account = new Account();
    account.setUser(user);
    account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
    account.setBalance(100.00);
    ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);

    //when
    accountService.saveAccountToDatabase(account);
    //then
    verify(accountRepository,times(1)).save(accountArgumentCaptor.capture());
    assertEquals(account, accountArgumentCaptor.getValue());

}

    @Test
    void updateAccountToDatabase_shouldSaveAccountToDb () {
        //given
        User user = new User();
        user.setUsername("testUser");
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setBalance(100.00);
        ArgumentCaptor<Account> accountArgumentCaptor = ArgumentCaptor.forClass(Account.class);

        //when
        accountService.updateAccount(account);
        //then
        verify(accountRepository,times(1)).save(accountArgumentCaptor.capture());
        assertEquals(account, accountArgumentCaptor.getValue());

    }

    @Test
    void getAccountByAccountNumber_shouldReturnCorrectAccount () {
        User user = new User();
        user.setUsername("testUser");
        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setBalance(100.00);
        ArgumentCaptor<String> accountArgumentCaptor = ArgumentCaptor.forClass(String.class);

        //when
        accountService.getAccountByAccountNumber(account.getAccountNumber());

        //then
        verify(accountRepository, times(1)).findAccountByAccountNumber(accountArgumentCaptor.capture());
        assertEquals(account.getAccountNumber(), accountArgumentCaptor.getValue());
    }




    public Account getAccountByAccountNumber (String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber);
    }









//@Test
//void withdrawAccount_shouldSubtractMoneyFromAccount_whenSuccessful () {
//    //given
//
//    //when
//    //then
//
//
//}
//
//
//@Test
//void createAndSaveNewTransfer () {
//    //given
//    Account account = new Account();
//    account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
//    account.setBalance(400.00);
//
//    Account receiver = new Account();
//    receiver.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
//    receiver.setBalance(100.00);
//
//    Transfer transfer = accountService.transferAccount();
//}




}

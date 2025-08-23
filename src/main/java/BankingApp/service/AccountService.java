package BankingApp.service;


import BankingApp.entity.Account;
import BankingApp.entity.Transfer;
import BankingApp.entity.TransferDate;
import BankingApp.entity.User;
import BankingApp.repository.AccountRepository;
import BankingApp.repository.TransferDateRepository;
import BankingApp.repository.TransferRepository;
import BankingApp.util.DepositTransactionException;
import BankingApp.util.WithdrawalTransactionException;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final TransferAndTransferDateService transferAndTransferDateService;
    private final TransferRepository transferRepository;
    private final TransferDateRepository transferDateRepository;

    public AccountService(AccountRepository accountRepository, EntityManagerFactory entityManagerFactory, TransferAndTransferDateService transferAndTransferDateService, TransferRepository transferRepository, TransferDateRepository transferDateRepository) {
        this.accountRepository = accountRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.transferAndTransferDateService = transferAndTransferDateService;
        this.transferRepository = transferRepository;
        this.transferDateRepository = transferDateRepository;
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



    public void depositAccount (String accountNumber, double depositMoney, String transferAccountNumber) throws DepositTransactionException {
        if (accountRepository.findAccountByAccountNumber(accountNumber) != null) {
            Account account = accountRepository.findAccountByAccountNumber(accountNumber);
            Account transferAccount = accountRepository.findAccountByAccountNumber(transferAccountNumber);

            Session session = getSession();
            Transaction tx = session.beginTransaction();

            try {
                account.setBalance(account.getBalance() + depositMoney);

            //Transfer transfer = transferAndTransferDateService.createAndSaveNewTransfer(accountNumber, transferAccountNumber, "-"+ depositMoney,
                   //account, transferAccount, "Deposit Money");

            //updateTransferListForAccount(account, transfer);
               // accountRepository.save(account);

                TransferDate transferDateNew = transferAndTransferDateService.addNewTransferDateIfNotExist();

                Transfer transfer = new Transfer(transferAccountNumber, "+" + depositMoney,
                        transferDateNew, account);
                transfer.setAccountInfo(transferAccount.getUser().getUsername());

                transferDateRepository.save(transferDateNew);
                transferRepository.save(transfer);




                List<Transfer> transferList = account.getTransfers();
                transferList.add(transfer);
                account.setTransfers(transferList);
                accountRepository.save(accountRepository.findAccountByAccountNumber(accountNumber));

                tx.commit();
            } catch (Exception e) {
                if (tx != null && account == null) {
                    System.out.println("rolling back deposit");
                    tx.rollback();
                    Throwable throwable = new Throwable();
                    throw new DepositTransactionException("deposit failed", throwable);
                }
            } finally {
                session.close();
            }
        } else {
            Throwable throwable = new Throwable();
            throw  new DepositTransactionException("Account does not exist", throwable);
        }

    }


    public void withdrawAccount (String accountNumber, Double withdrawMoney, String transferAccountNumber) throws WithdrawalTransactionException {

        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        Account transferAccount = accountRepository.findAccountByAccountNumber(transferAccountNumber);

        Session session = getSession();
        Transaction tx = session.beginTransaction();

        try { account.setBalance(account.getBalance() - withdrawMoney);

            Transfer transfer = transferAndTransferDateService.
                    createAndSaveNewTransfer(accountNumber, transferAccountNumber, "-"+ withdrawMoney,
                            account, transferAccount, "Withdraw Money");
           updateTransferListForAccount(account, transfer);

            tx.commit();
        } catch (Exception e) {
            if (tx != null && account==null) {
                System.out.println("rolling back deposit");
                tx.rollback();
                Throwable throwable = new Throwable();
                throw new DepositTransactionException("withdrawal failed",throwable);
            }
        } finally {
            session.close();
        }

    }

/*
    public void withdrawAccount(String accountNumber, double withdrawMoney, String transferAccountNumber)
            throws WithdrawalTransactionException {

        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        Account transferAccount = accountRepository.findAccountByAccountNumber(transferAccountNumber);

        Throwable throwable = new Throwable();
        if(account == null) throw new WithdrawalTransactionException("Account does not exist", throwable);

        account.setBalance(account.getBalance() - withdrawMoney);

          Transfer transfer = transferAndTransferDateService.
                   createAndSaveNewTransfer(accountNumber, transferAccountNumber, "-"+ withdrawMoney,
                           account, transferAccount, "Withdraw Money");
           updateTransferListForAccount(account, transfer);
    }

    public void depositAccount(String accountNumber, double depositMoney, String transferAccountNumber)
            throws DepositTransactionException {

        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        Account transferAccount = accountRepository.findAccountByAccountNumber(transferAccountNumber);

        Throwable throwable = new Throwable();
        if(account == null) throw new DepositTransactionException("Account does not exist", throwable);

//        account.setBalance(account.getBalance() + depositMoney);
//                  Transfer transfer = transferAndTransferDateService.createAndSaveNewTransfer(accountNumber, transferAccountNumber, "-"+ depositMoney,
//                  account, transferAccount, "Deposit Money");
//                  updateTransferListForAccount(account, transfer);

        Transfer transfer = new Transfer(accountNumber, "+" + depositMoney,
                transferAndTransferDateService.addNewTransferDateIfNotExist(), account);
       transferRepository.save(transfer);

    }
*/






    @Transactional
    public void transferAccount (String accountNumber, double transferredMoney, String transferAccountNumber)
            throws WithdrawalTransactionException, DepositTransactionException {

        withdrawAccount(accountNumber,transferredMoney, transferAccountNumber);
        depositAccount(transferAccountNumber, transferredMoney, accountNumber);
    }




    private void updateTransferListForAccount (Account account, Transfer transfer) {
        List<Transfer> transferList = account.getTransfers();
        transferList.add(transfer);
        account.setTransfers(transferList);
        accountRepository.save(account);
    }


    public Map<String, List<Transfer>> createMapOfDatesAndListsOfTransfers(String accountNumber) {

        //get current Account, assuming Account does exist -> error handling later
        Account account = accountRepository.findAccountByAccountNumber(accountNumber);
        //get all Transfers from this account
        List<Transfer> transfersForThisAccount = account.getTransfers();

        if (transfersForThisAccount != null) {
            //get all Dates in a set (no duplicates!)
            Set<String> dates = new HashSet<>();
            for (Transfer transfer : transfersForThisAccount) {
                String transferDate = transfer.getTransferDate().getTransferDate();
                dates.add(transferDate);
            }

            //Create map with a String date as key and a list<Transfer> as a value
            Iterator<String> it = dates.iterator();
            Map<String, List<Transfer>> mapWithTransferForEachDate = new HashMap<>();
            while (it.hasNext()) {
                String transferDate = it.next();
                //Create a list of transfers for current date and current account
                List<Transfer> transfersForEachDate = new ArrayList<>();
                for (Transfer transfer : transfersForThisAccount) {
                    if (Objects.equals(transfer.getTransferDate().getTransferDate(),
                            transferDate)) {
                        transfersForEachDate.add(transfer);
                    }
                }
                mapWithTransferForEachDate.put(transferDate, transfersForEachDate);
            }
            return mapWithTransferForEachDate;
        }
        return null;
    }

    private Session getSession () {
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        return  sessionFactory.openSession();
    }

}


package BankingApp.dto;

import BankingApp.entity.Account;
import BankingApp.entity.TransferDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class TransferRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="transfer_id")
    private int transferID;

    @Column(name= "account_number")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    @Column(name = "account_number_receiver")
    private String accountNumberReceiver;

    @Column(name = "transferred_Money")
    private double transferredMoney;


    @ManyToOne

    private TransferDate transferDate;

    public TransferDate getTransferDate() {
        return transferDate;
    }


    private String accountInfo;

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public TransferRequest(String accountNumberReceiver, double transferredMoney, TransferDate transferDate,
                    Account account) {
        this.accountNumberReceiver = accountNumberReceiver;
        this.transferredMoney = transferredMoney;
        this.transferDate = transferDate;
        this.account = account;
    }

    @ManyToOne
    private Account account;

    public TransferRequest() {
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getAccountNumberReceiver() {
        return accountNumberReceiver;
    }

    public double getTransferredMoney() {
        return transferredMoney;
    }

    public void setTransferredMoney(double transferMoney) {
        this.transferredMoney = transferMoney;
    }
}


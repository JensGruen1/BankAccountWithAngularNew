package BankingApp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="transfer_id")
    private int transferID;


    @Column(name = "account_number_receiver")
    private String accountNumberReceiver;

    @Column(name = "transferred_Money")
    private String transferredMoney;


    @ManyToOne
    @JoinColumn(name = "transferDate_id")
    @JsonIgnore
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

    public Transfer(String accountNumberReceiver, String transferredMoney, TransferDate transferDate,
                    Account account) {
        this.accountNumberReceiver = accountNumberReceiver;
        this.transferredMoney = transferredMoney;
        this.transferDate = transferDate;
        this.account = account;
    }

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    public Transfer() {
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

    public String getTransferredMoney() {
        return transferredMoney;
    }

    public void setTransferredMoney(String transferMoney) {
        this.transferredMoney = transferMoney;
    }
}

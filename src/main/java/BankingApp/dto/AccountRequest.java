package BankingApp.dto;

import BankingApp.entity.User;
import jakarta.persistence.*;

@Entity
public class AccountRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name ="account_id")
    private int accountId;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "account_type")
    private String accountType;

    private double balance;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) throws IllegalArgumentException {

        this.balance = balance;

        if (balance<0) {
            throw new IllegalArgumentException();
        }

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    private User user;


//    @OneToMany
//    @JoinColumn(name = "account_id")
//    private List<Transfer> transfers;
//
//    public List<Transfer> getTransfers() {
//        return transfers;
//    }
//
//    public void setTransfers(List<Transfer> transfers) {
//        this.transfers = transfers;
//    }
}

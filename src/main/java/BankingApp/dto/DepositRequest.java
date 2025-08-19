package BankingApp.dto;

import jakarta.persistence.Entity;


public class DepositRequest {
    private String accountNumber;
    private double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }
}

package BankingApp.dto;

public class WithdrawRequest {
    private String accountNumber;
    private double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAmount() {
        return amount;
    }
}

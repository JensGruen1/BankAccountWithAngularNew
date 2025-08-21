package BankingApp.util;

import jakarta.transaction.TransactionalException;

public class WithdrawalTransactionException extends TransactionalException {

    public WithdrawalTransactionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}

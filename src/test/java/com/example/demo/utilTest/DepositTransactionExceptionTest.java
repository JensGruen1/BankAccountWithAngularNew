package com.example.demo.utilTest;

import org.junit.jupiter.api.Test;
import BankingApp.util.DepositTransactionException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepositTransactionExceptionTest {

    @Test
    void constructor_shouldSetMessage () {
        //given
        String s = "deposit failed";
        Throwable throwable = new Throwable();

        //when
        DepositTransactionException ex = new DepositTransactionException(s,throwable);

        //then
        assertThat (ex.getMessage()).isEqualTo(s);

    }

    @Test
    void depositTransactionException_throwsNewDepositTransactionException () {
        //given
        String s = "deposit failed";
        Throwable throwable = new Throwable();

        //when //then
        assertThrows(DepositTransactionException.class,() -> { throw new DepositTransactionException(s,throwable); });
    }


    //Withdrawal Exception is doing the same, so no tests for this

}

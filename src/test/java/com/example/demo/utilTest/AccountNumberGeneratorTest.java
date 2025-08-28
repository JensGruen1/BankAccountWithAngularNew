package com.example.demo.utilTest;

import org.junit.jupiter.api.Test;
import BankingApp.util.AccountNumberGenerator;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountNumberGeneratorTest {

    @Test
    void accountNumberGenerator_shouldReturnCorrectLength () {
        //given
        //when
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        //then
        assertThat (accountNumber.length()).isEqualTo(16);
    }

    //no additional testing for now, because accountNumber has some restrictions in reality

}

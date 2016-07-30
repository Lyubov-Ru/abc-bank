package com.abc;

import com.abc.account.ZeroInterestAccount;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class TransactionTest {
    @Test
    public void correctlyComputesWithdrawalDirection() {
        ZeroInterestAccount account1 = new ZeroInterestAccount();
        ZeroInterestAccount account2 = new ZeroInterestAccount();

        assertThat(new Transaction(BigDecimal.valueOf(5),
                new LocalDateTime(), Transaction.TransactionType.DEPOSIT)
                .isWithdrawalFromAccount(account1)).isFalse();

        assertThat(new Transaction(BigDecimal.valueOf(5),
                new LocalDateTime(), account1, account2)
                .isWithdrawalFromAccount(account1)).isTrue();

        assertThat(new Transaction(BigDecimal.valueOf(5),
                new LocalDateTime(), account1, account2)
                .isWithdrawalFromAccount(account2)).isFalse();

    }
}

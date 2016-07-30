package com.abc.account;

import org.joda.time.LocalDate;
import org.junit.Test;

import java.math.BigDecimal;

import static com.abc.TestUtils.PRECISION;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

public class SavingsAccountTest {
    @Test
    public void computesInterestCorrectlyForZeroBalance() {
        SavingsAccount account = new SavingsAccount();
        assertThat(account.computeInterestForPreviousPeriod(new BalanceSnapshot(new LocalDate())))
                .isZero();
    }

    @Test
    public void computesInterestCorrectlyForOverdraftBalance() {
        SavingsAccount account = new SavingsAccount();
        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(), BigDecimal.valueOf(-100),
                BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(account.computeInterestForPreviousPeriod(new BalanceSnapshot(new LocalDate())))
                .isZero();
    }

    @Test
    public void computesInterestCorrectlyForPositiveBalanceNonLeapYear() {
        SavingsAccount checkingAccount = new SavingsAccount();

        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(2015, 2, 1),
                BigDecimal.valueOf(1000), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(checkingAccount.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(1000 * 0.1 / 100 / 365, offset(PRECISION));

        previousPeriod = new BalanceSnapshot(new LocalDate(2015, 2, 1),
                BigDecimal.valueOf(1001), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(checkingAccount.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(1001 * 0.2 / 100 / 365, offset(PRECISION));
    }


}

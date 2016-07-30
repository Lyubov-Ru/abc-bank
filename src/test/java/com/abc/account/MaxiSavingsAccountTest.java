package com.abc.account;

import com.abc.TestUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.math.BigDecimal;

import static com.abc.TestUtils.PRECISION;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

public class MaxiSavingsAccountTest {
    @Test
    public void computesInterestCorrectlyForZeroBalance() {
        MaxiSavingsAccount account = new MaxiSavingsAccount();
        assertThat(account.computeInterestForPreviousPeriod(new BalanceSnapshot(new LocalDate())))
                .isZero();
    }

    @Test
    public void computesInterestCorrectlyForOverdraftBalance() {
        MaxiSavingsAccount account = new MaxiSavingsAccount();
        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(), BigDecimal.valueOf(-100),
                BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(account.computeInterestForPreviousPeriod(new BalanceSnapshot(new LocalDate())))
                .isZero();
    }

    @Test
    public void computesInterestCorrectlyForPositiveBalanceNonLeapYearNoWithdrawals() {
        MaxiSavingsAccount account = new MaxiSavingsAccount();

        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(2015, 2, 1),
                BigDecimal.valueOf(1000), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(account.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(1000 * 5.0 / 100 / 365, offset(PRECISION));
    }

    @Test
    public void computesInterestCorrectlyForPositiveBalanceNonLeapYearWithdrawal() {
        MaxiSavingsAccount account = new MaxiSavingsAccount();
        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        account.deposit(BigDecimal.valueOf(1000));
        account.withdraw(BigDecimal.valueOf(1000));

        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(2015, 2, 11),
                BigDecimal.valueOf(1000), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(account.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(1000 * 5.0 / 100 / 365, offset(PRECISION));

        previousPeriod = new BalanceSnapshot(new LocalDate(2015, 2, 10),
                BigDecimal.valueOf(1000), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(account.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(1000 * 0.1 / 100 / 365, offset(PRECISION));
    }


}

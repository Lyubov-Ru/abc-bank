package com.abc.account;

import com.abc.Bank;
import com.abc.Customer;
import org.joda.time.LocalDate;
import org.junit.Test;

import java.math.BigDecimal;

import static com.abc.TestUtils.PRECISION;
import static org.fest.assertions.api.Assertions.*;

public class CheckingAccountTest {
    @Test
    public void computesInterestCorrectlyForZeroBalance() {
        CheckingAccount checkingAccount = new CheckingAccount();
        assertThat(checkingAccount.computeInterestForPreviousPeriod(new BalanceSnapshot(new LocalDate())))
                .isZero();
    }

    @Test
    public void computesInterestCorrectlyForOverdraftBalance() {
        CheckingAccount checkingAccount = new CheckingAccount();
        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(), BigDecimal.valueOf(-100),
                BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(checkingAccount.computeInterestForPreviousPeriod(new BalanceSnapshot(new LocalDate())))
                .isZero();
    }

    @Test
    public void computesInterestCorrectlyForPositiveBalanceNonLeapYear() {
        CheckingAccount checkingAccount = new CheckingAccount();
        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(2015, 2, 1),
                BigDecimal.valueOf(100), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(checkingAccount.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(100 * 0.1 / 100 / 365, offset(PRECISION));
    }

    @Test
    public void computesInterestCorrectlyForPositiveBalanceLeapYear() {
        CheckingAccount checkingAccount = new CheckingAccount();
        BalanceSnapshot previousPeriod = new BalanceSnapshot(new LocalDate(2016, 2, 1),
                BigDecimal.valueOf(100), BigDecimal.ZERO, BigDecimal.ZERO);
        assertThat(checkingAccount.computeInterestForPreviousPeriod(previousPeriod).doubleValue())
                .isEqualTo(100 * 0.1 / 100 / 366, offset(PRECISION));
    }

}

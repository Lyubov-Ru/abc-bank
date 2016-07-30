package com.abc.account;

import com.abc.TestUtils;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.math.BigDecimal;

import static com.abc.TestUtils.PRECISION;
import static org.fest.assertions.api.Assertions.*;

public class AccountTest {

    @Test
    public void computesBalanceWithNoTransactions() {
        ZeroInterestAccount account = new ZeroInterestAccount();
        assertThat(account.getCurrentBalance().getTotalBalance()).isZero();
        assertThat(account.getCurrentBalance().getInterestPaidForPreviousPeriod()).isZero();
    }

    @Test
    public void computesBalanceWithSingleTransaction() {
        ZeroInterestAccount account = new ZeroInterestAccount();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        account.deposit(BigDecimal.ONE);

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 6, 0));
        BalanceSnapshot currentBalance = account.getCurrentBalance();

        assertThat(currentBalance.getTotalBalance()).isZero();
        assertThat(currentBalance.getInterestPaidForPreviousPeriod()).isZero();
        assertThat(currentBalance.getTotalInterestPaid()).isZero();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 2, 6, 0));
        currentBalance = account.getCurrentBalance();

        assertThat(currentBalance.getTotalBalance()).isEqualTo(BigDecimal.ONE);
        assertThat(currentBalance.getInterestPaidForPreviousPeriod()).isZero();
        assertThat(currentBalance.getTotalInterestPaid()).isZero();
    }

    @Test
    public void computesBalanceAndInterestWithSingleTransaction() {
        CheckingAccount account = new CheckingAccount();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        account.deposit(BigDecimal.valueOf(100));

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 3, 6, 0));
        BalanceSnapshot currentBalance = account.getCurrentBalance();

        double interestPaid = 100 * 0.1 / 100 / 365;
        assertThat(currentBalance.getTotalBalance().doubleValue())
                .isEqualTo(100 + interestPaid, offset(PRECISION));
        assertThat(currentBalance.getInterestPaidForPreviousPeriod().doubleValue())
                .isEqualTo(interestPaid, offset(PRECISION));
        assertThat(currentBalance.getTotalInterestPaid().doubleValue())
                .isEqualTo(interestPaid, offset(PRECISION));
    }

    @Test
    public void computesBalanceAndInterestWithSingleTransactionComplexInterest() {
        CheckingAccount account = new CheckingAccount();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        account.deposit(BigDecimal.valueOf(100));

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 4, 6, 0));
        BalanceSnapshot currentBalance = account.getCurrentBalance();

        double interestPaidFirstDay = 100 * 0.1 / 100 / 365;
        double interestPaidSecondDay = (100 + interestPaidFirstDay) * 0.1 / 100 / 365;
        assertThat(currentBalance.getTotalBalance().doubleValue())
                .isEqualTo(100 + interestPaidFirstDay + interestPaidSecondDay, offset(PRECISION));
        assertThat(currentBalance.getInterestPaidForPreviousPeriod().doubleValue())
                .isEqualTo(interestPaidSecondDay, offset(PRECISION));
        assertThat(currentBalance.getTotalInterestPaid().doubleValue())
                .isEqualTo(interestPaidFirstDay + interestPaidSecondDay, offset(PRECISION));
    }

    @Test
    public void computesBalanceWithTwoTransaction() {
        ZeroInterestAccount account = new ZeroInterestAccount();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        account.deposit(BigDecimal.ONE);
        account.withdraw(BigDecimal.ONE);

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 4, 5, 0));
        BalanceSnapshot currentBalance = account.getCurrentBalance();

        assertThat(currentBalance.getTotalBalance()).isZero();
        assertThat(currentBalance.getInterestPaidForPreviousPeriod()).isZero();
        assertThat(currentBalance.getTotalInterestPaid()).isZero();
    }

    @Test
    public void computesBalanceWithTransferTransaction() {
        ZeroInterestAccount account = new ZeroInterestAccount();
        ZeroInterestAccount account2 = new ZeroInterestAccount();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        account.deposit(BigDecimal.ONE);
        account.trasferTo(BigDecimal.ONE, account2);

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 3, 5, 0));

        assertThat(account.getCurrentBalance().getTotalBalance()).isZero();
        assertThat(account2.getCurrentBalance().getTotalBalance()).isEqualTo(BigDecimal.ONE);
    }
}

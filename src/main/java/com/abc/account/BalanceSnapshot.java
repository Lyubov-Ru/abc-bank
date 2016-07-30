package com.abc.account;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class BalanceSnapshot {
    private final LocalDate date;
    private final BigDecimal totalBalance;
    private final BigDecimal interestPaidForPreviousPeriod;
    private final BigDecimal totalInterestPaid;

    public BalanceSnapshot(LocalDate date, BigDecimal totalBalance,
                           BigDecimal interestPaidForPreviousPeriod, BigDecimal totalInterestPaid) {
        this.date = date;
        this.totalBalance = totalBalance;
        this.interestPaidForPreviousPeriod = interestPaidForPreviousPeriod;
        this.totalInterestPaid = totalInterestPaid;
    }

    public BalanceSnapshot(BalanceSnapshot previousPeriod,
                           BigDecimal transactionsSaldo,
                           BigDecimal interestPaidForPreviousPeriod) {
        this.date = previousPeriod.getDate().plusDays(1);
        this.totalBalance = previousPeriod.getTotalBalance()
                .add(transactionsSaldo)
                .add(interestPaidForPreviousPeriod);
        this.interestPaidForPreviousPeriod = interestPaidForPreviousPeriod;
        this.totalInterestPaid = previousPeriod.totalInterestPaid.add(interestPaidForPreviousPeriod);
    }

    public BalanceSnapshot(LocalDate date) {
        this(date, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public BigDecimal getInterestPaidForPreviousPeriod() {
        return interestPaidForPreviousPeriod;
    }

    public BigDecimal getTotalInterestPaid() {
        return totalInterestPaid;
    }
}

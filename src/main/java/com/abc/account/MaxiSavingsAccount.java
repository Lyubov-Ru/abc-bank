package com.abc.account;

import com.abc.DateProvider;
import com.abc.Transaction;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MaxiSavingsAccount extends Account {
    private static final BigDecimal YEAR_RATE_BASE = BigDecimal.valueOf(0.1)
            .divide(BigDecimal.valueOf(100));
    private static final BigDecimal YEAR_RATE_NO_WITHDRAWALS = BigDecimal.valueOf(5)
            .divide(BigDecimal.valueOf(100));

    public String getName() {
        return "Maxi Savings Account";
    }

    @Override
    protected BigDecimal computeInterestForPreviousPeriod(BalanceSnapshot previousPeriod) {
        if (previousPeriod.getTotalBalance().compareTo(BigDecimal.ZERO) == -1) {
            return BigDecimal.ZERO;
        }

        boolean withdrawalsWere = hasWithdrawalTransactionsMadeDuringPeriod(
                previousPeriod.getDate().minusDays(9),
                previousPeriod.getDate());

        BigDecimal rate = withdrawalsWere ? YEAR_RATE_BASE : YEAR_RATE_NO_WITHDRAWALS;

        return previousPeriod.getTotalBalance()
                .multiply(getDailyInterestRateFromAnnualRate(rate, previousPeriod.getDate()));
    }

    protected boolean hasWithdrawalTransactionsMadeDuringPeriod(final LocalDate fromIncluding,
                                                                          final LocalDate toIncluding) {
        LocalDate toExcludingDate = toIncluding.plusDays(1);
        for (Transaction transaction : Lists.reverse(getTransactions())) {
            if (toExcludingDate.isBefore(transaction.getTransactionDate().toLocalDate())) continue;
            if (fromIncluding.isAfter(transaction.getTransactionDate().toLocalDate())) break;

            if (transaction.isWithdrawalFromAccount(this)) return true;
        }

        return false;
    }
}

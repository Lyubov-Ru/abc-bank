package com.abc.account;

import java.math.BigDecimal;

public class CheckingAccount extends Account {
    private static final BigDecimal YEAR_RATE = BigDecimal.valueOf(0.1)
            .divide(BigDecimal.valueOf(100));

    @Override
    protected BigDecimal computeInterestForPreviousPeriod(BalanceSnapshot previousPeriod) {
        if (previousPeriod.getTotalBalance().compareTo(BigDecimal.ZERO) == -1) {
            return BigDecimal.ZERO;
        }

        return previousPeriod.getTotalBalance()
                .multiply(getDailyInterestRateFromAnnualRate(YEAR_RATE, previousPeriod.getDate()));
    }

    public String getName() {
        return "Checking Account";
    }

}

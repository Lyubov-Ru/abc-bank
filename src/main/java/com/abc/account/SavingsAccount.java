package com.abc.account;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private static final BigDecimal YEAR_RATE_UNDER_1000 = BigDecimal.valueOf(0.1)
            .divide(BigDecimal.valueOf(100));
    private static final BigDecimal YEAR_RATE_ABOVE_1000 = BigDecimal.valueOf(0.2)
            .divide(BigDecimal.valueOf(100));
    private static final BigDecimal LIMIT_1000 = BigDecimal.valueOf(1000);

    @Override
    protected BigDecimal computeInterestForPreviousPeriod(BalanceSnapshot previousPeriod) {
        if (previousPeriod.getTotalBalance().compareTo(BigDecimal.ZERO) == -1) {
            return BigDecimal.ZERO;
        }

        BigDecimal rate = previousPeriod.getTotalBalance().compareTo(LIMIT_1000) == 1
                ? YEAR_RATE_ABOVE_1000 : YEAR_RATE_UNDER_1000;

        return previousPeriod.getTotalBalance()
                .multiply(getDailyInterestRateFromAnnualRate(rate, previousPeriod.getDate()));
    }

    public String getName() {
        return "Savings Account";
    }

}

package com.abc.account;

import java.math.BigDecimal;

public class ZeroInterestAccount extends Account {
    protected BigDecimal computeInterestForPreviousPeriod(BalanceSnapshot previousPeriod) {
        return BigDecimal.ZERO;
    }

    public String getName() {
        return "Zero Interest Account";
    }

    protected BigDecimal getYearInterest(BigDecimal currentAmount) {
        return null;
    }
}

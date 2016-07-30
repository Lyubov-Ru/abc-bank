package com.abc;

import com.abc.account.Account;

import java.math.BigDecimal;
import java.util.*;

public class Customer {
    private final String name;
    private final List<Account> accounts = new ArrayList<Account>();

    public Customer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Customer openAccount(Account account) {
        if ((account != null) && !accounts.contains(account)) {
            accounts.add(account);
        }
        return this;
    }

    public int getNumberOfAccounts() {
        return accounts.size();
    }

    public BigDecimal getTotalInterestPaid() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (Account a : accounts)
            total = total.add(a.getCurrentBalance().getTotalInterestPaid());
        return total;
    }

    public BigDecimal getTotalBalance() {
        BigDecimal total = BigDecimal.valueOf(0);
        for (Account a : accounts)
            total = total.add(a.getCurrentBalance().getTotalBalance());
        return total;
    }

    public String getStatement() {
        return Reporter.getInstance().createCustomerStatementReport(this);
    }

    public List<Account> getAccounts() {
        return Collections.unmodifiableList(accounts);
    }
}

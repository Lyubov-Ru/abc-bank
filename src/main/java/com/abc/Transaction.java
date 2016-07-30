package com.abc;

import com.abc.account.Account;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
    private final BigDecimal amount;

    private final LocalDateTime transactionDate;

    private final TransactionType type;

    private  final Account to;

    private  final Account from;

    public Transaction(BigDecimal amount, LocalDateTime date, TransactionType type) {
        this.amount = amount;
        this.transactionDate = date;
        this.type = type;
        this.to = null;
        this.from = null;
    }

    public Transaction(BigDecimal amount, LocalDateTime date, Account from, Account to) {
        this.amount = amount;
        this.transactionDate = date;
        this.type = TransactionType.TRANSFER;
        this.to = to;
        this.from = from;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public Account getTo() {
        return to;
    }

    public Account getFrom() {
        return from;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getNormalizedAmount(Account account) {
        validateAccount(account);

        return isWithdrawalFromAccount(account) ? amount.negate() : amount;
    }

    private void validateAccount(Account account) {
        Preconditions.checkArgument(
                !(type == TransactionType.TRANSFER
                && to != account
                && from != account), "Invalid account");
    }

    public boolean isWithdrawalFromAccount(Account account) {
        validateAccount(account);

        switch (type) {
            case DEPOSIT: return false;
            case WITHDRAWAL: return true;
            case TRANSFER: return to != account;
        }

        throw new IllegalStateException("type is invalid");
    }

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER;
    }
}

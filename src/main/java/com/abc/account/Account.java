package com.abc.account;

import com.abc.DateProvider;
import com.abc.Transaction;
import com.abc.exceptions.IllegalTransactionAmount;
import com.google.common.base.Preconditions;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.util.*;


public abstract class Account {

    public static final BigDecimal LEAP_YEAR = BigDecimal.valueOf(366);
    public static final BigDecimal NORMAL_YEAR = BigDecimal.valueOf(365);
    public static final int SCALE = 20;

    private List<Transaction> transactions = new ArrayList<Transaction>();
    private Map<LocalDate, BalanceSnapshot> snapshots = new HashMap<LocalDate, BalanceSnapshot>();

    public void deposit(BigDecimal amount) throws IllegalTransactionAmount {
        addTransaction(amount, Transaction.TransactionType.DEPOSIT);
    }

    public void withdraw(BigDecimal amount) throws IllegalTransactionAmount  {
        addTransaction(amount, Transaction.TransactionType.WITHDRAWAL);
    }

    public void trasferTo(BigDecimal amount, Account destinationAccount) throws IllegalTransactionAmount {
        validateTransactionAmount(amount);

        Transaction transaction = new Transaction(amount, DateProvider.getInstance().now(),
                this, destinationAccount);
        transactions.add(transaction);
        destinationAccount.addTransferTransaction(transaction);
    }

    private void validateTransactionAmount(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new IllegalTransactionAmount();
        }
    }

    private void addTransferTransaction(Transaction transaction) {
        Preconditions.checkArgument(transaction.getTo() == this);
        transactions.add(transaction);
    }

    private void addTransaction(BigDecimal amount, Transaction.TransactionType type) {
        validateTransactionAmount(amount);

        transactions.add(new Transaction(amount, DateProvider.getInstance().now(), type));
    }

    private synchronized BalanceSnapshot computeSnapshotsUpTo(LocalDate dateIncluding) {
        if (isBeforeFirstDeposit(dateIncluding)) {
            BalanceSnapshot initialZeroBalance = new BalanceSnapshot(dateIncluding);
            addBalanceSnapshot(initialZeroBalance);
            return initialZeroBalance;
        }

        if (isBalanceSnapshotComputedForDate(dateIncluding)) {
            return getBalanceSnapshotComputedForDate(dateIncluding);
        }

        BalanceSnapshot previousPeriod = computeSnapshotsUpTo(dateIncluding.minusDays(1));

        BalanceSnapshot currentPeriod = computeBalanceSnapshotForNextPeriod(previousPeriod);
        addBalanceSnapshot(currentPeriod);
        return currentPeriod;
    }

    public BalanceSnapshot getCurrentBalance() {
        return computeSnapshotsUpTo(DateProvider.getInstance().now().toLocalDate());
    }

    private BalanceSnapshot computeBalanceSnapshotForNextPeriod(BalanceSnapshot previousPeriod) {
        BigDecimal interestForPreviousPeriod = computeInterestForPreviousPeriod(previousPeriod);
        BigDecimal sumForPeriod = getTransactionsSumForPeriod(previousPeriod.getDate());
        return new BalanceSnapshot(previousPeriod, sumForPeriod, interestForPreviousPeriod);
    }

    private BigDecimal getTransactionsSumForPeriod(final LocalDate date) {
        BigDecimal sum = BigDecimal.ZERO;
        for (Transaction transaction : transactions) {
            if (date.equals(transaction.getTransactionDate().toLocalDate())) {
                sum = sum.add(transaction.getNormalizedAmount(this));
            }
        }
        return sum;
    }

    private BalanceSnapshot getBalanceSnapshotComputedForDate(LocalDate dateIncluding) {
        return snapshots.get(dateIncluding);
    }

    private boolean isBalanceSnapshotComputedForDate(LocalDate dateIncluding) {
        return snapshots.containsKey(dateIncluding);
    }

    private boolean isBeforeFirstDeposit(LocalDate dateIncluding) {
        return transactions.isEmpty()
                || dateIncluding.isBefore(transactions.get(0).getTransactionDate().toLocalDate());
    }

    private void addBalanceSnapshot(BalanceSnapshot balanceSnapshot) {
        snapshots.put(balanceSnapshot.getDate(), balanceSnapshot);
    }

    protected BigDecimal getDailyInterestRateFromAnnualRate(BigDecimal annualRate, LocalDate currentDate){
        BigDecimal daysInCurrentYear = currentDate.year().isLeap() ? LEAP_YEAR : NORMAL_YEAR;
        return annualRate.divide(daysInCurrentYear, SCALE, BigDecimal.ROUND_FLOOR);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public abstract String getName();
    protected abstract BigDecimal computeInterestForPreviousPeriod(BalanceSnapshot previousPeriod);
}

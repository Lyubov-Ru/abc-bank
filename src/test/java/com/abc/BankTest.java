package com.abc;

import com.abc.account.Account;
import com.abc.account.CheckingAccount;
import freemarker.template.TemplateException;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import static com.abc.TestUtils.PRECISION;
import static com.abc.TestUtils.getPatternForNumeric;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.junit.Assert.assertEquals;

public class BankTest {
    @Test
    public void computesTotalInterestAcrossSeveralAccountsAndCustomers() {
        Bank bank = buildBankWithTwoCustomers();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 3, 5, 0));
        assertThat(bank.totalInterestPaid().doubleValue())
                .isEqualTo(3 * 1000 * 0.1 / 100 / 365, offset(PRECISION));
        assertThat(bank.getCustomers().get(0).getTotalInterestPaid().doubleValue())
                .isEqualTo(1 * 1000 * 0.1 / 100 / 365, offset(PRECISION));
        assertThat(bank.getCustomers().get(1).getTotalInterestPaid().doubleValue())
                .isEqualTo(2 * 1000 * 0.1 / 100 / 365, offset(PRECISION));
    }

    private Bank buildBankWithTwoCustomers() {
        Bank bank = new Bank();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        Account checkingAccount = new CheckingAccount();
        Customer bill = new Customer("Bill").openAccount(checkingAccount);
        bank.addCustomer(bill);

        Account checkingAccount2 = new CheckingAccount();
        Account checkingAccount3 = new CheckingAccount();
        Customer bill2 = new Customer("John")
                .openAccount(checkingAccount2)
                .openAccount(checkingAccount3);
        bank.addCustomer(bill2);

        checkingAccount.deposit(BigDecimal.valueOf(1000.0));
        checkingAccount2.deposit(BigDecimal.valueOf(1000.0));
        checkingAccount3.deposit(BigDecimal.valueOf(1000.0));
        return bank;
    }

    @Test
    public void generatesSummaryReport() {
        Bank bank = buildBankWithTwoCustomers();

        System.out.println(bank.getCustomersSummaryReport());

        assertThat(bank.getCustomersSummaryReport())
            .matches(Pattern.compile(".*Bill.*1.*", Pattern.DOTALL))
            .matches(Pattern.compile(".*John.*2.*", Pattern.DOTALL));
    }

    @Test
    public void getTotalInterestPaidReport() {
        Bank bank = buildBankWithTwoCustomers();

        TestUtils.configureDateForDateProvider(new LocalDateTime(2016, 2, 1, 5, 0));

        System.out.println(bank.getTotalInterestPaidReport());

        assertThat(bank.getTotalInterestPaidReport())
            .matches(getPatternForNumeric(bank.totalInterestPaid()))
            .matches(getPatternForNumeric(bank.getCustomers().get(0).getTotalInterestPaid()))
            .matches(getPatternForNumeric(bank.getCustomers().get(1).getTotalInterestPaid()));
    }

}

package com.abc;

import com.abc.account.Account;
import com.abc.account.CheckingAccount;
import com.abc.account.SavingsAccount;
import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.abc.TestUtils.*;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class CustomerTest {

    @Test
    public void generatesStatement(){

        configureDateForDateProvider(new LocalDateTime(2015, 2, 1, 5, 0));
        Account checkingAccount = new CheckingAccount();
        Account savingsAccount = new SavingsAccount();

        Customer henry = new Customer("Henry").openAccount(checkingAccount)
                .openAccount(savingsAccount);

        checkingAccount.deposit(BigDecimal.valueOf(100000.0));
        savingsAccount.deposit(BigDecimal.valueOf(300000.0));
        savingsAccount.withdraw(BigDecimal.valueOf(90000.0));
        savingsAccount.trasferTo(BigDecimal.valueOf(2000.0), checkingAccount);

        configureDateForDateProvider(new LocalDateTime(2015, 2, 6, 5, 0));
        System.out.println(henry.getStatement());

        assertThat(henry.getStatement())
                .matches(Pattern.compile(".*Checking Account.*", Pattern.DOTALL))
                .matches(Pattern.compile(".*Savings Account.*", Pattern.DOTALL))
                .doesNotMatch(Pattern.compile(".*Maxi Savings Account.*", Pattern.DOTALL))
                .matches(getPatternForNumeric(100000.0))
                .matches(getPatternForNumeric(300000.0))
                .matches(getPatternForNumeric(90000.0))
                .matches(getPatternForNumeric(2000.0))
                .matches(getPatternForNumeric(checkingAccount.getCurrentBalance().getTotalBalance()))
                .matches(getPatternForNumeric(savingsAccount.getCurrentBalance().getTotalBalance()))
                .matches(getPatternForNumeric(henry.getTotalInterestPaid()));
   }

}

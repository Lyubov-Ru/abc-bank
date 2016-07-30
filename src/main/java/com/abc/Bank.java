package com.abc;

import com.abc.exceptions.ReportGenerationException;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;

public class Bank {
    private final List<Customer> customers  = new ArrayList<Customer>();

    public void addCustomer(Customer customer) {
        if ((customer != null) && !customers.contains(customer)) {
            customers.add(customer);
        }
    }

    public String getCustomersSummaryReport() throws ReportGenerationException {
        return Reporter.getInstance().createAllCustomersSummaryReport(getCustomers());
    }

    public String getTotalInterestPaidReport() throws ReportGenerationException {
        return Reporter.getInstance().createTotalInterestReport(getCustomers(), totalInterestPaid());
    }

    public List<Customer> getCustomers() {
        return Collections.unmodifiableList(customers);
    }

    public BigDecimal totalInterestPaid() {
        BigDecimal total = BigDecimal.valueOf(0);
        for(Customer c: customers)
            total = total.add(c.getTotalInterestPaid());
        return total;
    }

    public String getFirstCustomer() {
        return customers.isEmpty() ? null : customers.get(0).getName();
    }
}

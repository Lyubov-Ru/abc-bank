package com.abc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.abc.exceptions.ReportGenerationException;
import com.google.common.collect.ImmutableMap;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

public class Reporter {

    private final static String CUSTOMER_SUMMARY_FILE = "customerSummary.ftl";
    private final static String CUSTOMER_STATEMENT_FILE = "customerStatement.ftl";
    private final static String TOTAL_INTEREST_FILE = "totalInterest.ftl";
    private static final String TEMPLATE_LOCATION = "/report_templates";

    private Configuration cfg;

    private static Reporter instance = new Reporter();

    private Reporter() {
        cfg = new Configuration(Configuration.VERSION_2_3_25);
        cfg.setTemplateLoader(new ClassTemplateLoader(Reporter.class, TEMPLATE_LOCATION));
        cfg.setDefaultEncoding("UTF-8");
    }

    public static Reporter getInstance() {
        return instance;
    }

    public String createCustomerStatementReport(Customer customer) throws ReportGenerationException {
        return render(ImmutableMap.of("customer", customer), CUSTOMER_STATEMENT_FILE);
    }

    public String createAllCustomersSummaryReport(List<Customer> customers) throws ReportGenerationException {
        return render(ImmutableMap.of("customers", customers), CUSTOMER_SUMMARY_FILE);
    }

    public String createTotalInterestReport(List<Customer> customers,
                                            BigDecimal total) throws ReportGenerationException {
        ImmutableMap<String, ?> data = ImmutableMap.of("customers", customers, "total", total);
        return render(data, TOTAL_INTEREST_FILE);
    }

    public String createReport(Map<String, Object> data, String template) {
        return render(data, template);
    }

    private String render(Map<String, ?> data, String template) {
        try {
            Writer out = new StringWriter();
            cfg.getTemplate(template).process(data, out);
            return out.toString();
        } catch (TemplateException e) {
            throw new ReportGenerationException(e);
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }

}

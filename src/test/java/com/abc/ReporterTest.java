package com.abc;

import freemarker.template.TemplateException;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class ReporterTest {
    @Test
    public void createReport() throws TemplateException, IOException, URISyntaxException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("amount", BigDecimal.valueOf(100.00));
        assertEquals("$100.00", Reporter.getInstance().createReport(data, "test.ftl"));
    }
}

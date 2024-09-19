package it.univaq.f4i.iw.framework.application;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 *
 * @author Giuseppe Della Penna
 */
public class ApplicationInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {

        DataSource ds = null;
        Pattern protect_pattern = null;
        Map<Pattern, String> protect = new HashMap<>();

        //init protection pattern
        Enumeration parms = event.getServletContext().getInitParameterNames();
        while (parms.hasMoreElements()) {
            String name = (String) parms.nextElement();
            if (name.startsWith("security.protect.patterns")) {
                String role = name.length() > 25 ? name.substring(26).replace(".", "_") : null;
                String pattern = event.getServletContext().getInitParameter(name);
                if (pattern != null && !pattern.isBlank()) {
                    String[] split = pattern.split("\\s*,\\s*");
                    protect_pattern = Pattern.compile(Arrays.stream(split).collect(Collectors.joining("$)|(?:", "(?:", "$)")));
                } else {
                    protect_pattern = Pattern.compile("a^"); //this regular expression does not match anything!
                }
                protect.put(protect_pattern, role);
            }
        }

        //init data source
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/" + event.getServletContext().getInitParameter("data.source"));
        } catch (NamingException ex) {
            Logger.getLogger(ApplicationInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }

        event.getServletContext().setAttribute("protect", protect);
        event.getServletContext().setAttribute("datasource", ds);
    }

}

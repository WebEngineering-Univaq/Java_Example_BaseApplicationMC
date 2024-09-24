package it.univaq.f4i.iw.framework.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
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
        Pattern role_access_pattern = null;
        List<String> all_access_patterns = new ArrayList<>();
        Map<Pattern, String> role_access_patterns = new HashMap<>();

        //init protection pattern
        Enumeration parms = event.getServletContext().getInitParameterNames();
        while (parms.hasMoreElements()) {
            String name = (String) parms.nextElement();
            if (name.startsWith("security.protect.patterns")) {
                String role = name.length() > 25 ? name.substring(26).replace(".", "_") : null;
                String pattern = event.getServletContext().getInitParameter(name);
                if (pattern != null && !pattern.isBlank()) {
                    String[] split = pattern.split("\\s*,\\s*");
                    all_access_patterns.addAll(Arrays.asList(split));
                    role_access_pattern = Pattern.compile(Arrays.stream(split).collect(Collectors.joining("$)|(?:", "(?:", "$)")));
                } else {
                    role_access_pattern = Pattern.compile("a^"); //this regular expression does not match anything!
                }
                role_access_patterns.put(role_access_pattern, role);
            }
        }

        //init data source
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup("java:comp/env/" + event.getServletContext().getInitParameter("data.source"));
        } catch (NamingException ex) {
            Logger.getLogger(ApplicationInitializer.class.getName()).log(Level.SEVERE, null, ex);
        }

        
        if (!all_access_patterns.isEmpty()) {
            protect_pattern = Pattern.compile(all_access_patterns.stream().collect(Collectors.joining("$)|(?:", "(?:", "$)")));
        } else {
            protect_pattern = Pattern.compile("a^"); //this regular expression does not match anything!
        }
        event.getServletContext().setAttribute("protect_pattern",protect_pattern);
        event.getServletContext().setAttribute("role_access_patterns", role_access_patterns);
        event.getServletContext().setAttribute("datasource", ds);
    }

}

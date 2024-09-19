package it.univaq.f4i.iw.examples.controller;

import it.univaq.f4i.iw.examples.application.ApplicationBaseController;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.view.HTMLResult;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class Secure2Controller extends ApplicationBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, DataException {

        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Very Very Confidential page");
        result.appendToBody("<h1>Very Very Confidential page</h1>");
        result.appendToBody("<p>Yiu cannot access this page unless you have role1</p>");
        result.appendToBody("<p><a href=\"homepage\">Return to homepage</a></p>");
        result.activate(request, response);

    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        action_default(request, response);
    }
}

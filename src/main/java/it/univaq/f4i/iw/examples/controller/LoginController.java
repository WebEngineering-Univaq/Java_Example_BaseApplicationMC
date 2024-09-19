/*
 * LoginController.java
 *
 *
 */
package it.univaq.f4i.iw.examples.controller;

import it.univaq.f4i.iw.ex.newspaper.data.model.User;
import it.univaq.f4i.iw.examples.application.ApplicationDataLayer;
import it.univaq.f4i.iw.examples.application.ApplicationBaseController;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.view.HTMLResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class LoginController extends ApplicationBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Welcome");
        result.appendToBody("<h1>Please Login</h1>");
        //
        if (request.getAttribute("https-redirect") != null) {
            result.appendToBody("<p>WARNING! you are not using a secured (https) connection! "
                    + "Please follow <a href=\"" + request.getAttribute("https-redirect") + "\">this url</a> to login securely!</p>");
        }
        //
        result.appendToBody("<form method=\"post\" action=\"login\">");
        result.appendToBody("<p>Username: <input name=\"u\" type=\"text\"/><br/><small>Hint: try &quot;a&quot; (role1 and role2) or &quot;b&quot; (role2)</small></p>");
        result.appendToBody("<p>Password: <input name=\"p\" type=\"password\"/><br/><small>Hint: try &quot;p&quot;</small></p>");
        if (request.getParameter("referrer") != null) {
            result.appendToBody("<input name=\"referrer\" type=\"hidden\" value=\"" + request.getParameter("referrer") + "\"/></p>");
        }
        result.appendToBody("<p><input value=\"login\" name=\"login\" type=\"submit\"/></p>");
        result.appendToBody("</form>");
        result.activate(request, response);
    }

    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException, DataException {
        String username = request.getParameter("u");
        String password = request.getParameter("p");

        ApplicationDataLayer dl = (ApplicationDataLayer) request.getAttribute("datalayer");
        if (!username.isEmpty() && !password.isEmpty()) {
            User u = dl.getUserDAO().getUserByName(username);
            try {
                if (u != null && SecurityHelpers.checkPasswordHashPBKDF2(password, u.getPassword())) {
                    //se la validazione ha successo
                    //if the identity validation succeeds
                    SecurityHelpers.createSession(request, u);
                    //se è stato trasmesso un URL di origine, torniamo a quell'indirizzo
                    //if an origin URL has been transmitted, return to it
                    if (request.getParameter("referrer") != null) {
                        response.sendRedirect(request.getParameter("referrer"));
                    } else {
                        response.sendRedirect("homepage");
                    }
                    return;
                }
            } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        handleError("Login failed", request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getParameter("login") != null) {
            action_login(request, response);
        } else {
            String https_redirect_url = SecurityHelpers.checkHttps(request);
            request.setAttribute("https-redirect", https_redirect_url);
            action_default(request, response);
        }

    }

}

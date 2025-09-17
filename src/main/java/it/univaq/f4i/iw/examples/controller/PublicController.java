/*
 * PublicController.java
 *
 *
 */
package it.univaq.f4i.iw.examples.controller;

import it.univaq.f4i.iw.ex.newspaper.data.model.Article;
import it.univaq.f4i.iw.examples.application.ApplicationDataLayer;
import it.univaq.f4i.iw.examples.application.ApplicationBaseController;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.view.HTMLResult;
import java.io.*;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Ingegneria del Web
 * @version
 */
public class PublicController extends ApplicationBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, DataException {

        //preleviamo il data layer 
        //get the data layer
        ApplicationDataLayer dl = (ApplicationDataLayer) request.getAttribute("datalayer");
        List<Article> articles = dl.getArticleDAO().getArticles();
        HTMLResult result = new HTMLResult(getServletContext());
        result.setTitle("Show Articles");
        result.appendToBody("<h1>Articles available</h1>");
        if (!articles.isEmpty()) {
            result.appendToBody("<ul>");
            for (Article a : articles) {
                result.appendToBody("<li>"
                        + HTMLResult.sanitizeHTMLOutput(a.getTitle())
                        + "<br/><em>by " + a.getAuthor().getName() + " " + a.getAuthor().getSurname() + "</em>"
                        + "<br/><small>" + (a.getIssue() != null ? ("published on issue #" + a.getIssue().getNumber()) : "<em>unpublished</em>") + "</small>"
                        + "</li>");
            }
            result.appendToBody("</ul>");
            result.appendToBody("<p><a href=\"homepage\">Return to homepage</a></p>");

        }
        result.activate(request, response);
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        action_default(request, response);
    }
}

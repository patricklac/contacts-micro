package ch.heig.pl.presentation;

import ch.heig.pl.dto.Couple;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/uniCouple")
public class UniCouple extends HttpServlet {

    @Inject
    @RestClient
    private CoupleService coupleService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/uniCouple.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nom1 = request.getParameter("nom1");
        String nom2 = request.getParameter("nom2");

        List<String> errors = new ArrayList<>();
        if (nom1 == null || nom1.trim().equals("")) {
            errors.add("Les noms doivent être renseignés");
        }
        if (nom2 == null || nom2.trim().equals("")) {
            errors.add("Les noms doivent être renseignés");
        }

        if (errors.size() == 0) {
            try {
                coupleService.unit(new Couple(nom1,nom2));
                response.sendRedirect(request.getContextPath() + "/listeCouples");
            } catch (WebApplicationException e) {
                errors.add("Contact pas trouvé ou déjà en couple");
            }
        }
        if (errors.size() != 0) {
            request.setAttribute("nom1", nom1);
            request.setAttribute("nom2", nom2);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/pages/uniCouple.jsp").forward(request, response);
        }

    }

}

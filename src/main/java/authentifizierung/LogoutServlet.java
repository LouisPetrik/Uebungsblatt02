package authentifizierung;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import banking.Kunde;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogoutServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();

        // angemeldeten kunden aus der session holen
        Kunde kunde = (Kunde) session.getAttribute("kunde");

        // gibt noch vor und nachname an die logout.jsp weiter, ohne dass diese dafür auf die bereits gelöschte session zugreifen muss
        request.setAttribute("kunde_vorname", kunde.getVorname());
        request.setAttribute("kunde_nachname", kunde.getNachname());


        // Damit wird aus der Session nur das Kundenobjekt, also der aktuell eingeloggte Kunde entfernt
        session.removeAttribute("kunde");
        // damit vom abgemeldeten Kunden keine Daten mehr angezeigt werden
        session.removeAttribute("konten");
        session.removeAttribute("showKonto");
        session.removeAttribute("kontostand");
        session.removeAttribute("anlegenErr");
        session.removeAttribute("csvErr");

        // redirecten auf die logout.jsp wo der Kunde noch verabschiedet wird.
        // Die Session wird erst danach von dem Kunden bereinigt, damit die konto.jsp noch
        // auf die Attribute des Kunden aus der Session zugreifen kann.
        request.getRequestDispatcher("logout.jsp").forward(request, response);
    }
}

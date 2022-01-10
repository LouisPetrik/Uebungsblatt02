package authentifizierung;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class LogoutServlet
 */
@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogoutServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // bisher wird hier die komplette User-Datenbank gelöscht. Aber nur die Session vom User soll gelöscht werden. 
        System.out.println("User möchte sich abmelden"); 

        HttpSession session = request.getSession();        
        // redirecten auf die logout.jsp wo der Kunde noch verabschiedet wird. 
        // Die Session wird erst danach von dem Kunden bereinigt, damit die konto.jsp noch 
        // auf die Attribute des Kunden aus der Session zugreifen kann. 
        request.getRequestDispatcher("logout.jsp").forward(request, response); 

        // Damit wird aus der Session nur das Kundenobjekt, also der aktuell eingeloggte Kunde entfernt 
        // Die gesamte Kundendatenbank die in der Sessino die registrierten Kunden verzeichnet, bleibt erhalten. 
        // Der Kunde, der sich ausloggen will bleibt also registiert und kann sich wieder anmelden. 
        session.removeAttribute("kunde");
        
        // damit vom abgemeldeten Kunden keine Daten mehr angezeigt werden
        session.removeAttribute("kontenForm");
        session.removeAttribute("showKonto");
        session.removeAttribute("kontostand");
    }
}
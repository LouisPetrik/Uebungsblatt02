package authentifizierung;

/**
 * Dieses Servlet ist dafür da, damit sich bestehende Nutzer über die login.jsp anmelden können.
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import banking.Konto;
import banking.Kunde;


import database.DatabaseKunden;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }

    /*
     * Soll verhindern, dass beim neuladen der LoginServlet Seite
     * die Daten verloren gehen - was leider ziemlich nervig ist.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("get request an LoginServlet");




        request.getRequestDispatcher("konto.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String passwort = request.getParameter("passwort");


        // hier wird die E-mail mit regex getestet
        boolean regexMatch = false;

        regexMatch = Pattern.matches("^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$", email);

        if (regexMatch) {
            System.out.println("Die E-Mail ist Regex-konform");
        } else {
            System.out.println("Die E-Mail ist nicht regex-konform");
        }


        // die eingaben in der datenbank überprüfen, und gegebenfalls den kunden einloggen

        Kunde eingeloggterKunde = DatabaseKunden.kundeEinlogggen(email, passwort);

        // Insofern es keinen fehler gab, ist das fehlermeldungs-feld des objekts null
        if (eingeloggterKunde.fehlermeldung == null) {

            System.out.println("LoginServlet: Kunde wurde erfolgreich eingeloggt");
            System.out.println("LoginServlet, Kundenname " + eingeloggterKunde.vorname);

            // Kundenobjekt wird erstellt und in der Session gespeichert:
            session.setAttribute("kunde", eingeloggterKunde);

            // Da der Kunde erfolgreich eingeloggt wurde, wird ihm seine persönliche Konto-Seite angezeigt:
            // Eventuell muss hier statt auf die JSP selbst auf die GET methode des KontoServlets umgeleitet werden
            // damit vor dem rendern die DB-abfrage stattfinden kann.
            request.getRequestDispatcher("konto.jsp").forward(request, response);

        } else {
            System.out.println("Fehler im Kundenobjekt " + eingeloggterKunde.fehlermeldung);

            // insofern ein fehler auftritt, bleibt der user mit einer fehlermeldung auf der login.jsp

            request.setAttribute("fehlertyp", eingeloggterKunde.fehlermeldung);
            request.getRequestDispatcher("login.jsp").forward(request, response);

        }


    }
}

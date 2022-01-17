package authentifizierung;

/**
 * Dieses Servlet ist dafür da, damit sich bestehende Nutzer über die login.jsp anmelden können.
 */
import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import banking.Kunde;
import database.DatabaseKategorie;
import database.DatabaseKonto;
import database.DatabaseKunden;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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

        if (!Pattern.matches("(?=^.{5,254}$)[a-z+\\-\\.]{1,63}@[a-z+\\-\\.]+\\.[a-z+\\-\\.]+", email)) {
            System.out.println("Die E-Mail ist nicht regex-konform");
            request.setAttribute("fehlertyp", "Die E-Mail ist nicht regex-konform");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        Kunde kunde = DatabaseKunden.getKunde(email);
        if (kunde == null) {
            System.out.println("Kunde wurde nicht gefunden");
            request.setAttribute("fehlertyp", "Kunde wurde nicht gefunden(email falsch?)");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        // passwort überprüfen
        if (passwort.equals(kunde.getPasswort())) {         // richtiges passwort
            // konten von der datenbank in kunde speichern
            kunde.kontenliste = DatabaseKonto.getKonten(email);
            session.setAttribute("kunde", kunde);

            if (!kunde.kontenliste.isEmpty()) {
                session.setAttribute("konten", kunde.kontenAsHTML());
            }

            session.setAttribute("kategorienListe", DatabaseKategorie.kategorienAusgeben());

            // persönliche Konto-Seite angezeigen
            request.getRequestDispatcher("konto.jsp").forward(request, response);
        } else {                                        // falsches passwort
            System.out.println("Passwort ist falsch");
            request.setAttribute("fehlertyp", "Passwort ist falsch");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}

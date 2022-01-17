package authentifizierung;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Pattern;

import banking.Kunde;
import database.DatabaseKunden;


@WebServlet("/RegistrierungsServlet")
public class RegistrierungsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public RegistrierungsServlet() {
        super();
    }

    private void sendeFehlerMeldung(String fehlertyp, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("sendeFehlerFunktion gecalled");
        request.setAttribute("fehlertyp", fehlertyp);
        request.getRequestDispatcher("registrierung.jsp").forward(request, response);
    }

    private void restoreForm(HttpServletRequest request) {
        String vorname = request.getParameter("vorname");
        String nachname = request.getParameter("nachname");
        int alter = Integer.parseInt(request.getParameter("alter"));
        String email = request.getParameter("email");
        String bankinstitut = request.getParameter("bankinstitut");

        request.setAttribute("vorname", vorname);
        request.setAttribute("nachname", nachname);
        request.setAttribute("alter", alter);
        request.setAttribute("email", email);
        request.setAttribute("bankinstitut", bankinstitut);

        if (request.getParameter("newsletter") != null)
            request.setAttribute("newsletter", "checked");

        if (request.getParameter("bedingungen") != null)
            request.setAttribute("bedingungen", "checked");
    }

    private void clearForm(HttpServletRequest request) {
        request.removeAttribute("vorname");
        request.removeAttribute("nachname");
        request.removeAttribute("alter");
        request.removeAttribute("email");
        request.removeAttribute("bankinstitut");
        request.removeAttribute("newsletter");
        request.removeAttribute("bedingungen");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String vorname = request.getParameter("vorname");
        String nachname = request.getParameter("nachname");
        int alter = Integer.parseInt(request.getParameter("alter"));
        String email = request.getParameter("email");
        String bankinstitut = request.getParameter("bankinstitut");
        String passwort = request.getParameter("passwort");
        String passwortBestaetigung = request.getParameter("passwortBestaetigung");

        // Da ob der Kunde den Newsletter haben möchte in einem Bool gespeichert werden kann,
        // das Form aber einen String sendet wird ob ja oder nein hier zu einem Boolean verwandelt.
        boolean newsletter = (request.getParameter("newsletter") != null);

        // gewünschte Passwort und die Bestätigung müssen gleich sein
        if (!Validierung.sindPasswoerterGleich(passwort, passwortBestaetigung)) {
            System.out.println("Passwört sind nicht gleich!");

            restoreForm(request);
            sendeFehlerMeldung("Passwörter sind nicht gleich!", request, response);
            return;
        }

        if (!Pattern.matches("(?=^.{5,254}$)[a-z+\\-\\.]{1,63}@[a-z+\\-\\.]+\\.[a-z+\\-\\.]+", email)) {
            System.out.println("Die E-Mail ist nicht Regex-konform");

            restoreForm(request);
            sendeFehlerMeldung("Keine gültige E-Mail (nicht regex-konform)", request, response);
            return;
        }

        if (!Pattern.matches("[A-ZÄÜÖ][a-zäüö]*(( |\\-)[a-zäüö]+)*", vorname)) {
           System.out.println("Vorname ist nicht Regex konform");
            restoreForm(request);
            sendeFehlerMeldung("Vorname ist nicht Regex konform", request, response);
            return;
        }

        if (!Pattern.matches("[A-ZÄÜÖ][a-zäüö]*(( |\\-)[a-zäüö]+)*", nachname)) {
            System.out.println("Nachname ist nicht Regex konform");
            restoreForm(request);
            sendeFehlerMeldung("Nachname ist nicht Regex konform", request, response);
            return;
        }

        // ist die Email bereits benutzt wurden
        if (Validierung.hasEmail(email)) {
            System.out.println("Mail wurde bereits benutzt!");

            restoreForm(request);
            sendeFehlerMeldung("Diese Mail wird bereits verwendet!", request, response);
            return;
        }

        Kunde kunde = DatabaseKunden.addKunde(vorname, nachname, alter, email,
                bankinstitut, passwort, newsletter);
        if (kunde == null) {
            System.out.println("Fehler beim speichern in der Datenbank");

            restoreForm(request);
            sendeFehlerMeldung("Fehler beim speichern in der Datenbank", request, response);
            return;
        }

        // validierung war erfolgreich form muss nicht restored werden also kann es gecleared werden
        clearForm(request);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}

package banking;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;

import database.DatabaseKategorie;

// Dieses Servlet dient zu verwaltung und annahme von POST-requests für das Erstellen von Kategorien
@WebServlet("/KategorienServlet")
public class KategorienServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public KategorienServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // leitet auf die konto.jsp weiter um anschließend wieder da zu sein, wo das form gesendet wurde.
    }

    // hier wird nur das Form für die Kategorien in der konto.jsp verarbeitet.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        String kategorie = request.getParameter("kategorie");
        String schlagwoerter_feld = request.getParameter("schlagwoerter");

        String kategorie_ausgewaehlt = request.getParameter("kategorie_ausgewaehlt");

        System.out.println(kategorie_ausgewaehlt);

        // für den fall, dass das form zur erstellung einer neuen kategorie abgesendet wurde
        if (kategorie != null) {

            System.out.println("User will neue Kategorie anlegen");
            System.out.println("KategorienServlet: Kategorie "+ kategorie + "Schlagw�rter " + schlagwoerter_feld);

            // verwandeln der mit komma-getrennten schlagwörter im form in ein arraylist.
            ArrayList<String> schlagwoerter = new ArrayList<>(Arrays.asList(schlagwoerter_feld.split(",")));

            System.out.println("Alle schlagwoerter: ");

            for (String schlagwort : schlagwoerter) {
             System.out.println(schlagwort);
            }

            // anlegen der neuen kategorie in der datenbank
            DatabaseKategorie.kategorieErstellen(kategorie, schlagwoerter);

            // testweise ausgeben aller erstellen kategorien:
            ArrayList<String> kategorienListe = new ArrayList<>(DatabaseKategorie.kategorienAusgeben());

            System.out.println("Liste aller Kategorien:");
            for (String kategorieName : kategorienListe) {
             System.out.println(kategorieName);
            }

            // damit der kunde in der konto-seite auf die kategorien zugreifen kann, werden diese noch aus der DB in der session gespeichert:
            session.setAttribute("kategorienListe", kategorienListe);

            // wieder auf die konto.jsp umleiten:
            request.getRequestDispatcher("konto.jsp").include(request, response);
        }

        // für den fall, dass das form zur ausgabe aller posten einer kategorie abgesendet wurde
        if (kategorie_ausgewaehlt != null) {
            System.out.println("User will alle Posten der Kategorie " + kategorie_ausgewaehlt);

            // alle schlagwoerter der kategorie rausfinden:
            ArrayList<String> schlagwoerter = new ArrayList<>(DatabaseKategorie.schlagwoerterFinden(kategorie_ausgewaehlt));

            // alle transaktionen in denen die schlagwoerter vorkommen:
            ArrayList<String> transaktionsFazit = new ArrayList<>(DatabaseKategorie.filterTransaktionen(schlagwoerter));
            System.out.println("Die transaktionsfazit: ");
            for (String transaktion : transaktionsFazit) {
                System.out.println(transaktion);
            }

            // das fazit der transaktionen wird als arraylist an die konto JSP weitergeben
            request.setAttribute("transaktionsFazit", transaktionsFazit);

            request.getRequestDispatcher("konto.jsp").include(request, response);
        }
    }
}

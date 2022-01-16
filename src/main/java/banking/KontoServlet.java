package banking;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.DatabaseKonto;


@WebServlet("/KontoServlet")
public class KontoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public KontoServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hier wird abgefangen, falls jemand nachdem Aufruf der KontoServlet URL die Seite neulädt, also
        // einen GET-Request an /KontoServlet durchführt - in diesem Fall soll das direkt weiter an die konto.jsp, die alle
        // Inhalte darstellt.
        request.getRequestDispatcher("konto.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Kunde kunde = (Kunde) session.getAttribute("kunde");
        System.out.println("Der user ist: " + kunde.getEmail());

        // neues Konto erstellen
        String kontoname = request.getParameter("kontoname");
        if (kontoname != null) {
            if (kontoname == "") {
                System.out.println("error leerer Konotoname");
                session.setAttribute("anlegenErr", "Geben Sie dem Konto einen Namen!!");
                request.getRequestDispatcher("konto.jsp").forward(request, response);
                return;
            }

            // testen ob dieser Kontoname schon genutzt wurde
            for(int i = 0; i < kunde.kontenliste.size(); i++) {
                if (kunde.kontenliste.get(i).name.equals(kontoname)) {
                    System.out.println("Konto mit dem Namen" + kontoname + "wurde bereits erstellt");
                    session.setAttribute("anlegenErr", "Es gibt schon ein Konto mit diesen Namen");
                    request.getRequestDispatcher("konto.jsp").forward(request, response);
                    return;
                }
            }
   
            DatabaseKonto.addKonto(kunde.getEmail(), kontoname);
            kunde.kontenliste = DatabaseKonto.getKonten(kunde.getEmail());

            if (!kunde.kontenliste.isEmpty()) {
            	session.setAttribute("konten", kunde.kontenAsHTML());
            }
            
            session.removeAttribute("anlegenErr"); // keine fehler mehr anzeigen
        // Konto anzeigen
        } else {
            String selectedKonto = request.getParameter("selectedKonto");
            
            if (selectedKonto != null) {
            	Konto konto = kunde.kontenliste.get(Integer.parseInt(selectedKonto));
            			
                if (konto.txs.isEmpty()) {
                	konto.txs = DatabaseKonto.getTxs(konto);
                }
 
                if (konto.hasTxs()) {
                     session.setAttribute("showKonto", konto.txsAsHTML());
                     session.setAttribute("kontostand", konto.getKontostand());
                } else {
                	session.setAttribute("showKonto", "<b>keine Transaktionen (laden sie eine CSV hoch)</b>");
                }
            }
        }

        request.getRequestDispatcher("konto.jsp").include(request, response);
    }
}

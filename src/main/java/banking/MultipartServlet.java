package banking;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


@WebServlet("/MultipartServlet")
@MultipartConfig
public class MultipartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public MultipartServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        Kunde kunde = (Kunde) session.getAttribute("kunde");
        System.out.println("Der user ist: " + kunde.getEmail());

        final Part filePart = request.getPart("csvFile");

        InputStream filecontent = filePart.getInputStream();
        String csvFile = new String(filecontent.readAllBytes());
        String selectedKonto = request.getParameter("selectedKonto");

        System.out.println("selectedKonto " + selectedKonto);
        if (selectedKonto != null) {
            int konto_idx = Integer.parseInt(selectedKonto);

            System.out.println("csvFile '" + csvFile + "'");
            if (csvFile != "") {    // input type="file" has always default value ""
                                            // so instead of null it would return ""

                // sollte immer true sein aber zur sicherheit
                if (konto_idx < kunde.kontenliste.size()) {
                    kunde.kontenliste.get(konto_idx).loadCSV(csvFile);

                    String kontostand = "<p>kontostand: " + kunde.kontenliste.get(konto_idx).getKontostand() + "</p>";
                    session.setAttribute("kontostand", kontostand);
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("<div class=\"card\"> <div class=\"card-body\">");

            if (kunde.kontenliste.get(konto_idx).hasTxs()) {
                sb.append(kunde.kontenliste.get(konto_idx).txsAsHTML());
            } else {
               sb.append("<b>keine Transaktionen (laden sie eine CSV hoch)</b>");
            }

            sb.append("</div></div>");

            session.setAttribute("showKonto", sb.toString());
        }

        request.getRequestDispatcher("konto.jsp").include(request, response);
    }
}

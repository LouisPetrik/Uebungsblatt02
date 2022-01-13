package banking;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import banking.Konto; 
import banking.Kunde; 


/**
 * Servlet implementation class KontoServlet
 */
@WebServlet("/KontoServlet")
public class KontoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KontoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Hier wird abgefangen, falls jemand nachdem Aufruf der KontoServlet URL die Seite neulädt, also
        // einen GET-Request an /KontoServlet durchführt - in diesem Fall soll das direkt weiter an die konto.jsp, die alle
        // Inhalte darstellt.
        request.getRequestDispatcher("konto.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Damit wir die Session hier auslesen und bearbeiten können
        HttpSession session = request.getSession();

        Kunde kunde = (Kunde) session.getAttribute("kunde");
        System.out.println("Der user ist: " + kunde.getEmail());

        // neues Konto erstellen
        String kontoname = request.getParameter("kontoname");
        if (kontoname != null) {
        	System.out.println("User will Konto " + kontoname);
        	
        	
        	// deprecated 
            kunde.kontenliste.add(new Konto(kontoname, kunde.getEmail()));
            
            /* anlegen eines neuen kontos in der datenbank. Zur erkennung dient
             * die email des kundens, ist also foreign key in konto tabelle. 
            */
            
            if (kunde.kontenAsHTML() == "") {
            	session.setAttribute("kontenForm", "<b>Sie haben bisher keine Konten bei uns</b>"); 
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("<form method='POST' action='KontoServlet'>");
                
                sb.append("<select name='selectedKonto'>");
                sb.append(kunde.kontenAsHTML());
            	sb.append("</select>");
            	sb.append("<input type='submit' value='Konto anzeigen'/><br/>");
            	
                sb.append("</form><br/>");
                
                
                sb.append("<form method='POST' action='MultipartServlet' enctype='multipart/form-data'>");
                
                sb.append("<select name='selectedKonto'>");
                sb.append(kunde.kontenAsHTML());
            	sb.append("</select>");
            	sb.append("<input type='submit' value='CSV hochladen'><input type='file' name='csvFile'/>");
            	
                sb.append("</form>");
                
                session.setAttribute("kontenForm", sb.toString()); 
            }
        // Konto anzeigen
        } else {
        	String selectedKonto = request.getParameter("selectedKonto");
        	
        	System.out.println("selectedKonto " + selectedKonto);
        	if (selectedKonto != null) {
            	int konto_idx = Integer.parseInt(selectedKonto);
                
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
        }
        
        
        // Wichtig: Dies included nur den Inhalt der JSP, das heißt sämtliche Attribute die
        // beim Redirect zur konto.jsp anfänglich übergeben werden, sind verloren. Daher wurden
        // viele Daten in der Session gespeichert. 
        request.getRequestDispatcher("konto.jsp").include(request, response);
    }
}
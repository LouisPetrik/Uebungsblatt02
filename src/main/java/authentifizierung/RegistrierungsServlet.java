package authentifizierung;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import banking.Kunde;
import authentifizierung.Validierung;
import database.DatabaseKunden; 

/**
 * Servlet implementation class RegistrierungsServlet
 */
@WebServlet("/RegistrierungsServlet")
public class RegistrierungsServlet extends HttpServlet {
    /* Speichert die Liste aller registrierten Kunden.
     * Eventuell ist ein normales Array hinreichend, aber mal sehen.
     * sollte vielleicht in ein anderes Scope, aber für jetzt passt das.
     * Diese Liste soll noch in der Session gespeichert werden.
     */
    ArrayList<Kunde> kundenliste = new ArrayList<Kunde>();

    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
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
        
        boolean regexEmailMatch = false; 
        boolean regexVornameMatch = false; 
        boolean regexNachnameMatch = false; 

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
        
        
        // hier wird die E-mail mit regex getestet       
        regexEmailMatch = Pattern.matches("^[^@\\s]+@[^@\\s\\.]+\\.[^@\\.\\s]+$", email); 
        
        if (!regexEmailMatch) {
        	System.out.println("Die E-Mail ist NICHT Regex-konform"); 
        	
        	// das Form zurücksetzen mit meldung, dass es keine gültige E-Mail ist 
        } else {
        	System.out.println("Die E-Mail ist nicht regex-konform"); 
        }
        
        // hier wird der vor und nachname auf regex geteste - vor und nachname sollten den selben regex nutzen können. 
        // Eventuell könnte man die checks in eine einzelne funktion auslagern und dann alle weiteren schritte durchführen, insofern alle 
        // checks true sind 
        
        regexVornameMatch = Pattern.matches("((([A-Z]{1,1})[a-z]+))", vorname); 
        regexNachnameMatch = Pattern.matches("((([A-Z]{1,1})[a-z]+))", nachname); 
        
        if (regexVornameMatch) {
     	   System.out.println("Vorname ist Regex konform"); 
        } else {
     	   System.out.println("Vorname ist nicht regex konform"); 
        }
        
        if (regexNachnameMatch) {
        	System.out.println("Nachname ist Regex konform"); 
        } else {
        	System.out.println("Vorname ist nicht regex konform"); 
        }

        // Testen, ob die Email bereits im System registriert ist
        // DEPRECATED da nicht mehr mit der session gearbeitet wird. 
        if (Validierung.hasEmail(kundenliste, email)) {
        	System.out.println("Mail wurde bereits benutzt!"); 

        	restoreForm(request);
        	
            sendeFehlerMeldung("Diese Mail wird bereits verwendet!", request, response);
        	return;
        }
                
        System.out.println(vorname);
        System.out.println(nachname);
        System.out.println(alter);
        System.out.println(email);
        System.out.println(bankinstitut);
        System.out.println(newsletter);
        System.out.println(passwort);
        System.out.println(passwortBestaetigung);

        Kunde kunde = new Kunde(vorname, nachname, alter, email, bankinstitut, passwort, newsletter);
        kundenliste.add(kunde); 
        
        boolean kundeErfolgreichHinzu =  DatabaseKunden.fuegeKundeHinzu(kunde); 
        
        if (kundeErfolgreichHinzu) {
        	System.out.println("Wurde erfolgreich geadded"); 
        } else {
        	System.out.println("Fehler beim speichern in der Datenbank"); 
        }

        // validierung war erfolgreich form muss nicht restored werden also kann es gecleared werden
        clearForm(request);
        
        HttpSession session = request.getSession();
        session.setAttribute("bank.kundenliste", kundenliste); 
        
        // Nutzer wieder an index.jsp weiterleiten: 
        // Dies sollte nur geschehen, wenn die Registrierung erfolgreich war
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
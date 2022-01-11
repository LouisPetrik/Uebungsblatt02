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
        
   

        HttpSession session = request.getSession();
        ArrayList<Kunde> kundenliste = (ArrayList<Kunde>) session.getAttribute("bank.kundenliste"); 

        if (kundenliste != null) {
        	if (Validierung.hasEmail(kundenliste, email)) {
                for (Kunde kunde : kundenliste) {
                    if (kunde.email.equals(email) && kunde.passwort.equals(passwort)) {
                        session.setAttribute("kunde", kunde);
                        request.getRequestDispatcher("konto.jsp").forward(request, response);
                        return;
                    } 
                }	
            }

        	System.out.println("Email oder passwort ist falsch"); 
            request.setAttribute("fehlertyp", "E-Mail oder Passwort ist falsch");
            request.getRequestDispatcher("login.jsp").forward(request, response); 

        // für den Fall, dass es überhaupt Kunden in der DB gibt
        } else {
            System.out.println("Keine Kunden registriert"); 
            request.setAttribute("fehlertyp", "Es gibt keine registrierten Kunden"); 
            request.getRequestDispatcher("login.jsp").forward(request, response); 
        }
    }
}
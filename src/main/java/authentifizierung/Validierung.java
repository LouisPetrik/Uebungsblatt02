package authentifizierung;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import banking.Kunde;

// Diese Klasse dient dazu, Nutzereingaben in Formularen zu valideren.


// die klasse geht über alle bedingungen, fügt die erfüllten einem
// array hinzu, und wenn etwas fehlt wird geschmissen, was fehlt. Außerdem werden die existierenden werte
// dann an das template zurückgegeben, also wenn z. B. vorname fehlt, dass nachname, email und co. ausgefüllt bleiben.
// Sollte wiederverwendbar sein, damit sie für die 2 forms: Login und Registrierung genutzt werden können.


public class Validierung {
    public static boolean hasEmail(ArrayList<Kunde> kundenliste, String email) {
        for (Kunde kunde : kundenliste) {
            if (kunde.email.equals(email)) {
                System.out.println("Mail wurde bereits benutzt!");
                return true;
            }
        }

        return false;
    }

    public static boolean sindPasswoerterGleich(String passwort, String passwortBestaetigung) {
        return passwort.equals(passwortBestaetigung);
    }
}

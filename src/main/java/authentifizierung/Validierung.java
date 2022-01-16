package authentifizierung;

import java.util.ArrayList;

import database.DatabaseKunden;

// Diese Klasse dient dazu, Nutzereingaben in Formularen zu valideren.
public class Validierung {
    public static boolean hasEmail(String email) {
    	ArrayList<String> emails = DatabaseKunden.getKundenMails();
    	
        for (String e : emails) {
            if (e.equals(email)) {
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

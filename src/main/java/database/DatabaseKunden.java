package database;

//alles nötige für SQL: 
import java.sql.SQLException;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import banking.Kunde; 
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


// Diese klasse handeld alles was in der Datenbank mit den kunden zu tun hat - hinzufügen, löschen etc. 


public class DatabaseKunden {
    private static Connection con = null; 
    
    
    // Bekommt kunden objekt übergeben, und fügt die einträge in die datnebank hinzu 
    /* 
     * Aktuell führt die funktion zwei seperate SQL-inserts durch, einmal in kunde-tabelle und einmal in passwort-tabelle 
     * Sollte auf zwei anfragen in einem try-catch block geändert werden. 
     */
    public static boolean fuegeKundeHinzu(Kunde kunde) {
    	boolean erfolg = false; 
    	
      	try {
    		con = DatabaseConnection.getConnection(); 
    		PreparedStatement pstmt = con.prepareStatement("INSERT INTO kunde (email, vorname, nachname, alter, bank, agb, newsletter) VALUES ("
    				+ "?," // email 1
    				+ "?," // vorname 2
    				+ "?," // nachname 3
    				+ "?," // alter 4
    				+ "?," // bank 5
    				+ "?," // agb 6
    				+ "?" // newsletter 7 
    				+ ")"); 
    		
    	

    		pstmt.setString(1, kunde.email);
    		pstmt.setString(2, kunde.vorname);
    		pstmt.setString(3, kunde.nachname); 
    		pstmt.setInt(4, kunde.alter);
    		pstmt.setString(5, kunde.bankinstitut);
    		// wir nehmen erstmal an, dass der kunde immer AGB akzeptiert hat 
    		pstmt.setBoolean(6, true);
    		pstmt.setBoolean(7, kunde.newsletter);
    		
    		int zeilen = pstmt.executeUpdate(); 
    		
    		System.out.println("Zeilen aus DatabaseKunden " + zeilen); 
    		
    		if (zeilen > 0) {
    			erfolg = true; 
    		}
    		
    	} catch (SQLException e) {
    		System.out.println("Fehler beim speichern des neuen kundens"); 
    		System.out.println(e); 
    	} finally {
    		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schließen"); 
    			System.out.println(e); 
    		}
    	}
      	
      	// nun muss die kundenid des neu erstellen kundens aus der kunde tabelle geholt werden, damit sie als foreign key in passwort tabelle 
      	// eingesetzt werden kann, da das passwort ja in eine eigene tabelle soll
      	// muss schon initialisiert sein, weil sich die andere queue sonst beschwert
      	int kundenid = 0; 
      	
      	try {
      		con = DatabaseConnection.getConnection(); 
      		PreparedStatement pstmt = con.prepareStatement("SELECT kundenid FROM kunde WHERE email = ?;"); 
      		pstmt.setString(1, kunde.email);
      		
      		ResultSet rs = pstmt.executeQuery(); 
      		
      		
      		
      		while (rs.next()) {
        		kundenid = rs.getInt("kundenid"); 
    		}
      	} catch (SQLException e) {
      		System.out.println("Fehler beim Extrahieren der Kundenid des erstellten Kunden"); 
      		System.out.println(e); 
      	} finally {
      		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schließen"); 
    			System.out.println(e); 
    		}
      	}
      	
      	
      	
      	try {
      		con = DatabaseConnection.getConnection(); 
    		PreparedStatement pstmt = con.prepareStatement("INSERT INTO passwort VALUES ("
    				+ "?," // kundenid 1
    				+ "?" // passwort 2
    				+ ")"); 
    		
    		pstmt.setInt(1, kundenid);
    		pstmt.setString(2, kunde.passwort);
    		
    		pstmt.executeUpdate(); 
   
      	} catch (SQLException e) {
    		System.out.println("Fehler beim speichern des passwortes"); 
    		System.out.println(e); 
    	} finally {
    		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schließen"); 
    			System.out.println(e); 
    		}
    	}
    	
    	
    	return erfolg; 
    }
    
    
    /* 
     * Wenn diese Funktion ein Kundenobjekt zurück gibt, existiert der nutzer account und die kombination 
     * aus passwort und email ist richtig - der nutzer wird also angemeldet, seine daten in der session gespeichert. 
     * Die Funktion gibt das gesamte Kundenobjekt mit den Daten wie Newsletter, Mail, Namen etc zurück, da dies in der 
     * Aufgabe so verlangt wird, und auf vor, nachname etc. zugegriffen werden muss. 
     * Funktioniert über einen inner-join. 
     */
    
    
    public static Kunde kundeEinlogggen(String email, String passwort) {
    	boolean datenbankErfolg = false; 
    	
    	Kunde eingeloggterKunde = new Kunde("", "", 0, "", "", "", false); 
    	
    	String fehlermeldung = ""; 
    	
    	try {
    		con = DatabaseConnection.getConnection(); 
    		// Den Kunden samt passwort ausgeben, der über die angegebene email verfügt. 
    		PreparedStatement pstmt = con.prepareStatement(""
    				+ "SELECT kundenid, vorname, nachname, email, alter, agb, newsletter, bank, passwort FROM kunde INNER JOIN passwort USING (kundenid) WHERE email = ?;"
    				+ "");
    		
    		pstmt.setString(1, email);
    		
    		ResultSet rs = pstmt.executeQuery(); 
    		
    		// Passwort und Email, das in der DB gefunden werden insofern über die SQL abfrage 
    		// ein tatsächlich existierender nutzer gefunden wird. Alle auf leeren string / 0 gesetzt, weil sich eclipse sonst beschwert. 
    		String abgefragtesPasswort = ""; 
    		String abgefragteEmail = ""; 
    		
    		Integer kundenid = 0; 
    		String vorname = ""; 
    		String nachname = "";
    		Integer alter = 0; 
    		String bankinstitut = "";  
    		boolean newsletter = false; 
    		
    		// true, insofern ein account wie oben beschrieben in der DB gefunden wurde 
    		boolean accountGefunden = false; 
    		
    		
    		while (rs.next()) {
        		abgefragtesPasswort = rs.getString("passwort"); 
        		abgefragteEmail = rs.getString("email");  
        		
        		// alle anderen daten aus der datenbank übernehmen
        		kundenid = rs.getInt("kundenid"); 
        		vorname = rs.getString("vorname");
        		nachname = rs.getString("nachname"); 
        		alter = rs.getInt("alter"); 
        		bankinstitut = rs.getString("bank"); 
        		newsletter = rs.getBoolean("newsletter"); 
        		
        		
        		
        		
    			System.out.println("Passwort aus der db " + abgefragtesPasswort); 
        		System.out.println("Email aus der db " + abgefragteEmail); 
        		
        		// alles andere ausgeben: 
        		System.out.println(rs.getString("vorname") + rs.getString("nachname") + rs.getInt("alter")); 
        		
        		accountGefunden = true; 
    		}
    	
    		// insofern also ein account mit der email gefunden wurde, und der nutzer auch das richtige passwort 
    		// für vermeintlich seinen account eingeben hat 
    		if (accountGefunden && passwort.equals(abgefragtesPasswort)) {
    			System.out.println("Nutzer " + abgefragteEmail + " kann über DB eingeloggt werden");
    			
    			// erstellen eines nutzer-objektes 
    			eingeloggterKunde = new Kunde(vorname, nachname, alter, abgefragteEmail, bankinstitut, "", newsletter); 
    			// DIe kundenid, die ebenfalls aus der Datenbank geholt wurde setzen. Das geschieht bisher nicht über den konstruktor, siehe 
    			// Kunde.java warum. 
    			eingeloggterKunde.setKundenid(kundenid);
    			
    		// irgendwas ist schiefgelaufen: 
    		} else {
    			
    			// insofern die SQL abfrage keinen Account findet
    			if (!accountGefunden) {
    				fehlermeldung = "Es wurde kein Account mit dieser E-Mail gefunden"; 
    			}
    			
    			// Insofern zwar ein Account gefunden wurde, aber nicht das richitge passwort eingegeben wurde: 
    			if (accountGefunden && !passwort.equals(abgefragtesPasswort)) {
    				fehlermeldung = "Konto existiert zwar, aber das eingegebene Passwort ist falsch"; 
    			}
    			
  
    			eingeloggterKunde.fehlermeldung = fehlermeldung; 
    		}
    
    		
    	} catch (SQLException e) {
    		System.out.println("Fehler"); 
    		System.out.println(e); 
    	} finally {
    		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schließen"); 
    			System.out.println(e); 
    		}
    	}
    	

   
    	return eingeloggterKunde; 
    
    } 
    
    // Eigentlich fordert SQL an, dass das E-Mail feld unique ist - aber sicherhaltshalber kann man trotzdem 
    // abfragen, ob die email schon vorhanden ist. 
    public static boolean emailSchonVergeben(String email) {
    	return false; 
    }

}

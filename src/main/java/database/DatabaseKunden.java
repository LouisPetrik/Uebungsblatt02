package database;

//alles nˆtige f¸r SQL: 
import java.sql.SQLException;
import java.util.UUID;
import java.sql.Connection;
import java.sql.PreparedStatement; 
import banking.Kunde; 
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

// Diese klasse handeld alles was in der Datenbank mit den kunden zu tun hat - hinzuf¸gen, lˆschen etc. 


public class DatabaseKunden {
    private static Connection con = null; 
    
    
    // Bekommt kunden objekt ¸bergeben, und f¸gt die eintr‰ge in die datnebank hinzu 
    public static boolean fuegeKundeHinzu(Kunde kunde) {
    	boolean erfolg = false; 
    	
 
      	try {
    		con = DatabaseConnection.getConnection(); 
    		PreparedStatement pstmt = con.prepareStatement("INSERT INTO kunde VALUES ("
    				+ "?," // kundenid 1
    				+ "?," // email 2
    				+ "?," // vorname 3
    				+ "?," // nachname 4
    				+ "?," // alter 5
    				+ "?," // bank 6
    				+ "?," // agb 7
    				+ "?" // newsletter 8 
    				+ ")"); 
    		
    		int randomNum = ThreadLocalRandom.current().nextInt(0, 100000 + 1);
    		pstmt.setInt(1, randomNum);
    		pstmt.setString(2, kunde.email);
    		pstmt.setString(3, kunde.vorname);
    		pstmt.setString(4, kunde.nachname); 
    		pstmt.setInt(5, kunde.alter);
    		pstmt.setString(6, kunde.bankinstitut);
    		// wir nehmen erstmal an, dass der kunde immer AGB akzeptiert hat 
    		pstmt.setBoolean(7, true);
    		pstmt.setBoolean(8, kunde.newsletter);
    		
    		int zeilen = pstmt.executeUpdate(); 
    		
    		if (zeilen > 0) {
    			erfolg = true; 
    		}
    		
    	} catch (SQLException e) {
    		System.out.println("Fehler"); 
    		System.out.println(e); 
    	} finally {
    		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schlieﬂen"); 
    			System.out.println(e); 
    		}
    	}
    	
    	
    	return erfolg; 
    }
    
    // Eigentlich fordert SQL an, dass das E-Mail feld unique ist - aber sicherhaltshalber kann man trotzdem 
    // abfragen, ob die email schon vorhanden ist. 
    public static boolean emailSchonVergeben(String email) {
    	return false; 
    }

}

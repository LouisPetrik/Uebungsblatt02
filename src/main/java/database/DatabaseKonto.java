package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import banking.Kunde;
import banking.Konto; 

public class DatabaseKonto {
	
    private static Connection con = null; 
    
    public static boolean fuegeKontoHinzu(Konto konto) {
    
        
        
    	boolean erfolg = false; 
    	
    	// Die kunden ID, die zuf‰llig generiert wird, und f¸r beide tabellen gebraucht wird. 
    	int randomKundenID = ThreadLocalRandom.current().nextInt(0, 100000 + 1);
    	
 
      	try {
      		// die query f¸gt nicht die kontoid selbst ein, da diese nur 
    		con = DatabaseConnection.getConnection(); 
    		PreparedStatement pstmt = con.prepareStatement("INSERT INTO konto VALUES ("
    				+ "?," // kontoid 1
    				+ "?," // kundenemail 2
    				+ "?," // name 3
    				+ "?," // kontostand 4
    				+ "?," // alter 5
    				+ "?," // bank 6
    				+ "?," // agb 7
    				+ "?" // newsletter 8 
    				+ ")"); 
    		
    	
    		pstmt.setInt(1, randomKundenID);
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
    		System.out.println("Fehler beim speichern des neuen kundens"); 
    		System.out.println(e); 
    	} finally {
    		try {
    			con.close(); 
    		} catch (SQLException e){
    			System.out.println("Kann nicht schlieﬂen"); 
    			System.out.println(e); 
    		}
    	}
      	
      	try {
      		con = DatabaseConnection.getConnection(); 
    		PreparedStatement pstmt = con.prepareStatement("INSERT INTO passwort VALUES ("
    				+ "?," // kundenid 1
    				+ "?" // passwort 2
    				+ ")"); 
    		
    		pstmt.setInt(1, randomKundenID);
    		pstmt.setString(2, kunde.passwort);
    		
    		pstmt.executeUpdate(); 
 
    		
   
      	} catch (SQLException e) {
    		System.out.println("Fehler beim speichern des passwortes"); 
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
}

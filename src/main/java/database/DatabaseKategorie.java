package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 


// klasse zum handeln des erstellens und filtern nach kategorien 
public class DatabaseKategorie {
	
	
	private static Connection con;
	
	
	public static void kategorieErstellen(String kategorie, ArrayList<String> schlagwoerter) {
		System.out.println("Kategorie " + kategorie + " wird angelegt");
		
	      try {
	            // die query fÃ¼gt nicht die kontoid selbst ein, da diese als SERIAL in der DB automatisch gesetzt wird
	            con = DatabaseConnection.getConnection();
	            
	            // jede kategorie hat mindestens ein schlagwort, die tabelle sieht vor, dass immer nur paare als zeilen gespeichert werden 
	            // daher wird für jedes schlagwort eine zeile angelegt, als name die kategorie. 
	            for (String schlagwort : schlagwoerter) {
	                PreparedStatement pstmt = con.prepareStatement("INSERT INTO kategorie VALUES ("
		                    + "?,"  // name  1
		                    + "?"  // schlagwort  2
		                    + ")");

		            pstmt.setString(1, kategorie);
		            pstmt.setString(2, schlagwort);
		            
		            pstmt.executeUpdate();

	            }
	            	
	    
	        } catch (SQLException e) {
	            System.out.println("Fehler beim anlegen der Kategorie");
	            System.out.println(e);
	        }

	        try {
	            con.close();
	        } catch (SQLException e){
	            System.out.println("Kann die DB verbindung nicht schließen ");
	            System.out.println(e);
	        }

	    }
	
	// funktion, die alle kategorien die verfügbar sind, zurückgibt. SELECT DISTINCT name FROM kategorie; 
	public static ArrayList<String> kategorienAusgeben() {
		ArrayList<String> kategorien = new ArrayList<>(); 
		
		 try {
	      
	            con = DatabaseConnection.getConnection();
	            
	            PreparedStatement pstmt = con.prepareStatement("SELECT DISTINCT name FROM kategorie");
	   	            
	            ResultSet rs = pstmt.executeQuery();
	            
	            while (rs.next()) {
	            	kategorien.add(rs.getString("name")); 
	            }
	            	
	    
	        } catch (SQLException e) {
	            System.out.println("Fehler beim Ausgeben aller Kategorien");
	            System.out.println(e);
	        } finally {
	        	 try {
	 	            con.close();
	 	        } catch (SQLException e){
	 	            System.out.println("Kann die DB verbindung nicht schließen ");
	 	            System.out.println(e);
	 	        }
	        }
		 
		 return kategorien;
	       
	    }
	
	// Funktion, die alle Schlagwoerter einer Kategorie in eine ArrayList speichert und returnt 
	public static ArrayList<String> schlagwoerterFinden(String kategorie) {
		ArrayList<String> schlagwoerter = new ArrayList<>(); 
		
		try {
			con = DatabaseConnection.getConnection(); 
			
			PreparedStatement pstmt = con.prepareStatement("SELECT schlagwort FROM kategorie WHERE name = ?"); 
			
			pstmt.setString(1, kategorie);
			
		    ResultSet rs = pstmt.executeQuery();
		     
	        while (rs.next()) {
	        	schlagwoerter.add(rs.getString("schlagwort")); 
	        }
		
		} catch (SQLException e) {
			System.out.println("Fehler beim Ausgeben der Schlagwörter einer Kategorie"); 
			System.out.println(e); 
		} finally {
			try {
				con.close();
	 	   	} catch (SQLException e){
	 	   		System.out.println("Kann die DB verbindung nicht schließen ");
	 	   		System.out.println(e);
	 	    }
		}
		
		return schlagwoerter; 
	}
	
	// Funktion, die alle Transaktionen einer Kategorie (durch zutreffen eines Schlagwortes) ausgibt 
	public static ArrayList<String> filterTransaktionen(ArrayList<String> schlagwoerter) {
		
		// wir erstellen eine Array liste, die ersten Felder sind die einzelnen Transaktionen, zuletzt kommt die Summe: 
		ArrayList<String> transaktionsFazit = new ArrayList<>(); 
		
		// Gesamtkosten / Einnahmen durch die Transaktionen der Kategorie 
		double summeTransaktionen = 0; 
		
		
		
		System.out.println("An filterTransaktionen übergebene Schlagwoerter");
		for (String schlagwort : schlagwoerter) {
			System.out.println(schlagwort); 
		}
		try {
				con = DatabaseConnection.getConnection(); 
				
			// vorbereiten der query 
		      String query = "SELECT * FROM posten WHERE "; 

		      for (int i = 0; i < schlagwoerter.size(); i++) {
		         // falls es das letzte schlagwort in der query seien wird:; 
		         if (i == schlagwoerter.size() - 1) {
		            query = query.concat("verwendungszweck LIKE ?"); 
		         } else {
		            query = query.concat("verwendungszweck LIKE ? OR "); 
		         }
		      }
			
			PreparedStatement pstmt = con.prepareStatement(query); 
			
			System.out.println(query); 
			System.out.println(pstmt.toString()); 
			
			// nun werden in das preparedStatement die eigentlichen schlagworter eingefügt
			
			for (int i = 1; i <= schlagwoerter.size(); i++) {
				 pstmt.setString(i, "%" + schlagwoerter.get(i - 1) + "%");
			}
			
			System.out.println(pstmt.toString()); 
			
			
			
		    ResultSet rs = pstmt.executeQuery();
		     
		    System.out.println("Was wir wissen wollen: "); 
		    int anzahlZeilen = 0; 
		    
	        while (rs.next()) {
	        	anzahlZeilen += 1; 
	        	String verwendungszweck = rs.getString("verwendungszweck"); 
	        	double betrag = rs.getDouble("betrag"); 
	        	
	        	System.out.println(verwendungszweck); 
	        	System.out.println(betrag); 
	        	
	        	summeTransaktionen += betrag; 
	        	
	        	transaktionsFazit.add(verwendungszweck + " " + betrag + " Euro"); 
	        	
	        }
	        
	        // als letztes element in der arraylist die summe der transaktionen adden 
	        
	        transaktionsFazit.add(summeTransaktionen + " Euro"); 
	        
	        System.out.println("Es wurden " + anzahlZeilen + " Transaktionen gefunden"); 
			
		} catch (SQLException e) {
			System.out.println("Fehler beim Filtern aller Transaktionen nach Schlagwortern einer Kategorie"); 
			System.out.println(e); 
		} finally {
			try {
				con.close(); 
			} catch (SQLException e){
	 	   		System.out.println("Kann die DB verbindung nicht schließen ");
	 	   		System.out.println(e);
	 	    }
		}
		
		return transaktionsFazit; 
		

	
		// in der Servlet nicht den array in der session speichern, sondern als request object weitergeben. 
	}
	
}




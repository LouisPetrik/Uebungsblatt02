package database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import banking.Kunde;

public class DatabaseKunden {
	// gibt null zurück bei fehlern
    public static Kunde addKunde(String vorname, String nachname,
    		int alter, String email, String bankinstitut, String passwort, boolean newsletter) {
    	boolean erfolg = false;

    	try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO kunde (email, vorname, nachname, alter, bank, agb, newsletter) VALUES ("
                    + "?," // email 1
                    + "?," // vorname 2
                    + "?," // nachname 3
                    + "?," // alter 4
                    + "?," // bank 5
                    + "?," // agb 6
                    + "?" // newsletter 7
                    + ")");

            pstmt.setString(1, email);
            pstmt.setString(2, vorname);
            pstmt.setString(3, nachname);
            pstmt.setInt(4, alter);
            pstmt.setString(5, bankinstitut);
            pstmt.setBoolean(6, true); 		// Kunde muss AGB akzeptiert haben
            pstmt.setBoolean(7, newsletter);

            if (pstmt.executeUpdate() > 0) {
                erfolg = true;
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim speichern des neuen kundens");
            System.out.println(e);
        } finally {
        	DatabaseConnection.closeConnection();
        }

        if (!erfolg) {
        	System.out.println("Kunde konnte nicht in die Tabelle hinzugefügt werden");
        	return null;
        }
        

        // kundenid wird für die tabelle passwort und für das erstellen eines kunden benötigt
        int kundenid = -1;
        try {
        	Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("SELECT kundenid FROM kunde WHERE email = ?;");
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                kundenid = rs.getInt("kundenid");
            } else {
            	System.out.println("Es wurde keine kundenid gefunden");
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Extrahieren der Kundenid des erstellten Kunden");
            System.out.println(e);
        } finally {
        	DatabaseConnection.closeConnection();
        }

        if (kundenid == -1) {
        	System.out.println("Kundeid wurde nicht gefunden");
        	return null;
        }
        
        try {
        	Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO passwort VALUES ("
                    + "?," 	// kundenid 1
                    + "?" 	// passwort 2
                    + ")");

            pstmt.setInt(1, kundenid);
            pstmt.setString(2, passwort);

            if (pstmt.executeUpdate() > 0) {
                erfolg = true;
            } else {
            	erfolg = false;
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim speichern des passwortes");
            System.out.println(e);
        } finally {
        	DatabaseConnection.closeConnection();
        }

        if (!erfolg) {
        	System.out.println("Passwort konnte nicht hinzugefügt werden");
        	return null;
        }

        return new Kunde(kundenid, vorname, nachname, alter, email, bankinstitut, passwort, newsletter);
    }

    public static ArrayList<String> getKundenMails() {
    	ArrayList<String> res = new ArrayList<String>();
    	
    	try {
    		Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("SELECT email FROM kunde");
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
            	res.add(rs.getString("email"));
            }
            
    	} catch (SQLException e) {
            System.out.println("Fehler");
            System.out.println(e);
        } finally {
        	DatabaseConnection.closeConnection();
        }
    	
    	return res;
    }
    
    public static Kunde getKunde(String email) {
    	Kunde res = null;
    	
    	try {
    		Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("SELECT kundenid, vorname, nachname, email, alter, agb, newsletter, bank, passwort FROM kunde INNER JOIN passwort USING (kundenid) WHERE email = ?;");
            pstmt.setString(1, email);

            ResultSet rs = pstmt.executeQuery();

            String passwort = "";
            Integer kundenid = 0;
            String vorname = "";
            String nachname = "";
            Integer alter = 0;
            String bankinstitut = "";
            boolean newsletter = false;

            if (rs.next()) {
                passwort = rs.getString("passwort");
                kundenid = rs.getInt("kundenid");
                vorname = rs.getString("vorname");
                nachname = rs.getString("nachname");
                alter = rs.getInt("alter");
                bankinstitut = rs.getString("bank");
                newsletter = rs.getBoolean("newsletter");

                res = new Kunde(kundenid, vorname, nachname, alter, email, bankinstitut, passwort, newsletter);
                
                System.out.println("found kunde");
            }

        } catch (SQLException e) {
            System.out.println("Fehler");
            System.out.println(e);
        } finally {
        	DatabaseConnection.closeConnection();
        }
    	
    	return res;
    }
}

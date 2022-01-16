package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.ResultSet;

import banking.Konto;
import banking.Transaktion;


public class DatabaseKonto {
	public static boolean addKonto(String kundenemail, String kontoname) {
    	boolean success = false;

        try {
            // die query fügt nicht die kontoid selbst ein, da diese als SERIAL in der DB automatisch gesetzt wird
        	Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO konto (kundenemail, name, kontostand) VALUES ("
                    + "?,"  // kundenemail  1
                    + "?,"  // name         2
                    + "?"   // kontostand   3
                    + ")");

            pstmt.setString(1, kundenemail);
            pstmt.setString(2, kontoname);
            pstmt.setDouble(3, 0); // jedes Bankkonto ist ersteinmal leer.

            if (pstmt.executeUpdate() > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Erstellen des neuen Kontos");
            System.out.println(e);
        }

        DatabaseConnection.closeConnection();

        return success;
    }

    // -1 = konnte keine kontoid finden
    public static int getKontoId(String kundenemail, String kontoname) {
    	int res = -1;

        try {
        	Connection con = DatabaseConnection.getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT kontoid FROM konto WHERE kundenemail=? AND name=?");
            pstmt.setString(1, kundenemail);
            pstmt.setString(2, kontoname);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                res = rs.getInt("kontoid");
            }

        } catch (SQLException e) {
            System.out.println("Kontostand konnte nicht aktualisiert werden");
            System.out.println(e);
        }

        DatabaseConnection.closeConnection();

        return res;
    }

    public static boolean setKontostand(String kundenemail, String kontoname, double kontostand) {
    	boolean res = false;
    	
    	try {
    		Connection con = DatabaseConnection.getConnection();

            PreparedStatement pstmt = con.prepareStatement("UPDATE konto SET kontostand=? WHERE kundenemail=? AND name=?");
            pstmt.setDouble(1, kontostand);
            pstmt.setString(2, kundenemail);
            pstmt.setString(3, kontoname);

            if (pstmt.executeUpdate() > 0) {
                System.out.println("kontostand wurde aktualisiert");
                res = true;
            }
        } catch (SQLException e) {
            System.out.println("Kontostand konnte nicht aktualisiert werden");
            System.out.println(e);
        }

    	DatabaseConnection.closeConnection();
        
        return res;
    }

    public static boolean updateKonto(Konto konto) {
    	if (!DatabaseKonto.setKontostand(konto.kundenemail, konto.name, konto.getKontostand())) {
    		System.out.println("Kontostand konnte nicht aktualisiert werden");
        	return false;
    	}

        int kontoId = DatabaseKonto.getKontoId(konto.kundenemail, konto.name);
        if (kontoId == -1) {
        	System.out.println("kontoid konnte nicht gefunden werden(email und/oder name ist falsch)");
        	return false;
        }
        
        if (DatabaseKonto.addTxs(kontoId, konto.txs)) {
        	System.out.println("Transaktion konnte nicht hinzugefügt werden");
        	return false;
        }
        
        return true;
    }
    
    public static boolean addTxs(int kontoId, ArrayList<Transaktion> txs) {
    	boolean success = false;

        try {
            // die query fügt nicht die kontoid selbst ein, da diese als SERIAL in der DB automatisch gesetzt wird
        	Connection con = DatabaseConnection.getConnection();

            for (Transaktion tx : txs) {
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO posten (kontoid, betrag, verwendungszweck) VALUES ("
                        + "?,"  // kontoid          1
                        + "?,"  // betrag           2
                        + "?"   // verwendungszweck 3
                        + ")");

                pstmt.setInt(1, kontoId);
                pstmt.setDouble(2, tx.betrag);
                pstmt.setString(3, tx.zweck);

                if (pstmt.executeUpdate() > 0) {
                    success = true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim eintragen der Transaktionen ins Konto");
            System.out.println(e);
        }

        DatabaseConnection.closeConnection();

        return success;
    }
    
    public static ArrayList<Transaktion> getTxs(Konto konto) {
    	ArrayList<Transaktion> res = new ArrayList<Transaktion>();
    	
    	try {
    		Connection con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("SELECT betrag, verwendungszweck FROM posten WHERE kontoid=?");
            pstmt.setInt(1, konto.kontoid);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
            	Transaktion tx = new Transaktion(rs.getString("verwendungszweck"), rs.getDouble("betrag"));
            	res.add(tx);
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim eintragen der Transaktionen ins Konto");
            System.out.println(e);
        }

    	DatabaseConnection.closeConnection();

        return res;
    }

    public static ArrayList<Konto> getKonten(String kundenemail) {
    	ArrayList<Konto> kontenliste = new ArrayList<>();

        try {
        	Connection con = DatabaseConnection.getConnection();

            PreparedStatement pstmt = con.prepareStatement("SELECT * FROM konto WHERE kundenemail = ?;");
            pstmt.setString(1, kundenemail);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int kontoid = rs.getInt("kontoid");
                String name = rs.getString("name");
                Double kontostand = rs.getDouble("kontostand");

                Konto konto = new Konto(kontoid, name, kundenemail, kontostand);

                kontenliste.add(konto);
            }
        } catch (SQLException e) {
            System.out.println("Fehler beim Ausgeben aller Konten des Kunden");
            System.out.println(e);
        }

        DatabaseConnection.closeConnection();

        return kontenliste;
    }
}

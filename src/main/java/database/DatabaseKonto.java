package database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import banking.Kunde;
import banking.Konto;


public class DatabaseKonto {

    private static Connection con = null;

    public static boolean fuegeKontoHinzu(String kundenemail, String kontoname) {

        boolean erfolg = false;


        try {
            // die query fügt nicht die kontoid selbst ein, da diese als SERIAL in der DB automatisch gesetzt wird
            con = DatabaseConnection.getConnection();
            PreparedStatement pstmt = con.prepareStatement("INSERT INTO konto (kundenemail, name, kontostand) VALUES ("
                    + "?," // kundenemail 1
                    + "?," // name 2
                    + "?" // kontostand 3
                    + ")");

            pstmt.setString(1, kundenemail);
            pstmt.setString(2, kontoname);

            // jedes Bankkonto ist ersteinmal leer.
            pstmt.setDouble(3, 0);

            int zeilen = pstmt.executeUpdate();

            if (zeilen > 0) {
                erfolg = true;
            }

        } catch (SQLException e) {
            System.out.println("Fehler beim Erstellen des neuen Kontos");
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

    public static ArrayList<Konto> listeDerKonten(String kundenemail) {

        // Die Liste der Konten des Benutzers, die aus Konto-Objekten besteht.
        ArrayList<Konto> kontenliste = new ArrayList<>();


        try {
            con = DatabaseConnection.getConnection();
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
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Kann nicht schließen");
                System.out.println(e);
            }
        }

        return kontenliste;
    }
}

package banking;

import java.util.ArrayList;
import java.util.Scanner;

public class Konto {
    public final String name;
    public final String kundenemail;
    public final Integer kontoid;
    private double kontostand;

    public ArrayList<Transaktion> txs = new ArrayList<>();

    public Konto(Integer kontoid, String name, String kundenemail, Double kontostand) {
        this.kontoid = kontoid;
        this.name = name;
        this.kundenemail = kundenemail;
        this.kontostand = kontostand;
    }


    // Wenn ein Feld ein String mit einem Komma(";") enthält wird durch split dieses Feld in
    // zwei Felder (oder mehr bei mehreren Kommata) augeteilt.
    // Diese methode soll diese Felder wieder zusammenfügen
    private String[] escapeStrings(String fields[]) {
        int firstIdx = -1;
        int lastIdx = -1;

        for (int i = 0; i < fields.length; i++) {
            if (fields[i].startsWith("\"")) {
                firstIdx = i;
            }

            if (fields[i].endsWith("\"")) {
                lastIdx = i;
            }
        }

        if (firstIdx != -1 && lastIdx != -1) {
            String tmp[] = new String[fields.length - lastIdx + firstIdx];

            // tmp mit allen feldern füllen
            int fi = 0;
            for (int i = 0; i < tmp.length; i++) {
                if(fi == firstIdx+1)
                    fi = lastIdx+1;

                tmp[i] = fields[fi];
                fi++;
            }

            // concat fields
            for (int i = firstIdx + 1; i <= lastIdx; i++) {
                tmp[firstIdx] += ";" + fields[i];
            }

            fields = tmp;
        }

        return fields;
    }

    public void loadCSV(String csvFile) {
        Scanner scanner = new Scanner(csvFile);

        // um den header zu überspringen
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String txString = scanner.nextLine();

            String fields[] = txString.split(";");

            fields = escapeStrings(fields);

            if (fields.length != 17) {
                System.out.println("CSV hat keine gültiges Format (sollte 17 Felder haben)!");
                return;
            }

            double f14 = 0.f;
            fields[14] = fields[14].replace(',', '.');
            try {
                 f14 = Double.parseDouble(fields[14]);
            } catch (NumberFormatException e) {
                System.out.println("Feld 15 sollte ein double/float sein");
                return;
            }

            txs.add(new Transaktion(fields[4], f14));
        }
        
        setKontostand();

        scanner.close();
    }

    public double getKontostand() {
    	return kontostand;
    }
    
    public void setKontostand() {
    	kontostand = 0.0;

        for (Transaktion tx : txs) {
        	kontostand += tx.betrag;
        }
    }

    public boolean hasTxs() {
        return !txs.isEmpty();
    }

    public String txsAsHTML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>");
        for (Transaktion tx : txs) {
            sb.append("<tr>" + tx.asHTML() + "<tr/>");
        }
        sb.append("</table>");

        return sb.toString();
    }
}

package banking;

import java.util.UUID;

import java.util.ArrayList;
import java.util.Scanner;

import banking.Transaktion;

public class Konto {
    public final String name;
    public final String email;

    private final String ID;
    private ArrayList<Transaktion> txs = new ArrayList<>();

    public Konto(String name, String email) {
        this.name = name;
        this.email = email;

        // statt über den Konstrukor einen Wert für die ID entgegenzunehmen wird bei
        // jeder erstellen Instanz eines Kontos eine einzigartige ID generiert.
        this.ID = UUID.randomUUID().toString();
    }
    
    // Wenn ein Feld ein String mit einem Komma(",") enthält wird durch split dieses Feld in
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
		
		if (firstIdx != -1 && lastIdx != -1)
		{
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
				tmp[firstIdx] += "," + fields[i];
			}
			
			for (int i = 0; i < tmp.length; i++) {
				System.out.println(tmp[i]);
			}
				
			fields = tmp;
		}
		
		return fields;
    }
    
    public void loadCSV(String csvFile) {
    	System.out.println("loadCSV!");
    	
        	Scanner scanner = new Scanner(csvFile);
        	
        	// um den header zu überspringen
        	if (scanner.hasNextLine()) {        		
        		scanner.nextLine();
        	}
       	
        	while (scanner.hasNextLine()) {
        		String txString = scanner.nextLine();
        		
        		String fields[] = txString.split(",");
        		
        		// siehe Kommentar über methoden definition
        		// siehe erste Zeile der csv (grund für diese methode)
        		fields = escapeStrings(fields);
                		
        		if (fields.length != 17) {
        			System.out.println("CSV hat keine gültiges Format (sollte 17 Felder haben)!");
        			return;
        		}
        		
        		float f14 = 0.f;
        		try {
        			 f14 = Float.parseFloat(fields[14]);
        		} catch (NumberFormatException e) {
        			System.out.println("Feld 15 sollte ein float sein");
        			return;
        		}
        			
        		txs.add(new Transaktion(fields[0], fields[5], fields[3], fields[4], fields[12], f14, fields[15]));
        	}
        	
        	scanner.close();
    }
    
    public float getKontostand() {
    	float res = 0;
    	
    	for (Transaktion tx : txs) {
    		res += tx.betrag;
    	}
    	
    	return res;
    }
    
    public boolean hasTxs() {
    	return !txs.isEmpty();
    }
    
    public String txsAsHTML() {
    	StringBuilder sb = new StringBuilder();
    	
    	for (Transaktion tx : txs) {
    		sb.append(tx.asHTML() + "<br/>");
    	}
    	
    	return sb.toString();
    }
}
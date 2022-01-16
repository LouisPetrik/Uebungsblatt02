package banking;

import java.util.ArrayList;

public class Kunde {
    private final int kundenid;
    private String vorname;
    private String nachname;
    private int alter;
    private String email;
    private String bankinstitut;
    private String passwort;
    private boolean newsletter;

    public ArrayList<Konto> kontenliste = new ArrayList<Konto>();

    public Kunde(int kundenid, String vorname, String nachname, Integer alter, String email, String bankinstitut, String passwort, Boolean newsletter) {
        this.kundenid = kundenid;
    	this.vorname = vorname;
        this.nachname = nachname;
        this.alter = alter;
        this.email = email;
        this.bankinstitut = bankinstitut;
        this.passwort = passwort;
        this.newsletter = newsletter;
    }

    public String kontenAsHTML() {
        if (!kontenliste.isEmpty()) {
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < kontenliste.size(); i++ ) {
                sb.append("<option value='" + i + "'>" + kontenliste.get(i).name + "</option>");
            }

            return sb.toString();
        } else {
            System.out.println("Beim Einloggen: Der Kunde hat noch keine Konten.");
            return ""; // für KontoServlet -> kein form erstellen sondern nachricht
        }
    }

    public Integer getKundenid() {
        return this.kundenid;
    }

    public String getVorname() {
        return this.vorname;
    }

    public String getNachname() {
        return this.nachname;
    }

    public String getEmail() {
        return this.email;
    }

    public Integer getAlter() {
        return this.alter;
    }

    public String getBankinstitut() {
        return this.bankinstitut;
    }

    public String getPasswort() {
        return this.passwort;
    }

    public Boolean getNewsletter() {
        return this.newsletter;
    }
}

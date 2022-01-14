package banking;

import banking.Konto;
import java.util.ArrayList;

public class Kunde {
    public Integer kundenid;
    public String vorname;
    public String nachname;
    public Integer alter;
    public String email;
    public String bankinstitut;
    public String passwort;
    public Boolean newsletter;

    // Insofern es ein Problem mit dem Kundenobjekt gibt, z. B. Kein Kunde gefunden wird, wird hier
    // die fehlermeldung gespeichert.
    public String fehlermeldung;

    // Ein nutzer kann mehrere Konten haben, die als Liste von Konten-Objekten gespeichert werden.
    public ArrayList<Konto> kontenliste = new ArrayList<>();


    public Kunde(String vorname, String nachname, Integer alter, String email, String bankinstitut, String passwort, Boolean newsletter) {
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

    /* Hier folgen sämtliche Getter-Methoden für das Kundenobjekt.
    Sie helfen dabei, dass Objekt-Werte in den Templates genutzt werden können und
    schützen vor versehentlichen mutaten beim Zugriff auf die Attribute via .-Syntax.
    */

    // Der setter für die kundenid. Kundenid ist nicht im konstruktur, damit man dynamischer die ID setzen kann.
    // Z.B weil die ID von der DB abhängt und automatisch vergeben wird, daher kann sie nicht schon über den konstruktor festgelegt werden.
    public void setKundenid(Integer kundenid) {
        this.kundenid = kundenid;
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

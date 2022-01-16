package banking;

public class Transaktion {
    // nur zweck und betrag, da die Tabelle in der Datenbank
    // auch nur diese felder besitzt
    public final String zweck;
    public final double betrag;

    public Transaktion(String zweck, double betrag) {
        this.zweck = zweck;
        this.betrag = betrag;
    }

    public String asHTML() {
        return "<td>" + betrag + "</td> <td>" + zweck + "</td>";
    }
}

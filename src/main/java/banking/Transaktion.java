package banking;

public class Transaktion {
    public final String payer;
    public final String payee_id;
    public final String text;
    public final String zweck;
    public final String payee_iban;
    public final float betrag;
    public final String waehrung;
    
    public Transaktion(String payer, String payee_id, String text,
    		String zweck, String payee_iban, float betrag, String waehrung) {
    	this.payer = payer;
    	this.payee_id = payee_id;
    	this.text = text;
    	this.zweck = zweck;
    	this.payee_iban = payee_iban;
    	this.betrag = betrag;
    	this.waehrung = waehrung;
    }
    
    public String asHTML() {
    	return payer + " " + betrag + " " + waehrung + " " + zweck;
    }
}
/* hier ist der gesamte code für die DB drinne
 * newsletter feld sollte bei default vielleicht einfach false sein */

CREATE TABLE kunde
(
    kundenid integer NOT NULL,
    email "char"[] NOT NULL UNIQUE,
    vorname text NOT NULL,
    nachname text NOT NULL,
    alter integer NOT NULL,
    bank text NOT NULL,
    agb boolean NOT NULL,
    newsletter boolean,
    PRIMARY KEY (kundenid)
);


CREATE TABLE passwort (
	kundenid integer NOT NULL REFERENCES kunde (kundenid), 
	passwort "char"[] NOT NULL
);

CREATE TABLE konto (
	kontoid integer NOT NULL UNIQUE, 
	kundenemail "char"[] NOT NULL REFERENCES kunde (email), 
	name "char"[] NOT NULL, 
	kontostand double precision NOT NULL 
);

CREATE TABLE posten ( 
	kontoid integer REFERENCES konto (kontoid), 
	betrag double precision NOT NULL, 
	verwendungszweck text
);

CREATE TABLE kategorie (
	name text, 
	schlagwort text
);
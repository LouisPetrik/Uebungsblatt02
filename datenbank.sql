/* hier ist der gesamte code für die DB drinne
 * newsletter feld sollte bei default vielleicht einfach false sein */

CREATE TABLE kunde
(
    kundenid SERIAL UNIQUE,
    email character varying NOT NULL UNIQUE,
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
	passwort character varying NOT NULL
);

CREATE TABLE konto (
	kontoid SERIAL UNIQUE, 
	kundenemail character varying NOT NULL REFERENCES kunde (email), 
	name character varying NOT NULL, 
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

/* 
 * INSERT INTO kunde (email, vorname, nachname, alter, bank, agb, newsletter) VALUES ('louis@aol.com', 'louis', 'puis', 11, 'sparkasse', true, true); 
 * INSERT INTO konto (kundenemail, name, kontostand) VALUES ('julez@aol.com', 'Drogengeld', 0); 
 */

/* weiterer schnick-schnack. So kommt man an den kunden über seine eingegebene email: */
SELECT email, passwort FROM kunde INNER JOIN passwort USING (kundenid) WHERE email = 'julez@aol.com'; 
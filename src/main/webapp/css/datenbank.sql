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

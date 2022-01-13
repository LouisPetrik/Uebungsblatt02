# Uebungsblatt02

## TODO:
Was es zu tun gibt: 
Für die möglichkeit des einloggens muss mit den sessions gearbeitet werden und vermutlich die ganze liste 
in der session mit den möglichen kunden rausgenommen werden. 


Anfangen damit, auf der registrierungsseite actually den User in der Datenbank zu speichern. 
Zu beachten: - Email Regex benutzen. 


Für die Kunden-Id könnte man einfach pro eintrag das feld inkrementieren. 
Oder man generiert eine unique ID mit Java. 

Die regex für vorname und nachname müssen nicht richtig funktionsfähig gemacht werden. Außerdem muss 
noch sauber abgefangen werden, falls da wirklich etwas falsch ist.


String mit verschiedenen zeichen: character varying in postgres 



Zu aufgabe a) mit dem anzeigen des namens der bank: Die einlog-mechanik funktioniert soweit, aber in der navigation.jsp 
muss noch der name der bank angezeigt werden. Wichtig: Wir dürfen dafür ja die Expression Language nutzen. Wir dürfen auch die JSTL 
nutzen. Ein beispiel für if-else ist in der navigation.jsp zu sehen. 
Man darf nicht vergessen, dass page-encoding wie in der navigation.jsp anzupassen, sonst funktioniert JSTL nicht. 

Zu den Aufgaben 5 und 6: 

Ein Kunde kann mehrere Konten haben. Ein Konto kann mehrere Posten haben. 
In die tabelle "posten" wird nun die excel datei importiert, die damit sämtliche transaktionen für 
ein einzelnes konto hält. 

Für das erstellen eines neuen Kontos muss auch immer die kundenid bekannt sein. Vermutlich muss 
diese also noch aus der Datenbank beim Login in das kundenobjekt in der session gespeichert werden. 
Bisher wird eine ID für den 

Liste der Konten muss vermutlich in der Session gespeichert werden. 

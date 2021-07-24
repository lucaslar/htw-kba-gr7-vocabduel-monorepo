# 1. Komponentenschnitt

Das Informationssystem besteht aus drei großen Komponenten:
- vocabulary_administration
- game_administration
- user_administration

Diese haben jeweils eine Export-Schicht, in der die Database models und die Service Interfaces definiert sind.
Zudem eine Rest-Schicht, in der die REST-API zu den Komponenten implementiert ist.
Und sie haben eine Schicht, in der die Services implementiert sind.

Es gibt eine Komponente zum User Interface, die eine zusätzliche Export-Schicht für den Controller beinhaltet.

(configuration Komponente)

[comment]: <> (Add ideas here)

# 2. Schnittstellenbeschreibung

[comment]: <> (Add ideas here)

# 3. Konzeptionelles Datenmodell

[comment]: <> (Add ideas here)

# 4. Präsentationsschicht

[comment]: <> (Add ideas here)

# 5. Frameworks

Für dieses Java Maven-Projekt wurde Hibernate genutzt, um aus den models
eine MySQL Datenbank anzulegen und zu verwalten.
Dazu wird ein EntityManager genutzt, der auf eine konfigurierte persistence.xml zugreift,
in der steht, auf welche MySQL Datenbank mit welchem User wie zugegriffen werden soll
und welche models zur Datenbankerzeugung genutzt werden sollen.

Auf einem Tomcat Server der Version 9.0.40 läuft der Spring RestEasy Server, welcher
in der Rest-Schicht des configuration Moduls konfiguriert wird.
Die Rest-Schichten der Module game_administration, user_administration und vocabulary_administration
wurden als Dependencies angegeben und die Beans, in denen die REST HTTP Methoden definiert sind,
dem Server hinzugefügt.

Für die Benutzeroberfläche wird Angular genutzt.

[comment]: <> add mocking framework

# 6. Ablaufumgebung

[comment]: <> (Add ideas here)

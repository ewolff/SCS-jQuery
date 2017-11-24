# Beispiel starten

Die ist eine Schritt-für-Schritt-Anleitung zum Starten der Beispiele.
Informationen zu Maven und Docker finden sich im
[Cheatsheet-Projekt](https://github.com/ewolff/cheatsheets-DE).

## Installation

* Die Beispiele sind in Java implementiert. Daher muss Java
  installiert werden. Die Anleitung findet sich unter
  https://www.java.com/en/download/help/download_options.xml . Da die
  Beispiele kompiliert werden müssen, muss ein JDK (Java Development
  Kit) installiert werden. Das JRE (Java Runtime Environment) reicht
  nicht aus. Nach der Installation sollte sowohl `java` und `javac` in
  der Eingabeaufforderung möglich sein.

* Die Beispiele laufen in Docker Containern. Dazu ist eine
  Installation von Docker Community Edition notwendig, siehe
  https://www.docker.com/community-edition/ . Docker kann mit
  `docker` aufgerufen werden. Das sollte nach der Installation ohne
  Fehler möglich sein.

* Die Beispiele benötigen zum Teil sehr viel Speicher. Daher sollte
  Docker ca. 4 GB zur Verfügung haben. Sonst kann es vorkommen, dass
  Docker Container aus Speichermangel beendet werden. Unter Windows
  und macOS findet sich die Einstellung dafür in der Docker-Anwendung
  unter Preferences/ Advanced.

* Nach der Installation von Docker sollte `docker-compose` aufrufbar
  sein. Wenn Docker Compose nicht aufgerufen werden kann, ist es nicht
  als Teil der Docker Community Edition installiert worden. Dann ist
  eine separate Installation notwendig, siehe
  https://docs.docker.com/compose/install/ .

## Build

Wechsel in das Verzeichnis `scs-demo-jquery` und starte `./mvnw clean
package` bzw. `mvnw.cmd clean package` (Windows). Das wird einige Zeit
dauern:

```
[~/SCS-jQuery/scs-demo-jquery]./mvnw clean package
...
[INFO] 
[INFO] --- maven-jar-plugin:2.5:jar (default-jar) @ scs-demo-order-jquery ---
[INFO] Building jar: /Users/wolff/SCS-jQuery/scs-demo-jquery/scs-demo-order-jquery/target/scs-demo-order-jquery-0.0.1-SNAPSHOT.jar
[INFO] 
[INFO] --- spring-boot-maven-plugin:1.3.5.RELEASE:repackage (default) @ scs-demo-order-jquery ---
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary:
[INFO] 
[INFO] scs-demo-jquery .................................... SUCCESS [  0.948 s]
[INFO] scs-demo-catalog-jquery ............................ SUCCESS [ 17.129 s]
[INFO] scs-demo-order-jquery .............................. SUCCESS [ 10.332 s]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 28.767 s
[INFO] Finished at: 2017-09-08T15:54:39+02:00
[INFO] Final Memory: 36M/513M
[INFO] ------------------------------------------------------------------------
```

Weitere Information zu Maven gibt es im
[Maven Cheatsheet](https://github.com/ewolff/cheatsheets-DE/blob/master/MavenCheatSheet.md).

Falls es dabei zu Fehlern kommt:

* Stelle sicher, dass die Datei `settings.xml` im Verzeichnis  `.m2`
in deinem Heimatverzeichnis keine Konfiguration für ein spezielles
Maven Repository enthalten. Im Zweifelsfall kannst du die Datei
einfach löschen.

* Die Tests nutzen einige Ports auf dem Rechner. Stelle sicher, dass
  im Hintergrund keine Server laufen.

* Führe die Tests beim Build nicht aus: `./mvnw clean package
  -Dmaven.test.skip=true` bzw. `mvnw.cmd clean package
  -Dmaven.test.skip=true`.

* In einigen selten Fällen kann es vorkommen, dass die Abhängigkeiten
  nicht korrekt heruntergeladen werden. Wenn du das Verzeichnis
  `repository` im Verzeichnis `.m2` löscht, werden alle Abhängigkeiten
  erneut heruntergeladen.

## Docker Container starten

Weitere Information zu Docker gibt es im
[Docker Cheatsheet](https://github.com/ewolff/cheatsheets-DE/blob/master/DockerCheatSheet.md).

Zunächst musst du die Docker Images bauen. Wechsel in das Verzeichnis 
`docker` und starte `docker-compose build`. Das lädt die Basis-Images
herunter und installiert die Software in die Docker Images:

```
[~/SCS-jQuery/docker]docker-compose build 
...
Step 10/10 : CMD /start
 ---> Running in aed1c44da50b
 ---> 870158f27c4b
Removing intermediate container aed1c44da50b
Successfully built 870158f27c4b
Successfully tagged scsjquery_varnish:latest
```

Wenn der Build nicht klappt, dann kann man mit  `docker-compose build
--no-cache` die Container komplett neu bauen.

Danach sollten die Docker Images erzeugt worden sein. Sie haben das
Präfix `scsjquery`:

```
[~/SCS-jQuery/docker]docker images
REPOSITORY                                      TAG                 IMAGE ID            CREATED              SIZE
scsjquery_varnish                                       latest              870158f27c4b        44 seconds ago      328MB
scsjquery_order                                         latest              eea94180172c        48 seconds ago      205MB
scsjquery_catalog                                       latest              7fc6bcfc3a06        50 seconds ago      205MB
```

Nun kannst Du die Container mit `docker-compose up -d` starten. Die
Option `-d` bedeutet, dass die Container im Hintergrund gestartet
werden und keine Ausgabe auf der Kommandozeile erzeugen.

```
[~/SCS-jQuery/docker]docker-compose up -d
Creating network "scsjquery_default" with the default driver
Creating scsjquery_catalog_1 ... 
Creating scsjquery_order_1 ... 
Creating scsjquery_order_1
Creating scsjquery_catalog_1 ... done
Creating scsjquery_varnish_1 ... 
Creating scsjquery_varnish_1 ... done
```

Du kannst nun überprüfen, ob alle Docker Container laufen:

```
[~/SCS-jQuery/docker]docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                    NAMES
b71d21cd324e        scsjquery_varnish   "/start"                 19 seconds ago      Up 17 seconds       0.0.0.0:8081->8080/tcp   scsjquery_varnish_1
c3cb6750323b        scsjquery_catalog   "/bin/sh -c '/usr/..."   20 seconds ago      Up 18 seconds       8080/tcp                 scsjquery_catalog_1
f062748bfa7f        scsjquery_order     "/bin/sh -c '/usr/..."   20 seconds ago      Up 18 seconds       8080/tcp                 scsjquery_order_1
```

`docker ps -a`  zeigt auch die terminierten Docker Container an. Das
ist nützlich, wenn ein Docker Container sich sofort nach dem Start
wieder beendet..

Wenn einer der Docker Container nicht läuft, kannst du dir die Logs
beispielsweise mit `docker logs scsjquery_order_1` anschauen. Der Name
der Container steht in der letzten Spalte der Ausgabe von `docker
ps`. Das Anzeigen der Logs funktioniert auch dann, wenn der Container
bereits beendet worden ist. Falls im Log steht, dass der Container
`killed` ist, dann hat Docker den Container wegen Speichermangel
beendet. Du solltest Docker mehr RAM zuweisen z.B. 4GB. Unter Windows
und macOS findet sich die RAM-Einstellung in der Docker application
unter Preferences/ Advanced.

Um einen Container genauer zu untersuchen, kannst du eine Shell in dem
Container starten. Beispielsweise mit `docker exec -it
scsjquery_order_1 /bin/sh` oder du kannst in dem Container ein
Kommando mit `docker exec  scsjquery_order_1 /bin/ls` ausführen.
Leider funktioniert das nur in dem Order- und Varnish-Container. Der
Common-Container enthält nur ein Go Binary und daher keine weiteren
Werkzeuge.

Die Webseite für das Beispiel steht unter http://localhost:8081/ bereit.

Mit `docker-compose down` kannst Du alle Container beenden.


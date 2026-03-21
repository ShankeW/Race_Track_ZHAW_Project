# Racetrack Laufen Lassen

Racetrack kann mit folgender Befehl laufen gelassen:
**./gradlew run**

Racetrack Tests können mit folgender Befehl laufen gelassen:
**./gradlew test**

## Branching Model
Das Branching Model welche wir für unsere Projekt verwenden sei das [Github Flow Modell](https://docs.github.com/en/get-started/using-github/github-flow)

Möchte man eine Verändering beim Projekt durchführen muss man folgende Steps folgen:
1. Eine Neue Branch erstellen.
2. Die gewünschte Changes erstellen und committen.
3. Eine Pull request für diese Branch erstellen.
4. Eine Review für das Pull request sammeln.
5. Das Pull Request Mergen.
6. Das Branch am Schluss löschen



# Testkonzept Racetrack

## 1. Ziel

Dieses Testkonzept beschreibt, wie das Projekt `Racetrack` getestet wird. Ziel ist es, die fachliche Korrektheit der Spiellogik sicherzustellen, Fehler bei Änderungen früh zu erkennen und vor Abgaben einen stabilen Stand nachzuweisen.

Das Konzept orientiert sich an den Leitfragen aus den Hinweisen zum Testkonzept: Was wird getestet, wie wird getestet und wann wird getestet.

## 2. Was wird getestet?

Getestet werden vor allem die fachlich wichtigen Teile des Projekts:

- Einlesen und Validieren von Strecken-Dateien
- Berechnung von Positionen, Bewegungen und Richtungen
- Spielregeln der Racetrack-Logik
- Kollisionen mit Wänden und anderen Autos
- Erkennen eines gültigen Zieleinlaufs
- Wechsel zwischen aktiven Fahrzeugen
- Strategien für Spielzüge, soweit sie implementiert sind

Konkret betrifft das vor allem die Klassen `Track`, `PositionVector`, `Car`, `Game` und die Move-Strategien.

### Was wird nicht bzw. nur eingeschränkt getestet?

Nicht im Fokus stehen:

- externe Bibliotheken wie Gradle, JUnit oder Text-IO
- Performance- und Lasttests
- alle theoretisch möglichen Strecken- und Spielzug-Kombinationen
- rein optische bzw. komfortbezogene Aspekte der Konsolenausgabe

Begründung: Für dieses Projekt ist die korrekte Spiellogik wichtiger als Lastverhalten oder vollständige Oberflächenprüfung. Der Testaufwand wird deshalb auf die fachlich kritischen Funktionen konzentriert.

## 3. Wie wird getestet?

### 3.1 Automatisierte Tests

Der Schwerpunkt liegt auf automatisierten Tests mit JUnit über Gradle, damit Tests bei Codeänderungen schnell und wiederholbar ausgeführt werden können.

Geplant bzw. bereits vorhanden sind vor allem Unit-Tests für:

- Dateiverarbeitung und Validierung von Tracks
- Vektor- und Richtungsberechnungen
- Randfälle der Spiellogik
- Regelverhalten bei Kollision, Zielüberquerung und Fahrzeugwechsel

Bereits im Repository vorhanden sind automatisierte Tests für `Track` und `PositionVector`. Weitere Tests werden parallel zur Implementierung der noch offenen Spiellogik ergänzt.

### 3.2 Manuelle Tests

Manuelle Tests werden nur dort eingesetzt, wo sie sinnvoller sind als reine Unit-Tests:

- vollständiger Spielablauf in der Konsole
- Zusammenspiel von Track-Datei, Zugstrategie und Spielsteuerung
- Plausibilitätsprüfung der Ausgabe für Benutzerinnen und Benutzer

Begründung: Das PDF betont, dass Tests bei Änderungen erneut durchgeführt werden müssen. Deshalb bevorzugen wir automatisierte Tests und setzen manuelle Tests nur gezielt für End-to-End-Szenarien ein.

## 4. Wann wird getestet?

Getestet wird nicht nur am Schluss, sondern während des ganzen Projekts:

- während der Implementierung neuer Klassen oder Methoden
- nach Änderungen an bestehender Spiellogik
- nach Bugfixes, damit Fehler nicht erneut auftreten
- vor dem Merge bzw. vor einer Abgabe als kurzer Regressionstest

Der normale Ablauf ist:

1. Funktion implementieren oder ändern
2. passende Unit-Tests erstellen oder anpassen
3. automatisierte Tests lokal ausführen
4. vor Meilensteinen zusätzlich einen kurzen manuellen Gesamttest durchführen

## 5. Testmittel

Für das Testen werden folgende Mittel verwendet:

- `JUnit Jupiter` für Unit-Tests
- `Gradle` zum Ausführen der Tests
- vorhandene Track-Dateien aus dem Projektordner `tracks/`
- vorbereitete Zugdateien aus dem Ordner `moves/` für reproduzierbare Szenarien

Die Tests werden über den Gradle-Testtask ausgeführt.

## 6. Verantwortlichkeit

Alle Teammitglieder sind dafür verantwortlich, bei ihren Änderungen passende Tests mitzudenken und bestehende Tests nicht zu brechen. Vor einer gemeinsamen Abgabe wird der aktuelle Stand nochmals mit den wichtigsten automatisierten und manuellen Tests überprüft.

## 7. Zusammenfassung

Das Racetrack-Projekt wird primär mit automatisierten Unit-Tests getestet. Der Fokus liegt auf der Spiellogik, dem Einlesen von Strecken und den zentralen Spielregeln. Manuelle Tests ergänzen das Konzept nur für wenige vollständige Anwendungsszenarien in der Konsole. Getestet wird fortlaufend während der Entwicklung sowie nochmals gezielt vor Abgaben.

# Äquivalenzklassen

## Car
### move()
Diese Methode enthält keine Parameter, deshalb sind alle Inputs teil der gültigen Äquivalenzklassen.
### accelerate(Direction acceleration)
Diese Methode enthälte eine Enum als Parameter
Gültige Äquivalenzklasse: Direction Objekt
Ungültige Äquivalenzklasse: leere / Null Objekt

## Game
### calculatePath(PositionVector startPosition, PositionVector endPosition)
   1. `calculatePath()` mit diagonale Bewegung -> Startposition, Endposition und Positionen dazwischen werden zurück gegeben.
   2. `calculatePath()` mit Bewegung in Achsenrichtung -> Alle zwischenzeitliche Positionen bleiben auf derselben Achse.
### doCarTurn(Direction acceleration)
   3. `doCarTurn`() auf dem eigenen Startfeld -> Keine Eigenkollision auf der Startposition.
   4. `doCarTurn`() auf ein besetztes Streckenfeld -> Das aktuelle Auto baut einen Unfall; das letzte verbleibende Auto wird zum Gewinner.
   5. `doCarTurn`() überquert das Ziel in korrekter Richtung -> Der Gewinner wird festgelegt und das Auto bleibt auf dem Zielfeld stehen.
   6. `doCarTurn`() überquert das Ziel in falscher Richtung -> Kein Gewinner; das Auto verbleibt auf dem vorherigen Pfadabschnitt.
   7. `doCarTurn`(), nachdem bereits ein Gewinner feststeht -> Der Zug wird ignoriert.
   8. `doCarTurn`() für ein bereits verunfalltes Auto -> Der Zug wird ignoriert.
   9. Parametern:{Null Objekt, leerer Parameter} sind ungültig und werden nicht akzeptiert.
### switchToNextActiveCar()
   10. `switchToNextActiveCar()` mit verunfallten Autos -> Überspringt verunfallte Autos und beginnt am Ende wieder von vorn.
   11. `switchToNextActiveCar()`, wenn alle Autos verunfallt sind -> Der aktuelle Index bleibt unverändert.
### nextCarMove(int carIndex)
   12. `nextCarMove()` ohne Strategie -> `Optional.empty()`.
   13. `nextCarMove()` mit Strategie -> Gibt den von der Strategie bereitgestellten Zug zurück.

## MoveListStrategy
### nextMove()
   1. Ausgewählte moveFile existiert und beinhaltet gültige "Directions" -> `nextMove()` verarbeite und gibt die erste "Direction" zurück.
   2. Ausgewählte moveFile existiert und beinhaltet mehrere gültige "Directions" -> `nextMove()` verarbeiten diesen in der richtigen Reihenfolge.
   3. Ausgew'hlte moveFile existiert ist aber leer -> `nextMove()` gibt ein `Optional.emty()` Objekt zurück.
   4. moveFile beinhaltet ungültige "Direction" Objekt -> `nextMove()` wirft eine IllegalArgumentException.
   5. Ausgewählte moveFile kann nicht eingelesen weden -> `nextMove()` wirft eine NullPointerExcetion.

# Klassendiagramm
[Klassendiagramm](Klassendiagramm.png)

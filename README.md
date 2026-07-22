# Team & Contributions
This was a team project developed for the Software-Project Module at **ZHAW (Zurich University of Applied Sciences)**.
## **My primary contributions:** 
 - Implemented the class `MoveListStrategy` (Let user choose a strategy for racing).
 - Creating tests for classes `Game` and `MoveListStrategy` and the corresponding equivalence classes.

# Racetrack

## Projektbeschreibung

Das Racetrack-Projekt ist eine Simulation eines Rennspiels auf einem Raster. Mehrere Fahrzeuge bewegen sich
rundenbasiert über eine Strecke, wobei Geschwindigkeit und Richtung durch Beschleunigungsvektoren beeinflusst werden.

Ziel des Spiels ist es, als erstes Fahrzeug die Ziellinie korrekt zu überqueren oder als letztes verbleibendes
Fahrzeug nicht auszuscheiden.

Die Spiellogik basiert auf physikalisch vereinfachten Bewegungsregeln, bei denen die aktuelle Geschwindigkeit eines
Fahrzeugs in jeder Runde durch eine gewählte Beschleunigung verändert wird.

Das Projekt dient dazu, objektorientierte Konzepte wie Klassenstruktur, klare Verantwortlichkeiten
(Single Responsibility Principle) und das Strategiemuster (`MoveStrategy`) praktisch anzuwenden.

## Projekt starten und testen

Racetrack kann mit folgendem Befehl gestartet werden:

```bash
./gradlew run
```

Die automatisierten Tests können mit folgendem Befehl ausgeführt werden:

```bash
./gradlew test
```

## Branching-Modell

Für das Projekt verwenden wir das [GitHub-Flow-Modell](https://docs.github.com/en/get-started/using-github/github-flow).

Änderungen am Projekt erfolgen in den folgenden Schritten:

1. Einen neuen Branch erstellen.
2. Die gewünschten Änderungen umsetzen und committen.
3. Einen Pull Request für den Branch erstellen.
4. Eine Review für den Pull Request einholen.
5. Den Pull Request mergen.
6. Den Branch am Schluss löschen.


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

Bereits im Repository vorhanden sind automatisierte Tests für `Track`, `PositionVector`, `Car`, `Game` sowie die
vorhandenen Move-Strategien. Weitere Tests werden bei Änderungen an der Spiellogik ergänzt.

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
### id, position copying, zero baseline
   1. Construction invariants: configured `char` id is preserved, initial position is copied, velocity starts at zero and is copied.
### accelerate(Direction acceleration)
   2. Predictive movement: `nextPosition()` reflects current velocity without mutating position.
   3. Acceleration handling: valid accelerations accumulate deltas, null accelerations are rejected.
### move()
   4. Movement execution: `move()` applies velocity to position and keeps velocity, `updatePosition()` jumps directly to given coordinates.
### isCrashed()
   5. Crash lifecycle: `crash()` flags the car and freezes position, further movement or updates after a crash leave position unchanged while velocity stays at last value.
   6. Crash state reporting: freshly constructed car reports `isCrashed() == false`.
### getMove()
   7. Strategy integration: absent strategy yields `Optional.empty()`, configured strategy delegates its move.

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
   1. Ausgewählte Move-Datei existiert und beinhaltet gültige `Direction`-Werte -> `nextMove()` verarbeitet und gibt den ersten Zug zurück.
   2. Ausgewählte Move-Datei existiert und beinhaltet mehrere gültige `Direction`-Werte -> `nextMove()` verarbeitet diese in der richtigen Reihenfolge.
   3. Ausgewählte Move-Datei existiert, ist aber leer -> `nextMove()` gibt `Optional.empty()` zurück.
   4. Die Move-Datei beinhaltet einen ungültigen `Direction`-Wert -> `nextMove()` wirft eine `IllegalArgumentException`.
   5. Die ausgewählte Move-Datei kann nicht eingelesen werden -> der Konstruktor wirft eine `UncheckedIOException`.

## Abstraktionsebene

Die Abstraktion im Racetrack-Projekt wurde so gewählt, dass jede Klasse eine klar definierte Aufgabe übernimmt.

- Die Klasse `RaceTrack` übernimmt die Steuerung des Programms sowie die Interaktion mit der Benutzeroberfläche.
- Die Klasse `Game` enthält die zentrale Spiellogik, wie das Ausführen von Zügen und das Bestimmen eines Gewinners.
- Die Klasse `Track` ist verantwortlich für das Einlesen und Verwalten der Streckendaten.
- Die Klasse `Car` speichert den Zustand eines Fahrzeugs, wie Position, Geschwindigkeit und Unfallstatus.
- Die verschiedenen `MoveStrategy`-Implementierungen bestimmen das Verhalten der Fahrzeuge.

Durch diese Aufteilung werden Verantwortlichkeiten klar getrennt, was den Code besser wartbar, verständlicher und erweiterbar macht.

Diese Struktur folgt dem Prinzip der klaren Verantwortlichkeit (Single Responsibility Principle).

## Bekannte Einschränkungen

- Es gibt keine grafische Benutzeroberfläche (GUI), das Spiel läuft nur über die Konsole.
- Fehlerhafte oder ungültige Eingabedateien (z.B. Track-Dateien) werden nicht vollständig abgefangen.
- Die Kollisionslogik basiert auf einer vereinfachten Linienprüfung und kann in seltenen Fällen ungenau sein.
- Es existiert keine Speicherung oder Wiederaufnahme von Spielständen.
- Die implementierten MoveStrategien sind nicht optimiert und garantieren keine bestmöglichen Spielzüge.
- Das Projekt ist für Lernzwecke entwickelt und nicht für produktiven Einsatz gedacht.

# Klassendiagramm
[Klassendiagramm](Klassendiagramm.png)
## Änderungen am Klassendiagramm
### MoveListStrategy()
- Ein neues Attribut `private final List<String> moveList;` wird hinzugefügt.
- Der Konstruktor verwendet nun die Klasse `UserInterface.java`.
### Was bewusst nicht dargestellt wird
- Private Methoden
- Selbst erstellte Exceptions

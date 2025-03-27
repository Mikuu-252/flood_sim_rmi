# Symulator Sieci Rzecznej z Zbiornikami Retencyjnymi (RMI)

## Opis Projektu (Polski)
Ten projekt jest aplikacją rozproszoną, która symuluje sieć rzeczną ze zbiornikami retencyjnymi. System składa się z kilku podsystemów działających równolegle i komunikujących się za pomocą RMI (Remote Method Invocation).

### Składniki systemu:
- **Zbiornik retencyjny (RetentionBasin)** – symuluje działanie zbiornika retencyjnego i kontroluje poziom wody.
- **Odcinek rzeczny (RiverSection)** – symuluje przepływ wody między zbiornikami oraz uwzględnia wpływ opadów atmosferycznych.
- **Środowisko (Environment)** – generuje opady atmosferyczne wpływające na przepływ wody w odcinkach rzecznych.
- **Centrala (ControlCenter)** – umożliwia monitorowanie poziomu wody w zbiornikach oraz sterowanie zasuwami.
- **Krawiec (Tailor)** – komponent odpowiedzialny za ustanawianie połączeń pomiędzy pozostałymi komponentami systemu.

Każdy element systemu komunikuje się poprzez zdalne wywołania metod (RMI). Komponent Krawca jest rejestrowany w rejestrze RMI na zadanym hoście i porcie, umożliwiając dynamiczne konfigurowanie połączeń między podsystemami.

W implementacji systemu wykorzystano interfejsy dostarczone w bibliotece `floodlib-1.0-SNAPSHOT.jar`. Biblioteka została dostarczona przez prowadzącego zajęcia i nie można było jej edytować, tylko należało zaimportować ją dodatkowo w swoim projekcie.

---

## River Network Simulator with Retention Basins (RMI)

### Project Description (English)
This project is a distributed application that simulates a river network with retention basins. The system consists of multiple subsystems running in parallel and communicating via Remote Method Invocation (RMI).

### System Components:
- **Retention Basin (RetentionBasin)** – simulates a retention basin and controls water levels.
- **River Section (RiverSection)** – simulates water flow between basins and accounts for rainfall impact.
- **Environment (Environment)** – generates rainfall affecting water flow in river sections.
- **Control Center (ControlCenter)** – monitors basin water levels and controls discharge gates.
- **Tailor (Tailor)** – component responsible for establishing connections between other system components.

Each system component communicates via remote method calls (RMI). The Tailor component is registered in the RMI registry at a known host and port, enabling dynamic configuration of connections between subsystems.

The system is implemented using interfaces provided in the `floodlib-1.0-SNAPSHOT.jar` library. This library was provided by the course instructor and could not be edited; it had to be imported separately into the project.


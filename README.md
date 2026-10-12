# MiniDB – address book

A small address book with a **Java Swing** UI and an embedded **Apache Derby** database (learning project from my Java course): list view, single-entry view with editing, and new entries.

## Requirements

- JDK 17 or newer
- Apache Derby (`derby.jar`) on the classpath

## Database

The app opens the Derby database `miniDB` relative to the working directory (`src/de/fernschulen/minidb/Main/util/Properties.java`). It expects a table `adressen` with the columns `IDNUMBER` (generated), `NAME`, `LASTNAME`, `STREET`, `POSTALCODE`, `CITY`, `PHONE`.

## Run

Open the folder in IntelliJ IDEA (`newdatabasegui.iml`) and start `de.fernschulen.minidb.Main.MiniDBMain`.

## License

MIT, see [LICENSE](LICENSE).

## Contact

Johnny-tech-droid · contact@hausrund.info · https://github.com/Johnny-tech-droid

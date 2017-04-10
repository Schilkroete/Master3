package schilkroete.healthy.datenbankzugriffe;

/**
 * Instanzen dieser Klassen können die Daten eines SQLite-Datensatzes aufnehmen
 * Das ListView wird mit Objekten dieser Klasse gefüllt
 *
 * Diese Klasse wird genutzt um die Daten der Datensätze der SQLite-Datenbank in Java-Objekten zu speichern
 *
 */
public class User {

    private String vorname;
    private String nachname;
    private String passwort;
    private String rolle;
    private String datumZeit;
    private long id;

    public User(String vorname, String nachname, String passwort, String rolle,
                String datumZeit,long id) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.passwort = passwort;
        this.rolle = rolle;
        this.id = id;
        this.datumZeit = datumZeit;
    }

    public String gibVorname() {
        return vorname;
    }
    public void setzeVorname(String vorname) {
        this.vorname = vorname;
    }

    public String gibNachname() {
        return nachname;
    }
    public void setzeNachname(String nachname) {
        this.nachname = nachname;
    }

    public String gibPasswort() {
        return passwort;
    }
    public void setzePasswort(String passwort) {
        this.passwort = passwort;
    }

    public String gibRolle() {
        return rolle;
    }
    public void setzeRolle(String rolle) {
        this.rolle = rolle;
    }

    public String datumZeit() {
        return datumZeit;
    }
    public void datumZeit(String datumZeit) {
        this.datumZeit = datumZeit;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString(){
        String ausgabe = "Name: " + vorname + " " + nachname + " Passwort: " + passwort +
                "\nRolle: " + rolle + " Erstelldatum: " + datumZeit;

        return ausgabe;
    }
}
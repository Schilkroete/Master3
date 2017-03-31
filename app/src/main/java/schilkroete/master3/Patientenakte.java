package schilkroete.master3;

/**
 * Instanzen dieser Klassen können die Daten eines SQLite-Datensatzes aufnehmen
 * Das ListView wird mit Objekten dieser Klasse gefüllt
 *
 * Diese Klasse wird genutzt um die Daten der Datensätze der SQLite-Datenbank in Java-Objekten zu speichern
 *
 */
public class Patientenakte {

    private String vorname;
    private String nachname;
    private String geburtsdatum;
    private String beschwerden;
    private String medikamente;
    private String notizen;
    private String datumZeit;
    private long id;

    public Patientenakte(String vorname, String nachname, String geburtsdatum, String beschwerden,
                         String medikamente, String notizen, String datumZeit, long id) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.beschwerden = beschwerden;
        this.medikamente = medikamente;
        this.notizen = notizen;
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

    public String gibGeburtsdatum() {
        return geburtsdatum;
    }
    public void setzeGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public String gibBeschwerden() {
        return beschwerden;
    }
    public void setzeBeschwerden(String beschwerden) {
        this.beschwerden = beschwerden;
    }

    public String gibMedikamente() {
        return medikamente;
    }
    public void setzeMedikamente(String medikamente) {
        this.medikamente = medikamente;
    }

    public String gibNotizen() {
        return notizen;
    }
    public void setzeNotizen(String notizen) {
        this.notizen = notizen;
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
        String ausgabe = "Name: " + vorname + " " + nachname + "Geburtsdatum: " + geburtsdatum +
                "\nMedikamente: " + beschwerden + "Beschwerden: " + medikamente + "Notizen: " +
                notizen + "\nErstelldatum: " + datumZeit;

        return ausgabe;
    }
}
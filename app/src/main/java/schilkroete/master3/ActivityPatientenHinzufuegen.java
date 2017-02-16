package schilkroete.master3;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by Schilkroete on 02.02.2017.
 */

/**
 * Der Anwender gibt Patientendaten in die entsprechenden Felder und speichert diese anschliessend
 * in eine Datenbank
 */
public class ActivityPatientenHinzufuegen extends Activity implements View.OnClickListener {

    private static final String TAG = ActivityPatientenHinzufuegen.class.getSimpleName();

    public EditText et_vorname;
    public EditText et_nachname;
    public EditText et_notizen;
    public EditText et_medikamente;
    public EditText et_beschwerden;
    public TextView tv_pflichtfeld;
    public TextView tv_geburtsdatum;
    public TextView tv_aktuellesDatum;
    public TextView tv_alter;
    public Button btn_waehleGeburtstag;

    private int alter;

    private AgeCalculation ausgewahltesDatum = null;
    static final int DATE_START_DIALOG_ID = 0;
    public int startJahrImDialog = 1975;
    public int startMonatImDialog = 6;
    public int startTagImDialog = 15;

    private PatientenakteDatenquelle datenquelle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patienten_hinzufuegen);

        et_vorname = (EditText) findViewById(R.id.et_vorname);
        et_nachname = (EditText) findViewById(R.id.et_nachname);
        et_notizen = (EditText) findViewById(R.id.et_notizen);
        et_medikamente = (EditText) findViewById(R.id.et_medikamente);
        et_beschwerden = (EditText) findViewById(R.id.et_beschwerden);
        tv_aktuellesDatum = (TextView) findViewById(R.id.tv_aktuellesDatum);
        tv_geburtsdatum = (TextView) findViewById(R.id.tv_geburtsdatum);
        tv_pflichtfeld = (TextView) findViewById(R.id.tv_pflichtfeld);
        tv_alter = (TextView) findViewById(R.id.tv_alter);
        btn_waehleGeburtstag = (Button) findViewById(R.id.btn_waehleGeburtstag);

        ausgewahltesDatum = new AgeCalculation();
        tv_aktuellesDatum.setText(ausgewahltesDatum.getCurrentDate());

        btn_waehleGeburtstag.setOnClickListener(this);

        datenquelle = new PatientenakteDatenquelle(this);

    }


































    /**
     * Erstellt ein Dialogfenster, welches uns einen Kalender anzeigt
     * @param id
     * @return soll uns die Werte Jahr, Monat und Tag ausgeben
     */
    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_START_DIALOG_ID:
                return new DatePickerDialog(this, waehleGeburtsdatum, startJahrImDialog, startMonatImDialog, startTagImDialog);
        }
        return null;
    }


    /**
     * Öffnet das Dialogfenster beim Klicken des Plus-Buttons
     * @param view
     */
    public void onClick (View view){
        switch (view.getId()){
            case R.id.btn_waehleGeburtstag:
                Log.e(TAG, "Button \"waehleGeburtstag\" wurde geklickt und Dialog wurde geöffnet");
                showDialog(DATE_START_DIALOG_ID);
                break;
            default:
                break;
        }
    }


    /**
     * Nach Berechnung des Alter wird das Geburtdatum und das Alter für den Anwender sichtbar angezeit
     */
    private DatePickerDialog.OnDateSetListener waehleGeburtsdatum = new DatePickerDialog.OnDateSetListener () {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

            ausgewahltesDatum.setDateOfBirth(selectedYear, selectedMonth, selectedDay);
            berechneAlter();
            tv_geburtsdatum.setText(selectedDay + "." + (startMonatImDialog + 1) + "." +
                    startJahrImDialog);

            tv_alter.setText(ausgewahltesDatum.getResult());

            if (tv_geburtsdatum != null){
                tv_pflichtfeld.setVisibility(View.INVISIBLE);
            }
        }
    };


    /**
     * Berechung des Alters über die ausgewählen Werte aus dem Dialogfenster
     */
    private void berechneAlter() {
        //Log.e(TAG, "Berechnung startet");
        ausgewahltesDatum.calculateYear();
        ausgewahltesDatum.calculateMonth();
        ausgewahltesDatum.calculateDay();
        //Log.e(TAG, "Berechnung durchgeführt");

        alter = ausgewahltesDatum.getResultInt();
        //Log.e(TAG, "Resultat = " + alter + " Jahre");
    }



}

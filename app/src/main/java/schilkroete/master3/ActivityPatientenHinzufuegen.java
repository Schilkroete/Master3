package schilkroete.master3;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    public TextView tv_geburtsdatum;
    public TextView tv_pflichtfeld;
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


        tv_aktuellesDatum = (TextView) findViewById(R.id.tv_aktuellesDatum);
        tv_geburtsdatum = (TextView) findViewById(R.id.tv_geburtsdatum);
        tv_pflichtfeld = (TextView) findViewById(R.id.tv_pflichtfeld);
        tv_alter = (TextView) findViewById(R.id.tv_alter);
        btn_waehleGeburtstag = (Button) findViewById(R.id.btn_waehleGeburtstag);

        ausgewahltesDatum = new AgeCalculation();
        tv_aktuellesDatum.setText(ausgewahltesDatum.getCurrentDate());

        btn_waehleGeburtstag.setOnClickListener(this);

        datenquelle = new PatientenakteDatenquelle(this);

        aktiviereHinzufuegenButton();
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Die Datenquelle wird geöffnet.");
        datenquelle.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Die Datenquelle wird geschlossen.");
        datenquelle.close();
    }

    private void aktiviereHinzufuegenButton() {
        Button btn_speichern = (Button) findViewById(R.id.btn_speichern);
        final EditText et_vorname = (EditText) findViewById(R.id.et_vorname);
        final EditText et_nachname = (EditText) findViewById(R.id.et_nachname);
        final TextView tv_geburtsdatum = (TextView) findViewById(R.id.tv_geburtsdatum);
        final EditText et_beschwerden = (EditText) findViewById(R.id.et_beschwerden);
        final EditText et_medikamente = (EditText) findViewById(R.id.et_medikamente);
        final EditText et_notizen = (EditText) findViewById(R.id.et_notizen);

        btn_speichern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String st_et_vorname = et_vorname.getText().toString();
                String st_et_nachname = et_nachname.getText().toString();
                String st_tv_geburtsdatum = tv_geburtsdatum.getText().toString();
                String st_et_beschwerden = et_beschwerden.getText().toString();
                String st_et_medikamente = et_medikamente.getText().toString();
                String st_et_notizen = et_notizen.getText().toString();

                if(TextUtils.isEmpty(st_et_nachname)) {
                    et_nachname.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(st_et_vorname)) {
                    et_vorname.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(st_tv_geburtsdatum)) {
                    tv_geburtsdatum.setError(getText(R.string.editText_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(st_et_beschwerden)) {
                    et_beschwerden.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(st_et_medikamente)) {
                    et_medikamente.setError(getString(R.string.editText_errorMessage));
                    return;
                }
                if(TextUtils.isEmpty(st_et_notizen)) {
                    et_notizen.setError(getString(R.string.editText_errorMessage));
                    return;
                }

                et_vorname.setText("");
                et_nachname.setText("");
                tv_geburtsdatum.setText("");
                et_beschwerden.setText("");
                et_medikamente.setText("");
                et_notizen.setText("");

                datenquelle.erstellePatientenakte(st_et_vorname, st_et_nachname, st_tv_geburtsdatum,
                        st_et_beschwerden, st_et_medikamente, st_et_notizen);

                InputMethodManager inputMethodManager;
                inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if(getCurrentFocus() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        });

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

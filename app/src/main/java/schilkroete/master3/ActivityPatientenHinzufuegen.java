package schilkroete.master3;

/*
 TODO Design ändern, einfacher Aufbau (Liste)
 TODO BG schlicht (weiß)
 TODO DialogFenster für diverse Rollen beim klicken auf ActivityPatientenHinzufügen
 TODO Dashboard, Icons mit Beschriftung

 TODO DatePicker Fullscreen
 TODO Errormeldungen größer anzeigen lassen
 TODO Geburtsdatumswahl manuell und digital erstellen

 TODO Tastatur ausblenden nach Eingabe im EditText
 */

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RadioButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Der Anwender gibt Patientendaten in die entsprechenden Felder und speichert diese anschliessend
 * in eine Datenbank
 */
public class ActivityPatientenHinzufuegen extends Activity implements View.OnClickListener {

    private static final String TAG = ActivityPatientenHinzufuegen.class.getSimpleName();

    public TextView tv_geburtsdatum, tv_pflichtfeld, tv_aktuellesDatum, tv_alter;
    public Button btn_waehleGeburtstag;
    private int alter;
    EditText et_beschwerden, et_medikamente, et_notizen, et_vorname, et_nachname;

    RadioButton rb_notizen_ja, rb_notizen_nein;

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

        erzeugeRelevanteFelder();
        aktiviereHinzufuegenButton();
    }

    public void erzeugeRelevanteFelder (){
        et_vorname = (EditText) findViewById(R.id.et_vorname);
        et_nachname = (EditText) findViewById(R.id.et_nachname);
        et_beschwerden = (EditText) findViewById(R.id.et_beschwerden);
        et_medikamente = (EditText) findViewById(R.id.et_medikamente);
        et_notizen = (EditText) findViewById(R.id.et_notizen);

        tv_geburtsdatum = (TextView) findViewById(R.id.tv_geburtsdatum);
        tv_alter = (TextView) findViewById(R.id.tv_alter);
        tv_pflichtfeld = (TextView) findViewById(R.id.tv_pflichtfeld);
        tv_aktuellesDatum = (TextView) findViewById(R.id.tv_aktuellesDatum);
        ausgewahltesDatum = new AgeCalculation();
        tv_aktuellesDatum.setText(ausgewahltesDatum.getCurrentDate());

        rb_notizen_ja = (RadioButton) findViewById(R.id.rb_notizen_ja);
        rb_notizen_nein = (RadioButton) findViewById(R.id.rb_notizen_nein);

        btn_waehleGeburtstag = (Button) findViewById(R.id.btn_waehleGeburtstag);
        btn_waehleGeburtstag.setOnClickListener(this);

        datenquelle = new PatientenakteDatenquelle(this);
    }

    public void zeigeNotizenfeld(View view){
        et_notizen.setVisibility(View.VISIBLE);
    }

    public void versteckeNotizenfeld(View view){
        et_notizen.setVisibility(View.INVISIBLE);
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

    /**
     * Was passiert wenn der Speicher-Button gedrückt wurde
     */
    private void aktiviereHinzufuegenButton() {
        Button btn_speichern = (Button) findViewById(R.id.btn_speichern);
        assert btn_speichern != null;
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
                    et_nachname.setError(getString(R.string.tv_error));
                    return;
                }
                if(TextUtils.isEmpty(st_et_vorname)) {
                    et_vorname.setError(getString(R.string.tv_error));
                    return;
                }
                if(TextUtils.isEmpty(st_tv_geburtsdatum)) {
                    tv_geburtsdatum.setError(getText(R.string.tv_error));
                    return;
                }
                if(TextUtils.isEmpty(st_et_beschwerden)) {
                    et_beschwerden.setError(getString(R.string.tv_error));
                    return;
                }
                if(TextUtils.isEmpty(st_et_medikamente)) {
                    et_medikamente.setError(getString(R.string.tv_error));
                    return;
                }
                if(rb_notizen_ja.isChecked() == true){
                    /*  rb_notizen_ja.setChecked(false);*/
                    rb_notizen_nein.setChecked(true);
                    et_notizen.setVisibility(View.INVISIBLE);
                }
                if(et_notizen.getVisibility() == View.VISIBLE && TextUtils.isEmpty(st_et_notizen)) {
                    et_notizen.setError(getString(R.string.tv_error));
                    return;
                }
                try {
                android.app.AlertDialog.Builder alertDialogAbfrage = new
                        android.app.AlertDialog.Builder(ActivityPatientenHinzufuegen.this);

                alertDialogAbfrage
                        .setIcon(android.R.drawable.ic_input_add)
                        .setTitle("Sicherheitsabfrage")
                        .setMessage("Möchten Sie die Patientenakte anlegen?")
                        .setCancelable(false)
                        .setPositiveButton("Speichern und Fortfahren", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                speichereDatensatz();
                                loescheContent();
                                Intent ptSuchen = new Intent(ActivityPatientenHinzufuegen.this,
                                        ActivityPatientenSuchen.class);
                                startActivity(ptSuchen);
                            }
                        })
                        .setNegativeButton("Nein, Inhalt bearbeiten",new
                                DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNeutralButton("Speichern und neue Akte erstellen", new
                                DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            speichereDatensatz();
                            loescheContent();
                        }
                    });
                android.app.AlertDialog alertDialog = alertDialogAbfrage.create();
                alertDialog.show();
                } catch (Exception ex) {
                    Log.e(TAG, "AlertDialog Fehler");
                }
            }
        });
    }

    public void speichereDatensatz(){
        String st_erstelldatum = gibAktuellesDatum();
        String st_et_vorname = et_vorname.getText().toString();
        String st_et_nachname = et_nachname.getText().toString();
        String st_tv_geburtsdatum = tv_geburtsdatum.getText().toString();
        String st_et_beschwerden = et_beschwerden.getText().toString();
        String st_et_medikamente = et_medikamente.getText().toString();
        String st_et_notizen = et_notizen.getText().toString();

        datenquelle.erstellePatientenakte(st_et_vorname, st_et_nachname, st_tv_geburtsdatum,
                st_et_beschwerden, st_et_medikamente, st_et_notizen, st_erstelldatum);

        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void loescheContent(){
        et_vorname.setText("");
        et_nachname.setText("");
        tv_geburtsdatum.setText("Geburtsdatum");
        et_beschwerden.setText("");
        et_medikamente.setText("");
        et_notizen.setText("");
        tv_alter.setText("");
        tv_pflichtfeld.setVisibility(View.VISIBLE);

        rb_notizen_ja = (RadioButton) findViewById(R.id.rb_notizen_ja);
        rb_notizen_nein = (RadioButton) findViewById(R.id.rb_notizen_nein);
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
                return new DatePickerDialog(this, android.R.style.Theme_Wallpaper_NoTitleBar_Fullscreen, waehleGeburtsdatum, startJahrImDialog, startMonatImDialog, startTagImDialog);
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
        public void onDateSet(DatePicker view, int startJahrImDialog, int startMonatImDialog, int startTagImDialog) {
            ausgewahltesDatum.setDateOfBirth(startJahrImDialog, startMonatImDialog, startTagImDialog);
            berechneAlter();
            tv_geburtsdatum.setText(startTagImDialog + "." + (startMonatImDialog + 1) + "." +
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

    public String gibAktuellesDatum(){
        SimpleDateFormat datumsformatierung = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date datum = new Date();
        return datumsformatierung.format(datum);
    }
}
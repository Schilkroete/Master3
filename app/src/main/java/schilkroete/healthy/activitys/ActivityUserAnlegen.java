package schilkroete.healthy.activitys;

// TODO Rolle soll beim neuanlegen zurückgsetzt werden

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import schilkroete.healthy.R;
import schilkroete.healthy.datenbankzugriffe.DatenquelleUser;

import static android.graphics.Color.RED;

/**
 * Created by Schil on 10.04.2017.
 */

public class ActivityUserAnlegen extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = ActivityUserAnlegen.class.getSimpleName();
    public EditText et_vorname;
    public EditText et_nachname;
    public EditText et_passwort;
    public Spinner spinner;

    private DatenquelleUser datenquelleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_user_anlegen);

        erzeugeRelevanteFelder();
        aktiviereHinzufuegenButton();
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
                String st_et_passwort = et_passwort.getText().toString();
                String st_spinner_rolle = spinner.getSelectedItem().toString();
                TextView errorText = (TextView)spinner.getSelectedView();
                errorText.setError("");
                errorText.setTextColor(RED);

                if(TextUtils.isEmpty(st_et_nachname)) {
                    et_nachname.setError(getString(R.string.error_leeres_feld));
                    return;
                }
                if(TextUtils.isEmpty(st_et_vorname)) {
                    et_vorname.setError(getString(R.string.error_leeres_feld));
                    return;
                }
                if(TextUtils.isEmpty(st_et_passwort)) {
                    et_passwort.setError(getText(R.string.error_leeres_feld));
                    return;
                }
                if(st_spinner_rolle == "Bitte wählen Sie eine Rolle aus") {
                    errorText.setText(getText(R.string.error_leeres_feld));
                    return;
                }

                try {
                    android.app.AlertDialog.Builder alertDialogAbfrage = new
                            android.app.AlertDialog.Builder(ActivityUserAnlegen.this);

                    alertDialogAbfrage
                            .setIcon(android.R.drawable.ic_input_add)
                            .setTitle("Sicherheitsabfrage")
                            .setMessage("Möchten Sie die User anlegen?")
                            .setCancelable(false)
                            .setPositiveButton("Speichern und Fortfahren", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    speichereDatensatz();
                                    loescheContent();
                                    Intent ptSuchen = new Intent(ActivityUserAnlegen.this,
                                            ActivityUserSuchen.class);
                                    startActivity(ptSuchen);
                                }
                            })
                            .setNegativeButton("Nein, Inhalt bearbeiten",new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    })
                            .setNeutralButton("Speichern und neuen User erstellen", new
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
        String st_et_vorname = et_vorname.getText().toString();
        String st_et_nachname = et_nachname.getText().toString();
        String st_tv_passwort = et_passwort.getText().toString();
        String st_tv_rolle = spinner.getSelectedItem().toString();
        String st_erstelldatum = gibAktuellesDatum();

        datenquelleUser.erstelleUser(st_et_vorname, st_et_nachname, st_tv_passwort, st_tv_rolle,
                                        st_erstelldatum);

        InputMethodManager inputMethodManager;
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void loescheContent(){
        et_vorname.setText("");
        et_nachname.setText("");
        et_passwort.setText("");
    }

    public void erzeugeRelevanteFelder (){
        et_vorname = (EditText) findViewById(R.id.et_vorname);
        et_nachname = (EditText) findViewById(R.id.et_nachname);
        et_passwort = (EditText) findViewById(R.id.et_passwort);
        spinner = (Spinner) findViewById(R.id.spinner_user);

        Log.e(TAG, "Das Datenquellen-Objekt wird angelegt.");
        datenquelleUser = new DatenquelleUser(this);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.signup_user, android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Die Datenquelle wird geöffnet.");
        datenquelleUser.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Die Datenquelle wird geschlossen.");
        datenquelleUser.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view;
        Toast.makeText(this, "Sie haben " + textView.getText() + " ausgewaehlt!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String gibAktuellesDatum(){
        SimpleDateFormat datumsformatierung = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date datum = new Date();
        return datumsformatierung.format(datum);
    }
}

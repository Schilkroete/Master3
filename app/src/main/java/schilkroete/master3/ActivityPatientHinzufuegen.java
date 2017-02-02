package schilkroete.master3;

import android.app.Activity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Schilkroete on 02.02.2017.
 */


public class ActivityPatientHinzufuegen extends Activity implements View.OnClickListener {

    // TODO - Sollte es später Probleme bei den SharedPreferences geben: Zugriffsmodifizierer ändern, könnte helfen

    private TextView resultAlter;
    private TextView versteckePflichtfeld;
    private TextView geburtsdatum;
    static final int DATE_START_DIALOG_ID = 0;
    private int startJahr = 1975;
    private int startMonat = 6;
    private int startTag = 15;
    private AgeCalculation berechneAlter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_patienten_hinzufuegen);

        berechneAlter = new AgeCalculation();
        TextView aktuellesDatum = (TextView) findViewById(R.id.tv_aktuellesDatum);
        aktuellesDatum.setText(berechneAlter.getCurrentDate());
        geburtsdatum = (TextView) findViewById(R.id.tv_geburtsdatum);
        resultAlter = (TextView) findViewById(R.id.tv_alter);
        Button btnStart = (Button) findViewById(R.id.tv_waehleGeburtstag);
        btnStart.setOnClickListener(this);
        versteckePflichtfeld = (TextView) findViewById(R.id.tv_pflichtfeld);
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DATE_START_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, startJahr, startMonat, startTag);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener () {
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            startJahr = selectedYear;
            startMonat = selectedMonth;
            startTag = selectedDay;
            berechneAlter.setDateOfBirth(startJahr, startMonat, startTag);
            geburtsdatum.setText(selectedDay + "." + (startMonat + 1) + "." + startJahr);
            berechneAlter();
        }
    };

    public void onClick (View v){
        switch (v.getId()){
            case R.id.tv_waehleGeburtstag:
                showDialog(DATE_START_DIALOG_ID);
                break;
            default:
                break;
        }
    }

    private void berechneAlter() {
        berechneAlter.calculateYear();
        berechneAlter.calculateMonth();
        berechneAlter.calculateDay();
        Toast.makeText(getBaseContext(), "Das Alter entspricht: " + berechneAlter.getResult(),
                Toast.LENGTH_SHORT).show();
        resultAlter.setText("Alter: " + berechneAlter.getResult());

        if (resultAlter != null){
            versteckePflichtfeld.setVisibility(View.INVISIBLE);
        }
    }

}

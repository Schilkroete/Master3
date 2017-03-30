package schilkroete.master3;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ActivityDashboard extends Activity {

    Button btn;
    private static final String TAG = ActivityDashboard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);
        //Log.e(TAG, "onCreate() aufgerufen");


        //Log.e(TAG, "Button \"Patienten hinzufügen\" wurde erstellt/angelegt");
        btn = (Button) findViewById(R.id.btn_patienten_hinzufuegen);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {

                try {
                    android.app.AlertDialog.Builder alertDialogAbfrage = new
                            android.app.AlertDialog.Builder(ActivityDashboard.this);

                    alertDialogAbfrage
                            .setIcon(android.R.drawable.ic_input_add)
                            .setTitle("Sicherheitsabfrage")
                            .setMessage("Wählen Sie Ihre Rolle?")
                            .setCancelable(false)
                            .setPositiveButton("Arzt", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Log.e(TAG, "Button \"Patienten hinzufügen\" wurde geklickt");
                                    Intent ptHinzufuegen = new Intent(ActivityDashboard.this,
                                            ActivityPatientenHinzufuegen.class);
                                    startActivity(ptHinzufuegen);
                                }
                            })
                            .setNegativeButton("Ergotherapeut",new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Log.e(TAG, "Button \"Patienten hinzufügen\" wurde geklickt");
                                            Intent ptHinzufuegen = new Intent(ActivityDashboard.this,
                                                    ActivityPatientenHinzufuegen.class);
                                            startActivity(ptHinzufuegen);
                                        }
                                    })
                            .setNeutralButton("Physiotherapeut", new
                                    DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //Log.e(TAG, "Button \"Patienten hinzufügen\" wurde geklickt");
                                            Intent ptHinzufuegen = new Intent(ActivityDashboard.this,
                                                    ActivityPatientenHinzufuegen.class);
                                            startActivity(ptHinzufuegen);
                                        }
                                    });
                    android.app.AlertDialog alertDialog = alertDialogAbfrage.create();
                    alertDialog.show();
                } catch (Exception ex) {
                    Log.e(TAG, "AlertDialog Fehler");
                }



            }
        });


        //Log.e(TAG, "Button \"Patientensuche\" wurde erstellt/angelegt");
        btn = (Button) findViewById(R.id.btn_patientensuche);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {
                //Log.e(TAG, "Button \"Patientensuche\" wurde geklickt");
                Intent ptsuchen = new Intent(ActivityDashboard.this,
                        ActivityPatientenSuchen.class);
                startActivity(ptsuchen);
            }
        });


 /*        //Log.e(TAG, "Button \"Übungen\" wurde erstellt/angelegt");
        btn = (Button) findViewById(R.id.btn_uebungen);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {
                //Log.e(TAG, "Button \"Übungen\" wurde geklickt");
                Intent uebungen = new Intent(ActivityDashboard.this,
                        MainActivity.class);
                startActivity(uebungen);
            }
        });


       //Log.e(TAG, "Button \"Einstellungen\" wurde erstellt/angelegt");
        btn = (Button) findViewById(R.id.btn_einstellungen);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {
                //Log.e(TAG, "Button \"Einstellungen\" wurde geklickt");
                Intent einstellungen = new Intent(ActivityDashboard.this,
                        ActivityDashboard.class);
                startActivity(einstellungen);
            }
        });*/
    }
}
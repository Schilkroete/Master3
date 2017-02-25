package schilkroete.master3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityDashboard extends Activity {

    ImageButton btn;
    private static final String TAG = ActivityDashboard.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);
        //Log.e(TAG, "onCreate() aufgerufen");


        //Log.e(TAG, "Button \"Patienten hinzufügen\" wurde erstellt/angelegt");
        btn = (ImageButton) findViewById(R.id.btn_patienten_hinzufuegen);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {
                //Log.e(TAG, "Button \"Patienten hinzufügen\" wurde geklickt");
                Intent ptHinzufuegen = new Intent(ActivityDashboard.this,
                        ActivityPatientenHinzufuegen.class);
                startActivity(ptHinzufuegen);
            }
        });


        //Log.e(TAG, "Button \"Patientensuche\" wurde erstellt/angelegt");
        btn = (ImageButton) findViewById(R.id.btn_patientensuche);
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
        btn = (ImageButton) findViewById(R.id.btn_uebungen);
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
        btn = (ImageButton) findViewById(R.id.btn_einstellungen);
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
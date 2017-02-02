package schilkroete.master3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityDashboard extends Activity {

    ImageButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);


        btn = (ImageButton) findViewById(R.id.btn_Patienten_Hinzufuegen);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View n) {
                Intent suchePt = new Intent(ActivityDashboard.this,
                        ActivityPatientHinzufuegen.class);
                startActivity(suchePt);
            }
        });



    }




}
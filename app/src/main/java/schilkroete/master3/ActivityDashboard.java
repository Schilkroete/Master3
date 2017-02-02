package schilkroete.master3;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class ActivityDashboard extends Activity {

    ImageButton btn;
    boolean istGeklickt = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dashboard);


        // TODO Dies ist ein Test
        btn = (ImageButton) findViewById(R.id.btnSettings);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (istGeklickt) {
                    btn.setImageResource(R.drawable.change);
                } else {
                    btn.setImageResource(R.drawable.settings);
                }
                istGeklickt =! istGeklickt;
            }
        });

    }


}
package djamelfel.communitycargps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Anaelle on 18/10/2015.
 */
public class DisplaySettings extends Activity implements View.OnClickListener{

    private EditText choosenDistance;
    private Button bReset;
    private Button bSave;
    final String EXTRA_DISTANCE = "distance_voulue";

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.settings);
        choosenDistance = (EditText) findViewById(R.id.choosenDistance);
        bReset = (Button) findViewById(R.id.bReset);
        findViewById(R.id.bReset).setOnClickListener(this);
        bSave = (Button) findViewById(R.id.bSave);
        findViewById(R.id.bSave).setOnClickListener(this);
    }

    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bReset:
                choosenDistance.setText(String.valueOf(10));
                break;
            case R.id.bSave:
                Intent intentDisplaySettings = new Intent(DisplaySettings.this, Maps.class);
                intentDisplaySettings.putExtra(EXTRA_DISTANCE, choosenDistance.getText().toString());
                startActivity(intentDisplaySettings);
                break;
            default:
                break;
        }
    }
}

package lab03.eim.systems.cs.pub.ro.phonedialer;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class PhoneDialerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_dialer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setHandlers();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_phone_dialer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setHandlers() {

        final EditText editText = (EditText)findViewById(R.id.phone_number_edit_text);

        View.OnClickListener numberButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button button = (Button) v;
                editText.setText(editText.getText().toString() + button.getText().toString());
            }
        };

        View.OnClickListener backspaceButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextString = editText.getText().toString();

                if(editTextString.length() == 0)
                    return;

                editText.setText( editTextString.substring(0, editTextString.length()-1) );
            }
        };

        View.OnClickListener hangupButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                finish();
            }
        };

        View.OnClickListener callButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = editText.getText().toString();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(intent);
                } else {
                    // Iau eroare aici la castarea getApplicationContext la Activity (??)
                    //ActivityCompat.requestPermissions((Activity) getApplicationContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                    Log.d("[PhoneDialer]", "Application does not have calling permissions.");
                }
            }
        };

        for (int i=0; i<Constants.buttonIds.length; i++)
            findViewById(Constants.buttonIds[i]).setOnClickListener(numberButtonClickListener);

        findViewById(R.id.backspace_image_button).setOnClickListener(backspaceButtonClickListener);
        findViewById(R.id.hangup_image_button).setOnClickListener(hangupButtonClickListener);
        findViewById(R.id.call_image_button).setOnClickListener(callButtonClickListener);
    }
}

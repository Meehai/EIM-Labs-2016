package lab04.eim.systems.cs.pub.ro.contactsmanager;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    private Button saveButton, cancelButton;
    private ToggleButton additionalFieldsButton;
    private EditText nameText, phoneText, emailText, addressText, jobTitleText, companyNameText,
        websiteText, IMText;

    private void changeButtonsVisibility() {
        jobTitleText.setVisibility(additionalFieldsButton.isChecked() ?
                View.VISIBLE : View.GONE);
        companyNameText.setVisibility(additionalFieldsButton.isChecked() ?
                View.VISIBLE : View.GONE);
        websiteText.setVisibility(additionalFieldsButton.isChecked() ?
                View.VISIBLE : View.GONE);
        IMText.setVisibility(additionalFieldsButton.isChecked() ?
                View.VISIBLE : View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        additionalFieldsButton = (ToggleButton)findViewById(R.id.aditional_fields_button);
        saveButton = (Button)findViewById(R.id.save_button);
        cancelButton = (Button)findViewById(R.id.cancel_action);
        nameText = (EditText)findViewById(R.id.name_text);
        phoneText = (EditText)findViewById(R.id.phone_number_text);
        emailText = (EditText)findViewById(R.id.email_text);
        addressText = (EditText)findViewById(R.id.address_text);
        jobTitleText = (EditText)findViewById(R.id.job_title);
        companyNameText = (EditText)findViewById(R.id.company_name);
        websiteText = (EditText)findViewById(R.id.web_site);
        IMText = (EditText)findViewById(R.id.IM);

        if(savedInstanceState != null)
            this.changeButtonsVisibility();

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.aditional_fields_button:
                        changeButtonsVisibility();
                        break;
                    case R.id.cancel_action:
                        finish();
                        break;
                    case R.id.save_button:
                        
                }

            }
        };
        additionalFieldsButton.setOnClickListener(buttonListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onRestoreInstanceState(Bundle savedState) {
        super.onRestoreInstanceState(savedState);
        this.changeButtonsVisibility();
    }
}

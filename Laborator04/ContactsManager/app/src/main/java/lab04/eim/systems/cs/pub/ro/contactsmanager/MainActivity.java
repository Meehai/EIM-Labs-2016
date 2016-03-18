package lab04.eim.systems.cs.pub.ro.contactsmanager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

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

        Intent intent = getIntent();
        if (intent != null) {
            String phone = intent.getStringExtra("ro.pub.cs.systems.eim.lab04.contactsmanager.PHONE_NUMBER_KEY");
            if (phone != null) {
                phoneText.setText(phone);
            } else {
                Toast.makeText(this, getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
            }
        }

        View.OnClickListener buttonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()) {
                    case R.id.aditional_fields_button:
                        changeButtonsVisibility();
                        break;
                    case R.id.cancel_action:
                        setResult(Activity.RESULT_CANCELED, new Intent());
                        finish();
                        break;
                    case R.id.save_button:
                        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
                        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
                        if (nameText != null) {
                            intent.putExtra(ContactsContract.Intents.Insert.NAME,
                                    nameText.getText().toString());
                        }
                        if (phoneText != null) {
                            intent.putExtra(ContactsContract.Intents.Insert.PHONE,
                                    phoneText.getText().toString());
                        }
                        if (emailText != null) {
                            intent.putExtra(ContactsContract.Intents.Insert.EMAIL,
                                    emailText.getText().toString());
                        }
                        if (addressText != null) {
                            intent.putExtra(ContactsContract.Intents.Insert.POSTAL,
                                    addressText.getText().toString());
                        }
                        if (jobTitleText != null) {
                            intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE,
                                    jobTitleText.getText().toString());
                        }
                        if (companyNameText != null) {
                            intent.putExtra(ContactsContract.Intents.Insert.COMPANY,
                                    companyNameText.getText().toString());
                        }
                        ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
                        if (websiteText != null) {
                            ContentValues websiteRow = new ContentValues();
                            websiteRow.put(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
                            websiteRow.put(ContactsContract.CommonDataKinds.Website.URL,
                                    websiteText.getText().toString());
                            contactData.add(websiteRow);
                        }
                        if (IMText != null) {
                            ContentValues imRow = new ContentValues();
                            imRow.put(ContactsContract.Data.MIMETYPE,
                                    ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
                            imRow.put(ContactsContract.CommonDataKinds.Im.DATA,
                                    IMText.getText().toString());
                            contactData.add(imRow);
                        }
                        intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA,
                                contactData);
                        startActivityForResult(intent, Constants.CONTACTS_MANAGER_REQUEST_CODE);
                        break;
                }

            }
        };
        additionalFieldsButton.setOnClickListener(buttonListener);
        saveButton.setOnClickListener(buttonListener);
        cancelButton.setOnClickListener(buttonListener);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case Constants.CONTACTS_MANAGER_REQUEST_CODE:
                setResult(resultCode, new Intent());
                finish();
                break;
        }
    }
}

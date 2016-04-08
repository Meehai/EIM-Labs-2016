package practicaltest01var07.eim.systems.cs.pub.ro.practicaltest01var07;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

class ActivtyBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String data = null;

        if(action.equals("userMessage")){
            Log.d("TEST", "User name = " + intent.getStringExtra("userName"));
        }

        if(action.equals("groupMessage")){
            Log.d("TEST", "Group name = " + intent.getStringExtra("groupName"));
        }
    }
}

public class PracticalTest01Var07MainActivity extends Activity {

    private Button navigateButton;
    private EditText nameEditText, groupEditText;
    private CheckBox nameCheckBox, groupCheckBox;

    private NameCheckBoxListener nameCheckBoxListener;
    private EditCheckBoxListener groupCheckBoxListener;
    private ButtonClickListener navigateButtonListener;

    IntentFilter intentFilter;
    BroadcastReceiver startedServiceBroadcastReceiver;

    private class ButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), PracticalTest01var07SecondaryActivity.class);
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("group", groupEditText.getText().toString());
            startActivityForResult(intent, 12345);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 12345) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    private class NameCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            nameEditText.setEnabled(isChecked);
            Log.d("TEST", "Aici + group check = " + groupCheckBox.isChecked() + " nameCheck = " + nameCheckBox.isChecked());

            Intent intent = new Intent(getApplicationContext(), PracticalTest01Var07Service.class);
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("group", groupEditText.getText().toString());

            if(groupCheckBox.isChecked() && nameCheckBox.isChecked()){
                 startService(intent);
            }
            else {
                stopService(intent);
            }
        }
    }

    private class EditCheckBoxListener implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            groupEditText.setEnabled(isChecked);
            Log.d("TEST", "Aici + group check = " + groupCheckBox.isChecked() + " nameCheck = " + nameCheckBox.isChecked());

            Intent intent = new Intent(getApplicationContext(), PracticalTest01Var07Service.class);
            intent.putExtra("name", nameEditText.getText().toString());
            intent.putExtra("group", groupEditText.getText().toString());

            if(groupCheckBox.isChecked() && nameCheckBox.isChecked()){
                startService(intent);
            }
            else {
                stopService(intent);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var07_main);

        navigateButton = (Button)findViewById(R.id.left_button);
        nameEditText = (EditText)findViewById(R.id.name_edit_text);
        groupEditText = (EditText)findViewById(R.id.group_edit_text);
        nameCheckBox = (CheckBox)findViewById(R.id.name_checkbox);
        groupCheckBox = (CheckBox)findViewById(R.id.group_checkbox);

        nameCheckBoxListener = new NameCheckBoxListener();
        groupCheckBoxListener = new EditCheckBoxListener();
        navigateButtonListener = new ButtonClickListener();

        nameCheckBox.setOnCheckedChangeListener(nameCheckBoxListener);
        groupCheckBox.setOnCheckedChangeListener(groupCheckBoxListener);
        navigateButton.setOnClickListener(navigateButtonListener);

        if(savedInstanceState != null) {
            String s;

            if(savedInstanceState.containsKey("nameCheckBox")) {
                s = savedInstanceState.getString("nameCheckBox");
                if(s.equals("True")) {
                    nameCheckBox.setEnabled(true);
                }
            }

            if(savedInstanceState.containsKey("nameValue")) {
                s = savedInstanceState.getString("nameCheckBox");
                nameEditText.setText(s);
            }

            if(savedInstanceState.containsKey("groupCheckBox")){
                s = savedInstanceState.getString("nameCheckBox");
                if(s.equals("True")) {
                    groupCheckBox.setEnabled(true);
                }
            }

            if(savedInstanceState.containsKey("groupValue")) {
                s = savedInstanceState.getString("groupValue");
                groupEditText.setText(s);
            }

        }

        startedServiceBroadcastReceiver = new ActivtyBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction("userMessage");
        intentFilter.addAction("groupMessage");
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        String s;
        boolean v = nameCheckBox.isChecked();

        if(nameCheckBox.isChecked())
            savedInstanceState.putString("nameCheckBox", "True");
        if(groupCheckBox.isChecked())
            savedInstanceState.putString("groupCheckBox", "True");

        savedInstanceState.putString("nameValue", nameEditText.getText().toString());
        savedInstanceState.putString("groupValue", groupEditText.getText().toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test01_var07_main, menu);
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

    public void onDestroy() {
        Intent intent = new Intent(getApplicationContext(), PracticalTest01Var07Service.class);
        stopService(intent);
        super.onDestroy();
    }

    protected void onResume() {
        super.onResume();
        registerReceiver(startedServiceBroadcastReceiver, intentFilter);
    }

    protected void onPause() {
        unregisterReceiver(startedServiceBroadcastReceiver);
        super.onPause();
    }

}

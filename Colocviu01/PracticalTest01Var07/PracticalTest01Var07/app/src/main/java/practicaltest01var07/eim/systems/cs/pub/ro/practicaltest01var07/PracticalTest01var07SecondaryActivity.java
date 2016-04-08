package practicaltest01var07.eim.systems.cs.pub.ro.practicaltest01var07;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PracticalTest01var07SecondaryActivity extends Activity {

    private Button returnOkButton, returnCancelButton;
    private EditText nameEditText, groupEditText;
    private ButtonClickListener okCancelButtonListener;


    private class ButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ok_button:
                    setResult(RESULT_OK, null);
                    break;
                case R.id.cancel_button:
                    setResult(RESULT_CANCELED, null);
                    break;
            }

            finish();
        }
    }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_practical_test01_var07_secondary);

            nameEditText = (EditText) findViewById(R.id.nameSecondaryActivity);
            groupEditText = (EditText) findViewById(R.id.groupSecondaryActivity);
            returnOkButton = (Button) findViewById(R.id.ok_button);
            returnCancelButton = (Button) findViewById(R.id.cancel_button);

            okCancelButtonListener = new ButtonClickListener();
            returnOkButton.setOnClickListener(okCancelButtonListener);
            returnCancelButton.setOnClickListener(okCancelButtonListener);

            Intent intent = getIntent();
            if (intent != null && intent.getExtras().containsKey("name") && intent.getExtras().containsKey("group")) {
                String name = intent.getStringExtra("name");
                String group = intent.getStringExtra("group");

                nameEditText.setText(name);
                groupEditText.setText(group);
            }

        }
    }
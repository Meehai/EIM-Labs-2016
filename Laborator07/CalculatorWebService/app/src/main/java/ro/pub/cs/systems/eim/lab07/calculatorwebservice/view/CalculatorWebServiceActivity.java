package ro.pub.cs.systems.eim.lab07.calculatorwebservice.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab07.calculatorwebservice.R;

public class CalculatorWebServiceActivity extends AppCompatActivity {

    private EditText operator1EditText, operator2EditText;
    private TextView resultTextView;
    private Spinner operationsSpinner, methodsSpinner;
    private Button displayResultButton;

    private DisplayResultButtonClickListener displayResultButtonClickListener = new DisplayResultButtonClickListener();
    private class DisplayResultButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String operator1 = operator1EditText.getText().toString();
            String operator2 = operator2EditText.getText().toString();
            String operation = operationsSpinner.getSelectedItem().toString();
            String method = String.valueOf(methodsSpinner.getSelectedItemPosition());

            CalculatorWebServiceAsyncTask calculatorWebServiceAsyncTask = new CalculatorWebServiceAsyncTask();
            calculatorWebServiceAsyncTask.execute(operator1, operator2, operation, method);
        }
    }

    private class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String operator1 = params[0];
            String operator2 = params[1];
            String operation = params[2];
            int method = Integer.parseInt(params[3]);

            // TODO: exercise 4
            // signal missing values through error messages

            // create an instance of a HttpClient object

            // get method used for sending request from methodsSpinner

            // 1. GET
            // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
            // b) create an instance of a ResultHandler object
            // c) execute the request, thus generating the result

            // 2. POST
            // a) build the URL into a HttpPost object
            // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
            // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
            // d) create an instance of a ResultHandler object
            // e) execute the request, thus generating the result

            return null;
        }

        @Override
        public void onPostExecute(String result) {
            // display the result in resultTextView
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_web_service);

        operator1EditText = (EditText)findViewById(R.id.operator1_edit_text);
        operator2EditText = (EditText)findViewById(R.id.operator2_edit_text);

        resultTextView = (TextView)findViewById(R.id.result_text_view);

        operationsSpinner = (Spinner)findViewById(R.id.operations_spinner);
        methodsSpinner = (Spinner)findViewById(R.id.methods_spinner);

        displayResultButton = (Button) findViewById(R.id.display_result_button);
        displayResultButton.setOnClickListener(displayResultButtonClickListener);
    }
}

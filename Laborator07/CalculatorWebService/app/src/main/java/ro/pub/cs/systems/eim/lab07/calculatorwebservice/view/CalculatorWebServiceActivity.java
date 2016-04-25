package ro.pub.cs.systems.eim.lab07.calculatorwebservice.view;

import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.systems.eim.lab07.calculatorwebservice.R;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

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
            try {
                Double.parseDouble(operator1);
                Double.parseDouble(operator2);
            } catch (NumberFormatException e) {
                return "One of the numbers is not valid.";
            }

            // create an instance of a HttpClient object
            HttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpResponseEntity = null;

            // get method used for sending request from methodsSpinner
            if(method == Constants.GET_OPERATION) {
                // 1. GET
                // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
                // b) create an instance of a ResultHandler object
                // c) execute the request, thus generating the result
                HttpGet httpGet = new HttpGet(Constants.GET_WEB_SERVICE_ADDRESS
                        + "?" + Constants.OPERATION_ATTRIBUTE + "=" + operation
                        + "&" + Constants.OPERATOR1_ATTRIBUTE + "=" + operator1
                        + "&" + Constants.OPERATOR2_ATTRIBUTE + "=" + operator2);

                try {
                    HttpResponse httpGetResponse = httpClient.execute(httpGet);
                    httpResponseEntity = httpGetResponse.getEntity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(method == Constants.POST_OPERATION) {
                // 2. POST
                // a) build the URL into a HttpPost object
                // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
                // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
                // d) create an instance of a ResultHandler object
                // e) execute the request, thus generating the result

                HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
                List<NameValuePair> postParams = new ArrayList<NameValuePair>();
                postParams.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
                postParams.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1));
                postParams.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2));
                UrlEncodedFormEntity urlEncodedFormEntity;

                try {
                    urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
                    httpPost.setEntity(urlEncodedFormEntity);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    HttpResponse httpPostResponse = httpClient.execute(httpPost);
                    httpResponseEntity = httpPostResponse.getEntity();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(httpResponseEntity == null)
                return null;

            StringBuilder result = new StringBuilder();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponseEntity.getContent()));
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    result.append(currentLine).append("\n");
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }

            return result.toString();
        }

        @Override
        public void onPostExecute(String result) {
            resultTextView.setText(result);
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

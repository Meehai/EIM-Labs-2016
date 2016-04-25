package ro.pub.cs.systems.eim.lab07.googlesearcher.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.HttpCookie;

import ro.pub.cs.systems.eim.lab07.googlesearcher.R;
import ro.pub.cs.systems.eim.lab07.googlesearcher.general.Constants;

public class GoogleSearcherActivity extends AppCompatActivity {

    private EditText keywordEditText;
    private WebView googleResultsWebView;
    private Button searchGoogleButton;

    private SearchGoogleButtonClickListener searchGoogleButtonClickListener = new SearchGoogleButtonClickListener();
    private class SearchGoogleButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            // obtain the keyword from keywordEditText
            String query = keywordEditText.getText().toString();
            // signal an empty keyword through an error message displayed in a Toast window
            if(query.isEmpty()){
                Toast.makeText(getApplicationContext(), "Empty string provided", Toast.LENGTH_LONG).show();
                return;
            }
            // split a multiple word (separated by space) keyword and link them through +
            // prepend the keyword with "search?q=" string
            query = Constants.GOOGLE_INTERNET_ADDRESS + "search?q=" + query.replace(" ", "+");

            // start the GoogleSearcherAsyncTask passing the keyword
            new GoogleSearcherAsyncTask().execute(query);

        }
    }

    private class GoogleSearcherAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            // exercise 6b)
            // create an instance of a HttpClient object
            HttpClient httpClient = new DefaultHttpClient();
            // create an instance of a HttpGet object, encapsulating the base Internet address (http://www.google.com) and the keyword
            HttpGet httpGet = new HttpGet(params[0]);
            // create an instance of a ResponseHandler object
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String content = null;

            try {
                // execute the request, thus generating the result
                content = httpClient.execute(httpGet, responseHandler);
            } catch (IOException e ) {
                e.printStackTrace();
            }

            return content;
        }

        @Override
        public void onPostExecute(String content) {

            if(content == null)
                return;
            // exercise 6b)
            googleResultsWebView.loadDataWithBaseURL(Constants.GOOGLE_INTERNET_ADDRESS, content, "text/html", "UTF-8", null);
            // display the result into the googleResultsWebView through loadDataWithBaseURL() method
            // - base Internet address is http://www.google.com
            // - page source code is the response
            // - mimetype is text/html
            // - encoding is UTF-8
            // - history is null

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_searcher);

        keywordEditText = (EditText) findViewById(R.id.keyword_edit_text);
        googleResultsWebView = (WebView) findViewById(R.id.google_results_web_view);

        searchGoogleButton = (Button) findViewById(R.id.search_google_button);
        searchGoogleButton.setOnClickListener(searchGoogleButtonClickListener);
    }
}

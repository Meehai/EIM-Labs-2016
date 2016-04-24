package ro.pub.cs.systems.eim.lab07.googlesearcher.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import ro.pub.cs.systems.eim.lab07.googlesearcher.R;

public class GoogleSearcherActivity extends AppCompatActivity {

    private EditText keywordEditText;
    private WebView googleResultsWebView;
    private Button searchGoogleButton;

    private SearchGoogleButtonClickListener searchGoogleButtonClickListener = new SearchGoogleButtonClickListener();
    private class SearchGoogleButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            // TODO: exercise 6a)
            // obtain the keyword from keywordEditText
            // signal an empty keyword through an error message displayed in a Toast window
            // split a multiple word (separated by space) keyword and link them through +
            // prepend the keyword with "search?q=" string
            // start the GoogleSearcherAsyncTask passing the keyword

        }
    }

    private class GoogleSearcherAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            // TODO: exercise 6b)
            // create an instance of a HttpClient object
            // create an instance of a HttpGet object, encapsulating the base Internet address (http://www.google.com) and the keyword
            // create an instance of a ResponseHandler object
            // execute the request, thus generating the result

            return null;
        }

        @Override
        public void onPostExecute(String content) {

            // TODO: exercise 6b)
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

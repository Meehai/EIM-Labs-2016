package ro.pub.cs.systems.eim.lab07.landmarklister.view;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

import ro.pub.cs.systems.eim.lab07.landmarklister.R;
import ro.pub.cs.systems.eim.lab07.landmarklister.general.Constants;
import ro.pub.cs.systems.eim.lab07.landmarklister.model.LandmarkInformation;

public class LandmarkListerActivity extends AppCompatActivity {

    private EditText northEditText, southEditText, eastEditText, westEditText;
    private Button showResultsButton;
    private ListView landmarksListView;

    private ShowResultsButtonClickListener showResultsButtonClickListener = new ShowResultsButtonClickListener();
    private class ShowResultsButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String northString = northEditText.getText().toString();
            if (northString == null || northString.isEmpty()) {
                Toast.makeText(getApplication(), Constants.MISSING_INFORMATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                return;
            }
            String southString = southEditText.getText().toString();
            if (southString == null || southString.isEmpty()) {
                Toast.makeText(getApplication(), Constants.MISSING_INFORMATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                return;
            }
            String eastString = eastEditText.getText().toString();
            if (eastString == null || eastString.isEmpty()) {
                Toast.makeText(getApplication(), Constants.MISSING_INFORMATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                return;
            }
            String westString = westEditText.getText().toString();
            if (westString == null || westString.isEmpty()) {
                Toast.makeText(getApplication(), Constants.MISSING_INFORMATION_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
                return;
            }

            new LandmarkListerAsyncTask().execute(northString, southString, eastString, westString);
        }
    }

    private class LandmarkListerAsyncTask extends AsyncTask<String, Void, List<LandmarkInformation>> {

        @Override
        protected List<LandmarkInformation> doInBackground(String... params) {

            // TODO: exercise 7
            // - create an instance of a HttpClient object
            // - create the URL to the web service, appending the bounding box coordinates and the username to the base Internet address
            // - create an instance of a HttGet object
            // - create an instance of a ReponseHandler object
            // - execute the request, thus obtaining the response
            // - get the JSON object representing the response
            // - get the JSON array (the value corresponding to the "geonames" attribute)
            // - iterate over the results list and create a LandmarkInformation for each element

            return null;
        }

        @Override
        public void onPostExecute(List<LandmarkInformation> landmarkInformationList) {

            // TODO: exercise 7
            // create a LandmarkInformationAdapter with the array and attach it to the landmarksListView

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_lister);

        northEditText = (EditText)findViewById(R.id.north_edit_text);
        southEditText = (EditText)findViewById(R.id.south_edit_text);
        eastEditText = (EditText)findViewById(R.id.east_edit_text);
        westEditText = (EditText)findViewById(R.id.west_edit_text);

        landmarksListView = (ListView)findViewById(R.id.landmarks_list_view);

        showResultsButton = (Button)findViewById(R.id.show_results_button);
        showResultsButton.setOnClickListener(showResultsButtonClickListener);
    }
}

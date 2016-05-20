package ro.pub.cs.systems.eim.lab10.googlemapplaces.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import ro.pub.cs.systems.eim.lab10.R;
import ro.pub.cs.systems.eim.lab10.googlemapplaces.controller.PlacesAdapter;
import ro.pub.cs.systems.eim.lab10.googlemapplaces.general.Constants;
import ro.pub.cs.systems.eim.lab10.googlemapplaces.general.Utilities;
import ro.pub.cs.systems.eim.lab10.googlemapplaces.model.Place;

public class GoogleMapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap googleMap = null;
    private GoogleApiClient googleApiClient = null;

    private EditText latitudeEditText = null;
    private EditText longitudeEditText = null;
    private Button navigateToLocationButton = null;

    private EditText nameEditText = null;

    private Spinner markerTypeSpinner = null;

    private Spinner placesSpinner = null;
    private ArrayList<Place> places = null;
    private PlacesAdapter placesAdapter = null;

    private Button addPlaceButton = null;
    private Button clearPlacesButton = null;

    private AddPlaceButtonListener addPlaceButtonListener = new AddPlaceButtonListener();

    private class AddPlaceButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // exercise 6a
            // check whether latitude, longitude and name are filled, otherwise long an error

            String latitudeContent = latitudeEditText.getText().toString();
            String longitudeContent = longitudeEditText.getText().toString();
            String nameEditTextContent = nameEditText.getText().toString();

            if(latitudeContent == null || latitudeContent.isEmpty() ||
                    longitudeContent == null || longitudeContent.isEmpty() ||
                    nameEditTextContent == null || nameEditTextContent.isEmpty() ) {
                Toast.makeText(GoogleMapsActivity.this, "GPS coordinates or name not filled", Toast.LENGTH_LONG).show();
                return;
            }

            // navigate to the requested position (latitude, longitude)
            double latitudeValue = Double.parseDouble(latitudeContent);
            double longitudeValue = Double.parseDouble(longitudeContent);
            navigateToLocation(latitudeValue, longitudeValue);

            // create a MarkerOptions object with position, title and icon taken from the corresponding widgets
            MarkerOptions marker = new MarkerOptions()
                    .position(new LatLng(
                            latitudeValue,
                            longitudeValue
                    ))
                    .title(nameEditTextContent);
            marker.icon(BitmapDescriptorFactory.defaultMarker(Utilities.getDefaultMarker(markerTypeSpinner.getSelectedItemPosition())));

            // add the MarkerOptions to the Google Map
            googleMap.addMarker(marker);

            // add the Place information to the places list
            places.add(new Place(latitudeValue, longitudeValue, nameEditTextContent, Utilities.getDefaultMarker(markerTypeSpinner.getSelectedItemPosition())));

            // notify the placesAdapter that the data set was changed
            placesAdapter.notifyDataSetChanged();
        }
    }

    private ClearPlacesButtonListener clearPlacesButtonListener = new ClearPlacesButtonListener();

    private class ClearPlacesButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            // exercise 6b

            // check whether there are markers on the Google Map, otherwise log an error
            if(places == null || places.isEmpty()) {
                Toast.makeText(GoogleMapsActivity.this, "No markers avalaible on the map", Toast.LENGTH_LONG).show();
                return;
            }

            // clear the Google Map
            // clear the places List
            // notify the placesAdapter that the data set was changed
            googleMap.clear();
            places.clear();
            placesAdapter.notifyDataSetChanged();
        }
    }

    private NavigateToLocationButtonListener navigateToLocationButtonListener = new NavigateToLocationButtonListener();

    private class NavigateToLocationButtonListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String latitudeContent = latitudeEditText.getText().toString();
            String longitudeContent = longitudeEditText.getText().toString();

            if (latitudeContent == null || latitudeContent.isEmpty() ||
                    longitudeContent == null || longitudeContent.isEmpty()) {
                Toast.makeText(GoogleMapsActivity.this, "GPS coordinates should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }

            double latitudeValue = Double.parseDouble(latitudeContent);
            double longitudeValue = Double.parseDouble(longitudeContent);
            navigateToLocation(latitudeValue, longitudeValue);
        }
    }

    private PlacesSpinnerListener placesSpinnerListener = new PlacesSpinnerListener();

    private class PlacesSpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            Place place = (Place) placesAdapter.getItem(position);
            double latitude = place.getLatitude();
            double longitude = place.getLongitude();
            latitudeEditText.setText(String.valueOf(latitude));
            longitudeEditText.setText(String.valueOf(longitude));
            nameEditText.setText(place.getName());
            navigateToLocation(latitude, longitude);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }

    }

    private void navigateToLocation(double latitude, double longitude) {
        latitudeEditText.setText(String.valueOf(latitude));
        longitudeEditText.setText(String.valueOf(longitude));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)
        ).zoom(Constants.CAMERA_ZOOM)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(Constants.TAG, "onCreate() callback method was invoked");
        setContentView(R.layout.activity_google_maps);

        latitudeEditText = (EditText) findViewById(R.id.latitude_edit_text);
        longitudeEditText = (EditText) findViewById(R.id.longitude_edit_text);
        navigateToLocationButton = (Button) findViewById(R.id.navigate_to_location_button);
        navigateToLocationButton.setOnClickListener(navigateToLocationButtonListener);

        nameEditText = (EditText) findViewById(R.id.name_edit_text);

        markerTypeSpinner = (Spinner) findViewById(R.id.marker_type_spinner);

        placesSpinner = (Spinner) findViewById(R.id.places_spinner);
        places = new ArrayList<Place>();
        placesAdapter = new PlacesAdapter(this, places);
        placesSpinner.setAdapter(placesAdapter);
        placesSpinner.setOnItemSelectedListener(placesSpinnerListener);

        addPlaceButton = (Button) findViewById(R.id.add_place_button);
        addPlaceButton.setOnClickListener(addPlaceButtonListener);

        clearPlacesButton = (Button) findViewById(R.id.clear_places_button);
        clearPlacesButton.setOnClickListener(clearPlacesButtonListener);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        Log.i(Constants.TAG, "onStart() callback method was invoked");
        super.onStart();
        googleApiClient.connect();
        if (googleMap == null) {
            ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap readyGoogleMap) {
                    googleMap = readyGoogleMap;
                }
            });
        }
    }

    @Override
    protected void onStop() {
        Log.i(Constants.TAG, "onStop() callback method was invoked");
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "onDestroy() callback method was invoked");
        googleApiClient = null;
        super.onDestroy();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(Constants.TAG, "onConnected() callback method has been invoked");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(Constants.TAG, "onConnectionSuspended() callback method has been invoked");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(Constants.TAG, "onConnectionFailed() callback method has been invoked");
    }
}

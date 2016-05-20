package ro.pub.cs.systems.eim.lab10.googlemapsgeocoding.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import ro.pub.cs.systems.eim.lab10.googlemapsgeocoding.general.Constants;

public class GetLocationAddressIntentService extends IntentService {

    protected ResultReceiver resultReceiver = null;

    public GetLocationAddressIntentService() {
        super(Constants.TAG);
        Log.i(Constants.TAG, "GetLocationAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(Constants.TAG, "onHandleIntent() callback method has been invoked");

        String errorMessage = null;

        resultReceiver = intent.getParcelableExtra(Constants.RESULT_RECEIVER);
        if (resultReceiver == null) {
            errorMessage = "No result receiver was provided to handle the information";
            Log.e(Constants.TAG, "An exception has occurred: " + errorMessage);
            return;
        }

        Location location = intent.getParcelableExtra(Constants.LOCATION);
        if (location == null) {
            errorMessage = "No location data was provided";
            Log.e(Constants.TAG, "An exception has occurred: " + errorMessage);
            handleResult(Constants.RESULT_FAILURE, errorMessage);
            return;
        }

        List<Address> addressList = null;

        // exercise 8
        // instantiate a Geocoder object
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), Constants.NUMBER_OF_ADDRESSES);
            // get the addresses list by calling the getFromLocation() method on Geocoder (supply latitude, longitude, Constants.NUMBER_OF_ADDRESSES)
        } catch (IOException | IllegalArgumentException e) {
            // handle IOException and IllegalArgumentException properly as well as no results supplied situation
            e.printStackTrace();
        }

        if (errorMessage != null && !errorMessage.isEmpty()) {
            handleResult(Constants.RESULT_FAILURE, errorMessage);
            return;
        }

        if (addressList == null || addressList.isEmpty()) {
            errorMessage = "The geocoder could not find an address for the given latitude / longitude";
            Log.e(Constants.TAG, "An exception has occurred: " + errorMessage);
            handleResult(Constants.RESULT_FAILURE, errorMessage);
            return;
        }

        // iterate over the address list
        StringBuffer result = new StringBuffer();
        // concatenate all lines from each address (number of lines: getMaxAddressLineIndex(); specific line: getAddressLine()
        for (Address a : addressList) {
            for (int k=0; k<a.getMaxAddressLineIndex(); k++) {
                result.append(a.getAddressLine(k) + "\n");
            }
            result.append("\n");
        }

        // call handleResult method with result (Constants.RESULT_SUCCESS, Constants.RESULT_FAILURE) and the address details / error message
        handleResult(Constants.RESULT_SUCCESS, result.toString());
    }

    private void handleResult(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT, message);
        resultReceiver.send(resultCode, bundle);
    }

}
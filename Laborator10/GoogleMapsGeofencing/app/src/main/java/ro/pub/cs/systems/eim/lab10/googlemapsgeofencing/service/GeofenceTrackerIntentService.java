package ro.pub.cs.systems.eim.lab10.googlemapsgeofencing.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import ro.pub.cs.systems.eim.lab10.R;
import ro.pub.cs.systems.eim.lab10.googlemapsgeofencing.general.Constants;
import ro.pub.cs.systems.eim.lab10.googlemapsgeofencing.graphicuserinterface.GoogleMapsGeofenceEventActivity;

public class GeofenceTrackerIntentService extends IntentService {

    public GeofenceTrackerIntentService() {
        super(Constants.TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // exercise 9
        // obtain GeofencingEvent from the calling intent, using GeofencingEvent.fromIntent(intent);
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        // check whether the GeofencingEvent hasError(), log it and exit the method
        if(geofencingEvent.hasError()) {
            Log.d(Constants.TAG, "Geofencing error. " + geofencingEvent.getErrorCode());
            return;
        }

        // get the geofence transition using getGeofenceTransition() method
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // build a detailed message
        StringBuilder message = new StringBuilder();
        message.append("Transition Type: ");
        // - include the transition type
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER)
            message.append("Enter geofence area\n");
        else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT)
            message.append("Exit geofence area\n");
        else
            message.append("Unknown\n");

        // - include the request identifier (getRequestId()) for each  geofence that triggered the event (getTriggeringGeofences())
        for (Geofence g : geofencingEvent.getTriggeringGeofences()) {
            message.append("Request id = " + g.getRequestId() + "\n");
        }
        // send a notification with the detailed message (sendNotification())
        sendNotification(message.toString());
    }

    private void sendNotification(String notificationDetails) {
        Intent notificationIntent = new Intent(getApplicationContext(), GoogleMapsGeofenceEventActivity.class);
        notificationIntent.putExtra(Constants.NOTIFICATION_DETAILS, notificationDetails);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(GoogleMapsGeofenceEventActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle(Constants.GEOFENCE_TRANSITION_EVENT)
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}

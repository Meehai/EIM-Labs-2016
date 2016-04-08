package ro.pub.cs.systems.eim.lab05.boundedserviceactivity.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Timestamp;

import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.R;
import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.general.Constants;
import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.service.BoundedService;

public class BoundedServiceActivity extends AppCompatActivity {

    private TextView messageFromServiceTextView;
    private Button getMessageFromServiceButton;

    private BoundedService boundedService;
    private int boundedServiceStatus;

    private GetMessageFromServiceButtonListener getMessageFromServiceButtonListener = new GetMessageFromServiceButtonListener();
    private class GetMessageFromServiceButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (boundedService != null && boundedServiceStatus == Constants.SERVICE_STATUS_BOUND) {
                messageFromServiceTextView.setText("[" + new Timestamp(System.currentTimeMillis()) + "] " + boundedService.getMessage() + "\n" + messageFromServiceTextView.getText().toString());
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounded_service);
        Log.d(Constants.TAG, "onCreate() method from activity was invoked");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageFromServiceTextView = (TextView)findViewById(R.id.message_from_service_text_view);
        getMessageFromServiceButton = (Button)findViewById(R.id.get_message_from_service_button);
        getMessageFromServiceButton.setOnClickListener(getMessageFromServiceButtonListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(Constants.TAG, "onStart() method was invoked");
        Intent intent = new Intent(this, BoundedService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(Constants.TAG, "onStop() method was invoked");
        if (boundedServiceStatus == Constants.SERVICE_STATUS_BOUND) {
            unbindService(serviceConnection);
            boundedServiceStatus = Constants.SERVICE_STATUS_UNBOUND;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(Constants.TAG, "onServiceConnected() method was invoked");
            BoundedService.BoundedServiceBinder binder = (BoundedService.BoundedServiceBinder)service;
            boundedService = binder.getService();
            boundedServiceStatus = Constants.SERVICE_STATUS_BOUND;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.d(Constants.TAG, "onServiceDisconnected() method was invoked");
            boundedService = null;
            boundedServiceStatus = Constants.SERVICE_STATUS_UNBOUND;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bounded, menu);
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
}

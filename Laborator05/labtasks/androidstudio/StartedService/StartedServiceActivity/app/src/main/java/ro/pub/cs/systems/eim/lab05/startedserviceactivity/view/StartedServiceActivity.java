package ro.pub.cs.systems.eim.lab05.startedserviceactivity.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.startedserviceactivity.R;
import ro.pub.cs.systems.eim.lab05.startedserviceactivity.general.Constants;

public class StartedServiceActivity extends AppCompatActivity {

    private TextView messageTextView;
    private StartedServiceBroadcastReceiver startedServiceBroadcastReceiver;
    private IntentFilter startedServiceIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_started_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageTextView = (TextView)findViewById(R.id.message_text_view);

        // TODO: exercise 7a - create an instance of the StartedServiceBroadcastReceiver
        startedServiceBroadcastReceiver = new StartedServiceBroadcastReceiver(messageTextView);

        // TODO: exercise 7b - create an instance of the IntentFilter
        // with the corresponding actions of the broadcast intents
        startedServiceIntentFilter = new IntentFilter();
        startedServiceIntentFilter.addAction(Constants.ACTION_STRING);
        startedServiceIntentFilter.addAction(Constants.ACTION_INTEGER);
        startedServiceIntentFilter.addAction(Constants.ACTION_ARRAY_LIST);

        // TODO: exercise 7d - start the service
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("ro.pub.cs.systems.eim.lab05.startedservice", "ro.pub.cs.systems.eim.lab05.startedservice.service.StartedService"));
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(startedServiceBroadcastReceiver, startedServiceIntentFilter);
        // TODO: exercise 7c - register the broadcast receiver for the intent filter actions
    }

    @Override
    protected void onPause() {
        // TODO: exercise 7c - unregister the broadcast receiver
        unregisterReceiver(startedServiceBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("ro.pub.cs.systems.eim.lab05.startedservice", "ro.pub.cs.systems.eim.lab05.startedservice.service.StartedService"));
        stopService(intent);
        super.onDestroy();
    }

    // TODO: exercise 8 - implement the onNewIntent() callback method

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_started, menu);
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

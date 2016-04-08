package ro.pub.cs.systems.eim.lab05.boundedserviceactivity.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.R;
import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.general.Constants;
import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.service.BoundedService;

public class BoundedServiceActivity extends AppCompatActivity {

    private TextView messageFromServiceTextView;
    private Button getMessageFromServiceButton;

    private BoundedService boundedService;
    private int boundedServiceStatus;

    // TODO: exercise 9e - implement a button click listener for getMessageFromServiceButton

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bounded_service);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        messageFromServiceTextView = (TextView)findViewById(R.id.message_from_service_text_view);
        getMessageFromServiceButton = (Button)findViewById(R.id.get_message_from_service_button);
        // TODO: exercise 9e - set an instance of the button click listener to handle click events
        // for getMessageFromServiceButton
    }

    @Override
    protected void onStart() {
        super.onStart();
        // TODO: exercise 9d - bind the service through an intent
    }

    @Override
    protected void onStop() {
        super.onStop();
        // TODO: exercise 9d - unbind the service
    }

    // TODO: exercise 9c - create a ServiceConnection object
    // override methods onServiceConnected() and onServiceDisconnected()

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

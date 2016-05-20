package colocviu2example.eim.systems.cs.pub.ro.colocviu2example;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;


public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText serverPortEditText, clientAddressEditText, clientPortEditText, clientCityEditText;
    Spinner clientSpinner;
    Button serverConnectButton, clientGetWeatherButton;
    boolean isRunning = false;
    ServerThread serverThread = null;

    public class CommunicationThread extends Thread {
        private Socket socket;

        public CommunicationThread(Socket socket) {
            if(socket != null)
                this.socket = socket;
        }

        public void run() {

            BufferedReader reader = Utilities.getReader(socket);
            PrintStream writer = Utilities.getWriter(socket);
        }
    }

    public class ServerThread extends Thread {
        private ServerSocket serverSocket;

        public ServerThread(int port) {
            isRunning = true;
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    CommunicationThread communicationThread = new CommunicationThread(socket);
                    communicationThread.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectButtonClickListener implements Button.OnClickListener {
        public void onClick(View view) {
            if(!isRunning) {
                serverThread = new ServerThread(Integer.parseInt(serverPortEditText.getText().toString()));
            }

            isRunning = !isRunning;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        serverPortEditText = (EditText)findViewById(R.id.server_port);
        clientAddressEditText = (EditText)findViewById(R.id.client_address);
        clientPortEditText = (EditText)findViewById(R.id.client_port);
        clientCityEditText = (EditText)findViewById(R.id.client_city);
        clientSpinner = (Spinner)findViewById(R.id.client_spinner);
        serverConnectButton = (Button)findViewById(R.id.server_connect);
        clientGetWeatherButton = (Button)findViewById(R.id.client_get_forecast);

        clientGetWeatherButton.setOnClickListener(new ConnectButtonClickListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test02_main, menu);
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

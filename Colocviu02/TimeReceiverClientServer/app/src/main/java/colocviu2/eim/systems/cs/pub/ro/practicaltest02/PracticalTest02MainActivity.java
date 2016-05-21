package colocviu2.eim.systems.cs.pub.ro.practicaltest02;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class PracticalTest02MainActivity extends AppCompatActivity {

    EditText serverPortEditText;
    Button serverStart, clientConnect;
    TextView clientTimeContent;
    public ServerThread serverThread = null;
    public static final String TAG = "[Colocviu2]";

    class ServerStartListener implements Button.OnClickListener {
        public void onClick(View v) {
            if(serverThread == null) {
                serverThread = new ServerThread(Integer.parseInt(serverPortEditText.getText().toString()));
                serverThread.start();
                serverStart.setText("Stop server");
            }
            else {
                serverThread.stopThread();
                serverStart.setText("Start server");
            }
        }
    }

    class ClientConnectListener implements Button.OnClickListener {
        public void onClick(View v) {
            new ClientThread("127.0.0.1", Integer.parseInt(serverPortEditText.getText().toString())).start();
        }
    }

    class TimeClass {
        long time = -1;

        public TimeClass() {
            this.time = System.currentTimeMillis();
        }

        public long getTime() {
            return this.time;
        }
    }

    /* Client class => connect to server thread and read the receiving data. Update the UI using post method */
    class ClientThread extends Thread {

        String address;
        int port;

        public ClientThread(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public void run() {
            try {
                Socket socket = new Socket(address, port);
                if(socket == null) {
                    BufferedReader bufferedReader = Utilities.getReader(socket);

                    if(bufferedReader == null) {
                        clientTimeContent.post(new Runnable() {
                            public void run() {
                                Log.d(TAG, "Error at reading from server.");
                                clientTimeContent.setText("Error at reading from server");
                            }
                        });
                    }

                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        Log.d(TAG, "[client] Received data " + line);
                        final String final_line = line;

                        clientTimeContent.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "Setting text: " + final_line);
                                clientTimeContent.setText(final_line);
                            }
                        });
                    }

                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    class ServerThread extends Thread {

        ServerSocket serverSocket = null;
        TreeMap<String, TimeClass> listClients;
        public ServerThread(int port) {
           /* Comparator<Map.Entry<String, TimeClass>> comparator = new Comparable<Map.Entry<String, TimeClass>>() {
                public int compareTo(Map.Entry<String, TimeClass> o1, Map.Entry<String, TimeClass> o2) {
                    return o1.getKey().equals(o2.getKey()) ? 0 : 1;
                }
            };*/

            listClients = new TreeMap<String, TimeClass>();

            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        public void run() {

            if(serverSocket == null) {
                Log.d(TAG, "Server not running.");
                return;
            }
            Log.d(TAG, "Server is now running");

            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (socket == null) {
                    Log.d(TAG, "Null socket was received.");
                    continue;
                }

                InetAddress socketAddress = socket.getInetAddress();
                Log.d(TAG, "[server] accepted client at ip: " + socketAddress.getHostAddress());
                if (this.listClients.containsKey(socketAddress.getHostAddress())) {
                    TimeClass clientTime = this.listClients.get(socketAddress.getHostAddress());
                    if (System.currentTimeMillis() - clientTime.getTime() < 60) {
                        Utilities.getWriter(socket).println("Another request was done less than 60s ago!");
                        this.listClients.put(socketAddress.getHostAddress(), new TimeClass());
                        Log.d(TAG, "Another request was done less than 60s ago!");
                        continue;
                    }
                }

                this.listClients.put(socketAddress.getHostAddress(), new TimeClass());
                Utilities.getWriter(socket).println(serviceGetTime());
                try {
                    socket.close();
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }

        public void stopThread() {
            if (serverSocket != null) {
                interrupt();
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        public String serviceGetTime() {
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://www.timeapi.org/utc/now");
                HttpResponse httpGetResponse = httpClient.execute(httpGet);
                HttpEntity httpGetEntity = httpGetResponse.getEntity();
                if (httpGetEntity != null) {
                    // do something with the response
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String content = httpClient.execute(httpGet, responseHandler);
                    Log.d(TAG, "[server] returning data from service: " + content);
                    return content;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        serverPortEditText = (EditText)findViewById(R.id.server_port);
        serverStart = (Button)findViewById(R.id.server_start);
        clientConnect = (Button)findViewById(R.id.client_connect);
        clientTimeContent = (TextView)findViewById(R.id.client_content);

        serverStart.setOnClickListener(new ServerStartListener());
        clientConnect.setOnClickListener(new ClientConnectListener());
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

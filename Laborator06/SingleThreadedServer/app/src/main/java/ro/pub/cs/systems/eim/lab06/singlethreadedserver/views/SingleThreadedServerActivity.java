package ro.pub.cs.systems.eim.lab06.singlethreadedserver.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.lab06.singlethreadedserver.R;
import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Constants;
import ro.pub.cs.systems.eim.lab06.singlethreadedserver.general.Utilities;

public class SingleThreadedServerActivity extends AppCompatActivity {

    private EditText serverTextEditText;
    private ToggleButton serverToggleButton;

    private ServerTextContentWatcher serverTextContentWatcher;
    private ServerToggleListener serverToggleButtonListener;

    private class ServerTextContentWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            Log.v(Constants.TAG, "Text changed in edit text: " + charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    private class ServerToggleListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(v.getId() != R.id.server_toggle_button)
                return;

            ToggleButton button = (ToggleButton)v;
            if(button.isChecked()) {
                serverThread = new ServerThread();
                serverThread.startServer();
            }
            else {
                if(serverThread != null) {
                    serverThread.stopServer();
                    try {
                        serverThread.join();
                    } catch (InterruptedException e) {

                    }
                    serverThread = null;
                }
            }

        }
    }

    private ServerThread serverThread;
    private class ServerThread extends Thread {

        private boolean isRunning;

        private ServerSocket serverSocket;

        public void startServer() {
            isRunning = true;
            start();
            Log.v(Constants.TAG, "startServer() method invoked " + serverSocket);
        }

        public void stopServer() {
            isRunning = false;
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            Log.v(Constants.TAG, "stopServer() method invoked ");
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(Constants.SERVER_PORT);
                while (isRunning) {
                    Log.v(Constants.TAG, "isRunning = " + isRunning);
                    Socket socket = serverSocket.accept();
                    Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
                    PrintWriter printWriter = Utilities.getWriter(socket);
                    printWriter.println(serverTextEditText.getText().toString());
                    socket.close();
                    Log.v(Constants.TAG, "Connection closed");
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_threaded_server);

        /* Search the views */
        serverTextEditText = (EditText)findViewById(R.id.server_text_edit_text);
        serverToggleButton = (ToggleButton)findViewById(R.id.server_toggle_button);
        serverThread = null;

        /* Instantiate the handlers */
        serverTextContentWatcher = new ServerTextContentWatcher();
        serverToggleButtonListener = new ServerToggleListener();

        serverTextEditText.addTextChangedListener(serverTextContentWatcher);
        serverToggleButton.setOnClickListener(serverToggleButtonListener);
    }

}

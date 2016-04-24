package ro.pub.cs.systems.eim.lab06.pheasantgame.views;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Random;

import ro.pub.cs.systems.eim.lab06.pheasantgame.R;
import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Constants;
import ro.pub.cs.systems.eim.lab06.pheasantgame.general.Utilities;

public class ServerFragment extends Fragment {

    private TextView serverHistoryTextView;

    private class CommunicationThread extends Thread {

        private Socket socket;
        private Random random = new Random();

        private String expectedWordPrefix = new String();

        public CommunicationThread(Socket socket) {
            if (socket != null) {
                this.socket = socket;
                Log.d(Constants.TAG, "[SERVER] Created communication thread with: " + socket.getInetAddress() + ":" + socket.getLocalPort());
            }
        }

        public void run() {
            Log.d(Constants.TAG, "[SERVER] 1");
            if (socket == null) {
                return;
            }
            boolean isRunning = true;

            InputStream requestStream = null;
            OutputStream responseStream = null;
            try {
                requestStream = socket.getInputStream();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            try {
                responseStream = socket.getOutputStream();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            BufferedReader requestReader = new BufferedReader(new InputStreamReader(requestStream));
            PrintStream responsePrintWriter = new PrintStream(responseStream);
            String line = null;

            while (isRunning) {

                Log.d(Constants.TAG, "[SERVER] 2");

                // TODO: exercise 7a
                try {
                    line = requestReader.readLine();
                    Log.d(Constants.TAG, "[SERVER] 3");
                } catch (IOException e) {
                    Log.e(Constants.TAG, "An exception has occurred: " + e.getMessage());
                }

                Log.d(Constants.TAG, "Line read:" + line);
                if(line.equals(Constants.END_GAME)){
                    Log.d(Constants.TAG, "Server read end of the game, closing");
                    isRunning = false;
                    try {
                        socket.close();
                    } catch (Exception e) {
                        Log.d(Constants.TAG, "Exceptie: " + e.getMessage());
                    }
                }

                final String receivedString = "From client: " + line;
                serverHistoryTextView.post(new Runnable() {

                    @Override
                    public void run() {
                        serverHistoryTextView.setText(serverHistoryTextView.getText() + "\n" + receivedString);
                    }
                });

                if(Utilities.wordValidation(line)) {
                    List potentialWords = Utilities.getWordListStartingWith(
                            line.substring(line.length()-2, line.length()));
                    if(potentialWords.size() == 0)
                        responsePrintWriter.println("WIN");
                    else
                        responsePrintWriter.println(potentialWords.get(new Random().nextInt(potentialWords.size())));
                }
                else {
                    responsePrintWriter.println("LOSS - Invalid Word");
                }
            }
            try {
                socket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    private ServerThread serverThread;
    private class ServerThread extends Thread {

        private ServerSocket serverSocket;

        private boolean isRunning;

        public ServerThread() {
            try {
                Log.d(Constants.TAG, "[SERVER] Created server thread, listening on port " + Constants.SERVER_PORT);
                serverSocket = new ServerSocket(Constants.SERVER_PORT);
                isRunning = true;
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred:" + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }

        @Override
        public void run() {

            while(isRunning) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    Log.d(Constants.TAG, "[SERVER] Incoming communication " + socket.getInetAddress() + ":" + socket.getLocalPort());
                } catch (SocketException socketException) {
                    Log.e(Constants.TAG, "An exception has occurred: "+ socketException.getMessage());
                    if (Constants.DEBUG) {
                        socketException.printStackTrace();
                    }
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "An exception has occurred:" + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }

                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket);
                    communicationThread.start();
                }
            }

        }

        public void stopServer() {
            try {
                isRunning = false;
                if (serverSocket != null) {
                    serverSocket.close();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_server, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        serverHistoryTextView = (TextView)getActivity().findViewById(R.id.server_history_text_view);

        serverThread = new ServerThread();
        serverThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serverThread != null) {
            serverThread.stopServer();
        }
    }

}


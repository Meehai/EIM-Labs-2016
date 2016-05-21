package colocviu2.eim.systems.cs.pub.ro.practicaltest02;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Utilities {
    public static BufferedReader getReader(Socket socket) {
        InputStream requestStream = null;
        try {
            requestStream = socket.getInputStream();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return new BufferedReader(new InputStreamReader(requestStream));
    }

    public static PrintWriter getWriter(Socket socket) {
        OutputStream responseStream = null;
        try {
            responseStream = socket.getOutputStream();
        } catch (IOException e){
            e.printStackTrace();
        }
        return new PrintWriter(responseStream);
    }
}
package com.a605.cse.audiosampler;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerThread extends Thread {
//    public static boolean SYNCHRONIZING = false;
    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "ServerThread";
    private final String LOG_TAG = NAME + CLAZZ;

    private final int SERVER_PORT = 5000;

    @Override
    public void run() {
//        SYNCHRONIZING = !SYNCHRONIZING;
        BufferedReader bufferedReader = null;
        PrintWriter printWriter = null;
        ServerSocket serverSocket = initServerSocket();
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String payload = bufferedReader.readLine();

                if(payload.equals("1")){
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    Log.d(LOG_TAG, " Sending " + timestamp + " to the Server.");

                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    printWriter.println(timestamp);
                }
            }catch (UnknownHostException e) {
                Log.e(LOG_TAG, "Server Task UnknownHostException.");
            } catch (IOException e) {
                Log.e(LOG_TAG, "Server Task IOException.");
            } finally {
                cleanUp(printWriter, bufferedReader, socket);
//                SYNCHRONIZING = !SYNCHRONIZING;
            }
        }
    }

    private ServerSocket initServerSocket(){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    private void cleanUp(PrintWriter writer, BufferedReader reader, Socket socket){
        // Close output stream
        if (writer != null) {
            writer.close();
        } // Close input stream.
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                Log.i(LOG_TAG, "CleanUp of ObjectInputStream");
            }
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.i(LOG_TAG, "CleanUp of Socket");
            }
        }
    }
}

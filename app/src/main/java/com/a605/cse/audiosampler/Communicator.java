package com.a605.cse.audiosampler;

import android.util.Log;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

class Communicator extends Thread {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "Communicator";
    private final String LOG_TAG = CLAZZ; // Don't want to clutter AudioSampler tag results.

    private final int PORT = 8080;
    private String SERVER_IP;
    private String data;

    Communicator(MainActivity mainActivity, String data) {
        SERVER_IP = mainActivity.ipAddress;
        this.data = data;
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        PrintWriter printWriter = null;
//        BufferedReader bufferedReader = null;

        try {
            // Create socket for the destination port.
            clientSocket = new Socket(SERVER_IP, PORT);

            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
            printWriter.println(data);

//            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            String portID = bufferedReader.readLine();

        } catch (UnknownHostException e) {
            Log.e(LOG_TAG, "ClientTask Send UnknownHostException.");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(LOG_TAG, "ClientTask Send IOException");
            e.printStackTrace();
        } finally {
            cleanUp(printWriter, clientSocket);
        }
    }

    public void cleanUp(PrintWriter writer, Socket socket){
        // Close output stream
        if (writer != null) {
            writer.close();
        } // Close socket
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                Log.i(LOG_TAG, "CleanUp of Socket");
            }
        }
    }
}

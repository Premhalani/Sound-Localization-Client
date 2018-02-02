package com.a605.cse.audiosampler;

import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerManager {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "ServerManager";
    private final String LOG_TAG = NAME + CLAZZ;

    private ServerThread serverThread;

    public void start_server(){
        serverThread = new ServerThread();
        serverThread.start();
        Log.d(LOG_TAG,"Server started");
    }

    public void stop_server(){
        serverThread = null;
        Log.d(LOG_TAG,"Server Stopped");
    }

}

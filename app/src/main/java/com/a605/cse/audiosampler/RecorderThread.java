package com.a605.cse.audiosampler;

import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

class RecorderThread {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "RecorderThread";
    private final String LOG_TAG = NAME + CLAZZ;

    private Thread thread;
    private CallbackInterface callback;
    private MainActivity parentActivity;
    private AudioConfiguration audioConfiguration;

    public RecorderThread(AudioConfiguration _audioConfiguration, CallbackInterface _callback) {
        audioConfiguration = _audioConfiguration;
        callback = _callback;
    }

    public void setCallback(CallbackInterface callback) {
        this.callback = callback;
    }

    public void start() {
        Log.d(LOG_TAG," Recording Started.");
        if (thread != null) return;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);

                int minBufferSize = AudioRecord.getMinBufferSize(audioConfiguration.getSampleRate(),
                        audioConfiguration.getChannelConfiguration(),
                        audioConfiguration.getAudioEncoding());

                AudioRecord recorder = new AudioRecord(audioConfiguration.getAudioSource(),
                        audioConfiguration.getSampleRate(),
                        audioConfiguration.getChannelConfiguration(),
                        audioConfiguration.getAudioEncoding(), minBufferSize);

                if (recorder.getState() == AudioRecord.STATE_UNINITIALIZED) {
                    Thread.currentThread().interrupt();
                    return;
                } else {
                    Log.i(RecorderThread.class.getSimpleName(), "Started.");
                    //callback.onStart();
                }
                byte[] buffer = new byte[minBufferSize];
                recorder.startRecording();

                while (thread != null && !thread.isInterrupted() && recorder.read(buffer, 0, minBufferSize) > 0) {
                    callback.onBufferAvailable(buffer);
                }
                recorder.stop();
                recorder.release();
            }
        }, RecorderThread.class.getName());
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.interrupt();
            thread = null;
            Log.d(LOG_TAG," Recording Stopped");
        }
    }
}
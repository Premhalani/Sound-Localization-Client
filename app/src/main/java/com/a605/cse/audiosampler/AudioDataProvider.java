package com.a605.cse.audiosampler;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.a605.cse.audiosampler.calculators.AudioCalculator;
import com.a605.cse.audiosampler.dataobjects.AudioDataObject;
import com.a605.cse.audiosampler.dataobjects.NetworkDataObject;
import com.a605.cse.audiosampler.dataobjects.SyncDataObject;
import com.google.gson.Gson;

public class AudioDataProvider implements CallbackInterface {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "AudioDataProvider";
    private final String LOG_TAG = CLAZZ; // Don't want to clutter AudioSampler tag results.

    private AudioCalculator audioCalculator;
    private Handler handler;
    private MainActivity mainActivity;

    public AudioDataProvider(MainActivity _mainActivity) {
        mainActivity = _mainActivity;
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    public void onBufferAvailable(byte[] buffer) {
        final String timestamp = String.valueOf(System.currentTimeMillis());

        audioCalculator.setBytes(buffer);
        // Calculate the calculation time.
        int amplitude = audioCalculator.getAmplitude();
        double decibel = audioCalculator.getDecibel();
        double frequency = audioCalculator.getFrequency();

        final String amp = String.valueOf(amplitude);
        final String db = String.valueOf(decibel);
        final String hz = String.valueOf(frequency);

        // Printing
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                mainActivity.textAmplitude.setText(amp);
//                mainActivity.textDecibel.setText(db);
//                mainActivity.textFrequency.setText(hz);
//            }
//        });
        int targetFrequency = Integer.parseInt(mainActivity.inputFrequency.getText().toString());
        int syncFrequency = 5000;

        if (frequency > syncFrequency) {
            String deviceId = Settings.Secure.getString(mainActivity.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            Gson gson = new Gson();
            NetworkDataObject networkDataObject;

            if (frequency > targetFrequency) {
                networkDataObject = new AudioDataObject(amp, hz, db, deviceId, timestamp);
                Log.d(LOG_TAG, "Sending audio data through communicator.");
            } else {
                networkDataObject = new SyncDataObject(deviceId, timestamp);
                Log.d(LOG_TAG, "Sending sync data through communicator.");
            }

            String jsonAudioDataObject = gson.toJson(networkDataObject);

            Communicator communicator = new Communicator(mainActivity, jsonAudioDataObject);
            communicator.start();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    mainActivity.textAmplitude.setText(amp + " Amp");
                    mainActivity.textDecibel.setText(db + " db");
                    mainActivity.textFrequency.setText(hz + " Hz");
                }
            });
        }
    }
}

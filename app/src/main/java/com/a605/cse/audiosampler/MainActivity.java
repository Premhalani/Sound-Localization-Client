package com.a605.cse.audiosampler;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.a605.cse.audiosampler.dataobjects.AudioDataObject;

public class MainActivity extends Activity implements OnClickListener {

    private String NAME = "AudioSampler:: ";
    private String CLAZZ = "MainActivity";
    private final String LOG_TAG = NAME + CLAZZ;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String[] permissions = {Manifest.permission.RECORD_AUDIO};
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private RecorderThread recorderThread;
    private ServerManager serverManager;

    // Need to handle these better.
    Button startRecordingButton, stopRecordingButton, resetButton;
    TextView textAmplitude, textDecibel, textFrequency, textDeviceId;
    EditText ipAddressEditText, inputFrequency;
    public String ipAddress;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted) finish();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        setContentView(R.layout.activity_main);

        serverManager = new ServerManager();
        serverManager.start_server();

        initializeViews();
    }

    public void initializeViews() {
        textDecibel = findViewById(R.id.textDecibel);
        resetButton = findViewById(R.id.ResetButton);
        textDeviceId = findViewById(R.id.deviceId);
        textAmplitude = findViewById(R.id.textAmplitude);
        textFrequency = findViewById(R.id.textFrequency);
        inputFrequency = findViewById(R.id.inputFrequency);
        ipAddressEditText = findViewById(R.id.ipAddress);
        stopRecordingButton = findViewById(R.id.StopRecordingButton);
        startRecordingButton = findViewById(R.id.StartRecordingButton);

        startRecordingButton.setOnClickListener(this);
        stopRecordingButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);

        stopRecordingButton.setEnabled(false);

        String deviceId = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        textDeviceId.append(deviceId);
    }

    public void onClick(View v) {
        if (v == startRecordingButton) {
            record();
        } else if (v == stopRecordingButton) {
            stopRecording();
        } else if (v == resetButton) {
            resetApp();
        }
    }

    public void record() {
        //serverManager.stop_server();
        startRecordingButton.setEnabled(false);
        stopRecordingButton.setEnabled(true);
        ipAddress = ipAddressEditText.getText().toString();

        AudioDataProvider audioDataProvider = new AudioDataProvider(this);
        AudioConfiguration audioConfiguration = new AudioConfiguration();

        recorderThread = new RecorderThread(audioConfiguration, audioDataProvider);
        recorderThread.start();
    }

    public void stopRecording() {
        recorderThread.stop();
        startRecordingButton.setEnabled(true);
        stopRecordingButton.setEnabled(false);
    }

    public void resetApp() {
        AudioDataObject.counter = 0;
    }
}
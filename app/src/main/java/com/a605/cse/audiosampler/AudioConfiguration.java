package com.a605.cse.audiosampler;

import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioConfiguration {
    
    private int audioSource;
    private int channelConfiguration;
    private int audioEncoding;
    private int sampleRate;

    public AudioConfiguration() {
        audioSource = MediaRecorder.AudioSource.DEFAULT;
        channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        sampleRate = 44100;
    }

    public int getAudioSource() {
        return audioSource;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannelConfiguration() {
        return channelConfiguration;
    }

    public int getAudioEncoding() {
        return audioEncoding;
    }
}

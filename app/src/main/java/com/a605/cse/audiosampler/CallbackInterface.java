package com.a605.cse.audiosampler;

/**
 * Created by might on 11/5/17.
 */

public interface CallbackInterface {
    void onBufferAvailable(byte[] buffer);
}

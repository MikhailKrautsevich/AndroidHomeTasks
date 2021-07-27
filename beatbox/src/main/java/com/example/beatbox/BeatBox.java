package com.example.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssetManager;
    private ArrayList mSounds = new ArrayList();
    private SoundPool mSoundPool;
    private float mSpeedPlayback = 1.0f ;

    public BeatBox(Context context) {
        mAssetManager = context.getAssets();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //noinspection deprecation
            mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        } else {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build() ;
            mSoundPool = new SoundPool.Builder()
                    .setAudioAttributes(attributes)
                    .setMaxStreams(MAX_SOUNDS)
                    .build();
        }
        loadSounds();
    }

    public ArrayList<Sound> getSounds() {
        return mSounds;
    }

    public void playSound(Sound sound) {
        Integer id = sound.getSoundID();
        if (id == null) {
            return;
        }
        mSoundPool.play(id, 1.0f, 1.0f, 1, 0, mSpeedPlayback);
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds.");
        } catch (IOException e) {
            Log.i(TAG, "Could not list assets " + e);
            return;
        }
        for (String filename : soundNames) {
            try {
                String path = SOUNDS_FOLDER + '/' + filename;
                Sound sound = new Sound(path);
                loadSound(sound);
                mSounds.add(sound);
            } catch (IOException exception) {
                Log.e(TAG, "Could not load sound " + filename, exception);
            }
        }
    }

    private void loadSound(Sound sound) throws IOException {
        AssetFileDescriptor afd = mAssetManager.openFd(sound.getAssetPath());
        int soundID = mSoundPool.load(afd, 1);
        sound.setSoundID(soundID);
    }

    public void setSpeedPlayback(float speed) {
        mSpeedPlayback = speed ;
    }

    public float getSpeedPlayback() {
        return mSpeedPlayback ;
    }

    public void release() {
        mSoundPool.release();
    }
}

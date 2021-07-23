package com.example.beatbox;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class BeatBox {
    private static final String TAG = "BeatBox" ;
    private static final  String SOUNDS_FOLDER = "sample_sounds" ;

    private AssetManager mAssetManager ;
    private ArrayList mSounds = new ArrayList() ;

    public BeatBox(Context context){
        mAssetManager = context.getAssets() ;
        loadSounds() ;
    }

    public ArrayList<Sound> getSounds(){return mSounds;}

    private void loadSounds() {
        String [] soundNames ;
        try {
            soundNames = mAssetManager.list(SOUNDS_FOLDER) ;
            Log.i(TAG, "Found " + soundNames.length + " sounds.") ;
        } catch (IOException e) {
            Log.i(TAG, "Could not list assets" + e) ;
            soundNames = new String[0] ;
        }
        if (soundNames != null) {
            for (String s : soundNames) {
                String path = SOUNDS_FOLDER + '/' + s ;
                Sound sound = new Sound(path) ;
                mSounds.add(sound) ;
            }
        }
    }
}

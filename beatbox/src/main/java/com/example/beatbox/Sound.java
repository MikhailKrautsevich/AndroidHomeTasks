package com.example.beatbox;

import android.util.Log;

public class Sound {

    private String mAssetPath ;
    private String mName ;

    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = mAssetPath.split("/") ;
        String filename  = components[components.length - 1] ;
        Log.i("123456", filename) ;
        mName = filename.replace(".wav", "") ;
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }
}

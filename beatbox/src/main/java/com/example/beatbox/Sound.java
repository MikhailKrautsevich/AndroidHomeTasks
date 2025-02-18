package com.example.beatbox;

public class Sound {

    private String mAssetPath ;
    private String mName ;
    private Integer mSoundID ;

    public Sound(String assetPath) {
        mAssetPath = assetPath;
        String[] components = mAssetPath.split("/") ;
        String filename  = components[components.length - 1] ;
        mName = filename.replace(".wav", "") ;
    }

    public String getAssetPath() {
        return mAssetPath;
    }

    public String getName() {
        return mName;
    }

    public Integer getSoundID() {
        return mSoundID;
    }

    public void setSoundID(Integer soundID) {
        mSoundID = soundID;
    }
}

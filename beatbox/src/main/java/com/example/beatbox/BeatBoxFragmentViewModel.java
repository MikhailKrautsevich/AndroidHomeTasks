package com.example.beatbox;

import android.util.Log;

public class BeatBoxFragmentViewModel {

    private BeatBox mBeatBox ;

    public BeatBoxFragmentViewModel(BeatBox box){
        mBeatBox = box ;
    }

    public void changeSpeedPlayback() {
        Log.d("123456", "was changed") ;
    }
}

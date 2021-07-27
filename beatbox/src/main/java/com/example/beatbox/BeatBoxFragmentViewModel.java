package com.example.beatbox;

import android.util.Log;
import android.widget.SeekBar;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableFloat;

public class BeatBoxFragmentViewModel extends BaseObservable {

    private static final String LOG_TAG = "FragmentViewModel";
    private BeatBox mBeatBox;
    public ObservableFloat mSpeed ;

    public BeatBoxFragmentViewModel(BeatBox box) {
        mBeatBox = box;
        mSpeed = new ObservableFloat() ;
        mSpeed.set(mBeatBox.getSpeedPlayback());
    }

    public void onValueChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
        int progress = seekBar.getProgress();
        Log.d(LOG_TAG, "I get progress = " + progress);
        mBeatBox.setSpeedPlayback(transformToFloat(progress));
    }

    public String getString() {
        return "Playback speed " + mBeatBox.getSpeedPlayback() * 100 + " %";
    }

    private float transformToFloat(int progress) {
        return 0.5f + progress * 0.1f;
    }
}

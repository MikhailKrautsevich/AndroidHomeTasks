package com.example.beatbox;

import android.widget.SeekBar;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class BeatBoxFragmentViewModel extends BaseObservable {

    private final BeatBox mBeatBox;

    public BeatBoxFragmentViewModel(BeatBox box) {
        mBeatBox = box;
    }

    public void onValueChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
        int progress = seekBar.getProgress();
        float newSpeed = transformToFloat(progress) ;
        mBeatBox.setSpeedPlayback(newSpeed);
        notifyPropertyChanged(BR.string);
    }

    @Bindable
    public String getString() {
        return "Playback speed " + ( (int) (mBeatBox.getSpeedPlayback() * 100)) + " %";
    }

    private float transformToFloat(int progress) {
        return progress * 0.1f;
    }
}

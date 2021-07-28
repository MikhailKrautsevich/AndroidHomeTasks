package com.example.beatbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.SeekBar;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class BeatBoxFragmentViewModel extends BaseObservable {

    private final BeatBox mBeatBox;
    private final Context mContext ;

    public BeatBoxFragmentViewModel(BeatBox box, Context context) {
        mBeatBox = box;
        mContext = context ;
    }

    public void onValueChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
        int progress = seekBar.getProgress();
        float newSpeed = transformToFloat(progress) ;
        mBeatBox.setSpeedPlayback(newSpeed);
        notifyPropertyChanged(BR.speedString);
    }

    @SuppressLint("StringFormatInvalid")
    @Bindable
    public String getSpeedString() {
                String toFormat = mContext.getString(R.string.playbackSpeedText) ;
        return String.format(toFormat, ( (int) (mBeatBox.getSpeedPlayback() * 100)));
    }

    private float transformToFloat(int progress) {
        return progress * 0.1f;
    }
}

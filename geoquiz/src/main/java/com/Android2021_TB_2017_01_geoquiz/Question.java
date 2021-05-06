package com.Android2021_TB_2017_01_geoquiz;

import java.io.Serializable;

class Question implements Serializable {

    private int mTextResId ;
    private boolean mAnswerTrue ;
    private boolean mIsAnswered ;

    Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId ;
        this.mAnswerTrue = mAnswerTrue ;
    }

    void itIsAnswered() {mIsAnswered = true; }

    int getTextResId() {
        return mTextResId;
    }

    boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    boolean isItAnswered() {return mIsAnswered; }
}

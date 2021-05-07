package com.Android2021_TB_2017_01_geoquiz;

import java.io.Serializable;

class Question implements Serializable {

    private int mTextResId ;
    private boolean mAnswerTrue ;
    private boolean mIsAnswered ;
    private boolean mIsAnsweredRight ;

    Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId ;
        this.mAnswerTrue = mAnswerTrue ;
    }

    void itWasAnswered() {mIsAnswered = true; }

    void itWasAnsweredRight() {mIsAnsweredRight = true ;}

    int getTextResId() {
        return mTextResId;
    }

    boolean isAnswerTrue() {
        return mAnswerTrue;
    }

    boolean wasItAnswered() {return mIsAnswered; }

    boolean wasAnsweredRight() {return mIsAnsweredRight; }
}

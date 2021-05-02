package com.Android2021_TB_2017_01_geoquiz;

public class Question {

    private int mTextResId ;
    private boolean mAnswerTrue ;

    Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId ;
        this.mAnswerTrue = mAnswerTrue ;
    }

    public void setTextResId(int mTextResId) {
        this.mTextResId = mTextResId;
    }

    public void setAnswerTrue(boolean mAnswerTrue) {
        this.mAnswerTrue = mAnswerTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerTrue() {
        return mAnswerTrue;
    }

}

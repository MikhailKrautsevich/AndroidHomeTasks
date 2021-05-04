package com.Android2021_TB_2017_01_geoquiz;

class Question {

    private int mTextResId ;
    private boolean mAnswerTrue ;

    Question(int mTextResId, boolean mAnswerTrue) {
        this.mTextResId = mTextResId ;
        this.mAnswerTrue = mAnswerTrue ;
    }

    int getTextResId() {
        return mTextResId;
    }

    boolean isAnswerTrue() {
        return mAnswerTrue;
    }

}

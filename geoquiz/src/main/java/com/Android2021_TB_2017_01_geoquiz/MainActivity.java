package com.Android2021_TB_2017_01_geoquiz;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "QuizLog" ;
    private static final String KEY_INDEX = "KEY_INDEX" ;

    private LinearLayout mainLayout;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private Button mPreviousButton;
    private ImageButton mNextImageButton;
    private ImageButton mPreviousImageButton;

    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[]{
            new Question(R.string.question_australia, true),
            new Question(R.string.question_oceans, true),
            new Question(R.string.question_mideast, false),
            new Question(R.string.question_africa, false),
            new Question(R.string.question_americas, true),
            new Question(R.string.question_asia, true),
    };

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG, "OnCreate() called") ;

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX) ;
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mainLayout = findViewById(R.id.main_layout);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);

        mQuestionTextView.setOnClickListener(new GeoQuizListener());
        mTrueButton.setOnClickListener(new GeoQuizListener());
        mFalseButton.setOnClickListener(new GeoQuizListener());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mNextImageButton = findViewById(R.id.next_im_button);
            mPreviousImageButton = findViewById(R.id.prev_im_button);

            mNextButton = null ;
            mPreviousButton = null ;

            mNextImageButton.setOnClickListener(new GeoQuizListener());
            mPreviousImageButton.setOnClickListener(new GeoQuizListener());
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mNextButton = findViewById(R.id.next_button);
            mPreviousButton = findViewById(R.id.prev_button);

            mNextImageButton = null ;
            mPreviousImageButton = null ;

            mNextButton.setOnClickListener(new GeoQuizListener());
            mPreviousButton.setOnClickListener(new GeoQuizListener());
        }

        updateQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG, "OnStart() called") ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG, "OnResume() called") ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG, "OnPause() called") ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG, "OnStop() called") ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG, "OnDestroy() called") ;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG, "onSaveInstanceState() called") ;
        outState.putInt(KEY_INDEX, mCurrentIndex);
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean answer) {
        boolean isAnswerTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        int message;
        if (answer == isAnswerTrue) {
            message = R.string.correct_toast;
        } else {
            message = R.string.incorrect_toast;
        }
        Toast.makeText(MainActivity.this,
                message,
                Toast.LENGTH_SHORT)
                .show();
    }

    private void changeIndexOfQuestion(boolean up) {
        if (up) {
            if (mCurrentIndex < mQuestionBank.length - 1) {
                mCurrentIndex++;
            } else {
                Snackbar.make(mainLayout,
                        R.string.no_more_q_snackbar,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
            if (mCurrentIndex == 0) {
                Snackbar.make(mainLayout,
                        R.string.its_first_q_snackbar,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                mCurrentIndex--;
            }
        }
    }

    class GeoQuizListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.true_button: {
                    checkAnswer(true);
                    break;
                }
                case R.id.false_button : {
                    checkAnswer(false);
                    break;
                }
                case R.id.next_button :
                case R.id.next_im_button :
                case R.id.question_text_view: {
                    changeIndexOfQuestion(true);
                    updateQuestion();
                    break;
                }
                case R.id.prev_im_button :
                case R.id.prev_button : {
                    changeIndexOfQuestion(false);
                    updateQuestion();
                    break;
                }
                default: break;
            }
        }
    }
}


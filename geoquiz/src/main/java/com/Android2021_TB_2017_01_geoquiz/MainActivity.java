package com.Android2021_TB_2017_01_geoquiz;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String LOG = "QuizLog" ;
    private static final String KEY_INDEX = "KEY_INDEX" ;
    private static final String KEY_QUANTITY_OF_CORRECT_ANSWERS = "CORRECT" ;
    private static final String KEY_QUANTITY_OF_INCORRECT_ANSWERS = "INCORRECT" ;
    private static final String KEY_QUESTIONS = "KEY_QUESTIONS" ;
    private static final String KEY_CHEATER = "KEY_CHEATER" ;
    private static final int REQUEST_CODE_FOR_CHEAT = 0 ;

    private ViewGroup mainLayout;
    private Button mTrueButton;
    private Button mFalseButton;
    private View mNextButton;
    private View mPreviousButton;
    private Button mCheatButton ;
    private TextView mQuestionTextView;

    private GeoQuizListener mGeoQuizListener;

    private Question[] mQuestionBank ;

    private int mCurrentIndex = 0;
    private int mQCorrectAnswers = 0;
    private int mQInCorrectAnswers = 0;
    private int mQuestionsQuantity = 0;
    private boolean mIsCheater ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(LOG, "OnCreate() called") ;

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX) ;
            mQCorrectAnswers = savedInstanceState.getInt(KEY_QUANTITY_OF_CORRECT_ANSWERS) ;
            mQInCorrectAnswers = savedInstanceState.getInt(KEY_QUANTITY_OF_INCORRECT_ANSWERS) ;
            mIsCheater = savedInstanceState.getBoolean(KEY_CHEATER) ;
            mQuestionBank = (Question[]) savedInstanceState.getSerializable(KEY_QUESTIONS) ;
        } else initDefaultQuestionBank();

        if (mQuestionBank != null) {
        mQuestionsQuantity = mQuestionBank.length;
        }

        mQuestionTextView = findViewById(R.id.question_text_view);
        mainLayout = findViewById(R.id.main_layout);
        mTrueButton = findViewById(R.id.true_button);
        mFalseButton = findViewById(R.id.false_button);
        mNextButton = findViewById(R.id.next_button) ;
        mPreviousButton = findViewById(R.id.prev_button) ;
        mCheatButton = findViewById(R.id.cheat_button) ;

        mGeoQuizListener = new GeoQuizListener() ;

        mQuestionTextView.setOnClickListener(mGeoQuizListener);
        mTrueButton.setOnClickListener(mGeoQuizListener);
        mFalseButton.setOnClickListener(mGeoQuizListener);
        mNextButton.setOnClickListener(mGeoQuizListener);
        mPreviousButton.setOnClickListener(mGeoQuizListener);
        mCheatButton.setOnClickListener(mGeoQuizListener);

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
        if (mIsCheater) {
            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show();
        }
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
        outState.putInt(KEY_QUANTITY_OF_CORRECT_ANSWERS, mQCorrectAnswers);
        outState.putInt(KEY_QUANTITY_OF_INCORRECT_ANSWERS, mQInCorrectAnswers);
        outState.putBoolean(KEY_CHEATER, mIsCheater);
        outState.putSerializable(KEY_QUESTIONS, mQuestionBank);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_FOR_CHEAT) {
                if (data != null) {
                    mIsCheater = CheatActivity.wasAnswerShown(data) ;
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateQuestion() {
        Question currentQuestion = mQuestionBank[mCurrentIndex] ;
        mQuestionTextView.setText(currentQuestion.getTextResId());
        if (currentQuestion.wasItAnswered()) {
            if (currentQuestion.wasAnsweredRight()) {
                mQuestionTextView.setTextColor(getColor(R.color.green));
            } else {
                mQuestionTextView.setTextColor(getColor(R.color.red));
            }
        } else {
            mQuestionTextView.setTextColor(getColor(R.color.black));
        }
    }

    private void initDefaultQuestionBank() {
        mQuestionBank = new Question[]{
                new Question(R.string.question_australia, true),
                new Question(R.string.question_oceans, true),
                new Question(R.string.question_mideast, false),
                new Question(R.string.question_africa, false),
                new Question(R.string.question_americas, true),
                new Question(R.string.question_asia, true),
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkAnswer(boolean answer) {
        Question currentQuestion = mQuestionBank[mCurrentIndex] ;
        if (!currentQuestion.wasItAnswered()) {
            boolean isAnswerTrue = currentQuestion.isAnswerTrue();
            currentQuestion.itWasAnswered();
            int message;
            int colour ;
            if (answer == isAnswerTrue) {
                message = R.string.correct_toast;
                colour = R.color.green ;
                mQuestionBank[mCurrentIndex].itWasAnsweredRight();
                mQCorrectAnswers++ ;
            } else {
                message = R.string.incorrect_toast;
                colour = R.color.red ;
                mQInCorrectAnswers++ ;
            }
            Snackbar.make(mainLayout,
                    message,
                    Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(getColor(colour))
                    .show();
            if (mIsCheater) {
                Toast.makeText(MainActivity.this,
                        R.string.judgment_toast,
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
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

    private void initDefaultValues() {
        mCurrentIndex = 0;
        mQCorrectAnswers = 0;
        mQInCorrectAnswers = 0;
    }

    private void showResults(){
        double percentsForAnAnswer = 100.0/mQuestionBank.length ;
        double correctPercents = percentsForAnAnswer*mQCorrectAnswers ;
        double inCorrectPercents = percentsForAnAnswer*mQInCorrectAnswers ;

        String stringForFormat = getString(R.string.results_toast) ;

        String congrats = String
                .format(Locale.getDefault(),
                        stringForFormat,
                        correctPercents,
                        inCorrectPercents) ;
        if (correctPercents >= 50.0) {
            congrats = getResources().getString(R.string.congratulations) + congrats ;
        }
        Log.d(LOG, "showResults() called "+ congrats) ;
        Snackbar congratsSnackBar = Snackbar.make(MainActivity.this,
                mainLayout,
                congrats,
                Snackbar.LENGTH_LONG);
        View view = congratsSnackBar.getView() ;
        TextView textView = view.findViewById(com.google.android.material.R.id.snackbar_text) ;
        textView.setMaxLines(5);
        congratsSnackBar.setAction(getText(R.string.again), new TryAgainListener()) ;
        congratsSnackBar.show();
    }

    class GeoQuizListener implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
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
                case R.id.question_text_view: {
                    changeIndexOfQuestion(true);
                    break;
                }
                case R.id.prev_button : {
                    changeIndexOfQuestion(false);
                    break;
                }
                case R.id.cheat_button : {
                    Log.d(LOG, "CheatActivity started") ;
                    boolean cheat_answer = mQuestionBank[mCurrentIndex].isAnswerTrue() ;
                    Intent intent = CheatActivity.newIntent(MainActivity.this, cheat_answer) ;
                    startActivityForResult(intent, REQUEST_CODE_FOR_CHEAT);
                    break;
                }
                default: break;
            }
            updateQuestion();
            mIsCheater = false ;
            if (mQCorrectAnswers + mQInCorrectAnswers == mQuestionsQuantity) {
                showResults();
            }
        }
    }

    class TryAgainListener implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
            initDefaultValues();
            initDefaultQuestionBank();
            updateQuestion();
        }
    }
}


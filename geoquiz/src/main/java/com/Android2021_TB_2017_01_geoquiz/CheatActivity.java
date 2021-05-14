package com.Android2021_TB_2017_01_geoquiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class CheatActivity extends AppCompatActivity {

    private static  final String LOG = "QuizLog" ;
    private static final String EXTRA_CHEAT = "com.Android2021_TB_2017_01_geoquiz.KEY_CHEAT" ;
    private static final String EXTRA_ANSWER_WAS_SHOWN = "com.Android2021_TB_2017_01_geoquiz.ANSWER_WAS_SHOWN" ;
    private static final String EXTRA_CHEATS_LEFT = "com.Android2021_TB_2017_01_geoquiz.EXTRA_CHEATS_LEFT" ;

    private static final String KEY_IS_CHEATER = "KEY_IS_CHEATER" ;

    private boolean mIsAnswerTrue ;
    private boolean mIsCheater ;
    private TextView mAnswerTextView ;
    private TextView mAPITextView ;
    private TextView mHintsLeftTextView ;
    private Button mShowAnswerButton ;

    public static Intent newIntent(Context context, boolean answer_is_true) {
        Intent intent = new Intent(context, CheatActivity.class) ;
        intent.putExtra(EXTRA_CHEAT, answer_is_true) ;
        return intent ;
    }

    public static void putCheatsQuantity(Intent intent, int cheatsQuantity) {
        intent.putExtra(EXTRA_CHEATS_LEFT, cheatsQuantity) ;
    }

    public static boolean wasAnswerShown(Intent data) {
        return data.getBooleanExtra(EXTRA_ANSWER_WAS_SHOWN, false) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        Log.d(LOG, "CheatActivity - onCreate()") ;

        mAnswerTextView = findViewById(R.id.answer_text_view) ;
        mShowAnswerButton = findViewById(R.id.show_answer_button) ;
        mHintsLeftTextView= findViewById(R.id.hints_left) ;
        mAPITextView = findViewById(R.id.api_lvl_text_view) ;
        mAPITextView.setText(getAPIVersion());

        mIsAnswerTrue = getIntent().getBooleanExtra(EXTRA_CHEAT, false);

        checkIfPlayerHasHints(getIntent());

        if (savedInstanceState != null) {
            mIsCheater = savedInstanceState.getBoolean(KEY_IS_CHEATER) ;
            Log.d(LOG, "CheatActivity - get data from savedInstanceState : mIsCheater = " + mIsCheater) ;
        }

        if (mIsCheater) {
            setAnswerShownResult();
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAnswerTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                mIsCheater = true ;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mShowAnswerButton.getWidth()/2;
                    int cy = mShowAnswerButton.getHeight()/2;
                    float radius = mShowAnswerButton.getWidth() ;
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mShowAnswerButton, cx, cy, radius, 0) ;
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mShowAnswerButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                } else {
                    mShowAnswerButton.setVisibility(View.INVISIBLE);
                }
                setAnswerShownResult();
            }
        } ;

        mShowAnswerButton.setOnClickListener(listener);
    }

    private void checkIfPlayerHasHints(Intent intent) {
        int cheatsQuantity = intent.getIntExtra(EXTRA_CHEATS_LEFT, 0) ;
        String stringFormHintsLeftTextView = getString(R.string.you_have_no_hints) ;
        if (cheatsQuantity > 0) {
            stringFormHintsLeftTextView =
                    String.format(Locale.getDefault(),
                            getString(R.string.you_have_hints),
                            cheatsQuantity) ;
        } else {
            mShowAnswerButton.setVisibility(View.INVISIBLE);
        }
        mHintsLeftTextView.setText(stringFormHintsLeftTextView);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d(LOG, "CheatActivity - onPause()") ;
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(LOG, "CheatActivity - onStop()") ;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.d(LOG, "CheatActivity - onDestroy()") ;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_CHEATER, mIsCheater);
        Log.d(LOG, "CheatActivity - onSaveInstanceState() : mIsCheater = " + mIsCheater) ;
    }

    private void setAnswerShownResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_WAS_SHOWN, true) ;
        setResult(RESULT_OK, data);
    }

    private String getAPIVersion() {
        return getString(R.string.your_api_message) + Build.VERSION.SDK_INT;
    }

}

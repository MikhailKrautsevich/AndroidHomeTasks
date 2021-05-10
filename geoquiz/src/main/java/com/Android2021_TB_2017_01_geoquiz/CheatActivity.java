package com.Android2021_TB_2017_01_geoquiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_CHEAT = "com.Android2021_TB_2017_01_geoquiz.KEY_CHEAT" ;
    private static final String EXTRA_ANSWER_WAS_SHOWN = "com.Android2021_TB_2017_01_geoquiz.ANSWER_WAS_SHOWN" ;

    private boolean mIsAnswerTrue ;
    private TextView mAnswerTextView ;
    private Button mShowAnswerButton ;

    public static Intent newIntent(Context context, boolean answer_is_true) {
        Intent intent = new Intent(context, CheatActivity.class) ;
        intent.putExtra(EXTRA_CHEAT, answer_is_true) ;
        return intent ;
    }

    public static boolean wasAnswerShown(Intent data) {
        return data.getBooleanExtra(EXTRA_ANSWER_WAS_SHOWN, false) ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        mIsAnswerTrue = getIntent().getBooleanExtra(EXTRA_CHEAT, false) ;
        mAnswerTextView = findViewById(R.id.answer_text_view) ;
        mShowAnswerButton = findViewById(R.id.show_answer_button) ;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAnswerTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                } else {
                    mAnswerTextView.setText(R.string.false_button);
                }
                setAnswerShownResult(true) ;
            }
        } ;

        mAnswerTextView.setOnClickListener(listener);
        mShowAnswerButton.setOnClickListener(listener);
    }

    private void setAnswerShownResult(boolean b) {
        Intent data = new Intent() ;
        data.putExtra(EXTRA_ANSWER_WAS_SHOWN, true) ;
        setResult(RESULT_OK, data);
    }
}

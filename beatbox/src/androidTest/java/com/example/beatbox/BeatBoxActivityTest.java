package com.example.beatbox;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class BeatBoxActivityTest {
    @Rule
    public ActivityScenarioRule<BeatBoxActivity> mActivityRule = new ActivityScenarioRule<>(BeatBoxActivity.class) ;

    @Test
    public void showFirstFileName() {
        onView(withText("65_cjipie"))
                .check(matches(anything())) ;
    }
}

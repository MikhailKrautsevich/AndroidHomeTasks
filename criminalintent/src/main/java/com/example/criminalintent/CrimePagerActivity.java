package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {

    private static final String EXTRA_CRIME_ID = "com.example.criminalIntent.crime_id" ;
    private static final String EXTRA_WAS_EVENT_TURNED = "EXTRA_WAS_EVENT_TURNED" ;
    private static final String EXTRA_LEFT_EVENT = "EXTRA_LEFT_EVENT" ;
    private static final String EXTRA_RIGHT_EVENT = "EXTRA_RIGHT_EVENT" ;
    private static final String LOG = "CrimePagerActivity" ;

    private static boolean sWasEventTurned;
    private static int sLeftEvent ;
    private static int sRightEvent ;

    private ViewPager mViewPager ;
    private List<Crime> mCrimes ;

    public static Intent newIntent(Context packageContext, UUID crimeID) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class) ;
        intent.putExtra(EXTRA_CRIME_ID, crimeID) ;
        return intent ;
    }

    static int getLeftEvent(Intent data) {
        return data.getIntExtra(EXTRA_LEFT_EVENT, 0) ;
    }

    static int getRightEvent(Intent data) {
        return data.getIntExtra(EXTRA_RIGHT_EVENT, 0) ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        sWasEventTurned = false ;
        mViewPager = findViewById(R.id.crime_view_pager) ;

        mCrimes = CrimeLab.get(this).getCrimes() ;
        UUID crimeID = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID) ;
        FragmentManager fragmentManager = getSupportFragmentManager() ;
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position) ;
                return CrimeFragment.newInstance(crime.getID());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0 ; i < mCrimes.size(); i++ ) {
            if (mCrimes.get(i).getID().equals(crimeID)) {
                if (!sWasEventTurned) {
                    sLeftEvent = i ;
                    sRightEvent = i ;
                    sWasEventTurned = true ;
                    mViewPager.setCurrentItem(i);
                    Log.d(LOG, "sWasEventTurned = true by sLeftEvent=sRightEvent=i= " + i) ;
                } else {
                    if (i < sLeftEvent) {
                        sLeftEvent = i ;
                    } else if (i > sRightEvent) {
                        sRightEvent = i ;
                    }
                }
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float opacity = Math.abs(Math.abs(position) - 1) ;
                page.setAlpha(opacity);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (sWasEventTurned) {
            Intent intent = new Intent() ;
            intent.putExtra(EXTRA_LEFT_EVENT, sLeftEvent) ;
            intent.putExtra(EXTRA_RIGHT_EVENT, sRightEvent) ;
            setResult(RESULT_OK);
        }
    }

    void goToTheFirstItem() {
        sLeftEvent = 0 ;
        mViewPager.setCurrentItem(0);
    }

    void  goToTheLastItem() {
        int lastIndex = mCrimes.size() - 1 ;
        sRightEvent = lastIndex ;
        mViewPager.setCurrentItem(lastIndex);
    }

    int getAdapterPos() {
        return mViewPager.getCurrentItem() ;
    }

    boolean isItTheFirstItem() {
        return (getAdapterPos() == 0) ;
    }

    boolean isItTheLastItem() {
        return (getAdapterPos() == (mCrimes.size() - 1) );
    }
}

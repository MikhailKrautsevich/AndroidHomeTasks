package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private static final String LOG = "CrimePagerActivity" ;

    private ViewPager mViewPager ;
    private List<Crime> mCrimes ;

    public static Intent newIntent(Context packageContext, UUID crimeID) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class) ;
        intent.putExtra(EXTRA_CRIME_ID, crimeID) ;
        return intent ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

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

    void goToTheFirstItem() {
        mViewPager.setCurrentItem(0);
    }

    void  goToTheLastItem() {
        mViewPager.setCurrentItem(mCrimes.size() - 1);
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

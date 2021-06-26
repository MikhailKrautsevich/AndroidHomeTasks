package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CrimeListFragment extends Fragment {

    private static final String LOG = "CrimeListFragment_log" ;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle" ;
    private static final int REQUEST_CODE_FOR_UPDATE = 1114 ;

    private TextView mListIsEmpty ;
    private ImageButton mCrimeAdd ;
    private RecyclerView mcCrimeRecyclerView ;
    private CrimeAdapter mAdapter ;
    private int mCrimeChanged ;
    private boolean mSubtitleVisible ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false) ;
        mcCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view) ;
        mcCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mcCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

        mListIsEmpty = view.findViewById(R.id.crime_list_is_empty) ;
        mCrimeAdd = view.findViewById(R.id.crime_add_fab) ;
        mCrimeAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityToAddNewCrime();
            }
        });

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE) ;
        }

        updateUI() ;
        return view ;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG, "CrimeListFragment: onResume()") ;
        updateUI();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_UPDATE && data != null) {
            if (resultCode == RESULT_OK) {
                boolean wasDeleted = CrimePagerActivity.getDeleted(data);
                if (wasDeleted) {
                    mCrimeChanged = -1 ;
                    updateUI();
                } else {
                    int left = CrimePagerActivity.getLeftEvent(data) ;
                    int right = CrimePagerActivity.getRightEvent(data) ;
                    Log.d(LOG, "I get Right = " + right + " and Left = " + left);
                    updateSomeCrimes(left, right) ;
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle) ;
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle) ;
        } else subtitleItem.setTitle(R.string.show_subtitle);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime :
                startActivityToAddNewCrime();
                return true ;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible ;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true ;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSubtitle(){
        final CrimeLab crimeLab = CrimeLab.get(getActivity()) ;
        final LiveData<List<Crime>> liveData = crimeLab.getCrimes() ;
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Crime>>() {
            @Override
            public void onChanged(List<Crime> crimes) {
                int crimeCount  = 0 ;
                if (liveData.getValue() != null) {
                    crimeCount = liveData.getValue().size() ;
                }

                String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount) ;
                if (!mSubtitleVisible) {
                    subtitle = null ;
                }
                AppCompatActivity activity = (AppCompatActivity) getActivity() ;
                activity.getSupportActionBar().setSubtitle(subtitle);
            }
        });
    }

    private void updateUI() {
        final CrimeLab mCrimeLab = CrimeLab.get(getActivity()) ;
        LiveData<List<Crime>> liveData = mCrimeLab.getCrimes();
        liveData.observe(getViewLifecycleOwner(), new Observer<List<Crime>>() {
            @Override
            public void onChanged(List<Crime> crimes) {
                if (crimes != null) {
                    if (mAdapter == null) {
                        mAdapter = new CrimeAdapter(crimes);
                        mcCrimeRecyclerView.setAdapter(mAdapter);
                    }
                    if (mCrimeChanged >= 0 && crimes.size() >= mCrimeChanged+1) {
                        Crime crimeToUpdate = crimes.get(mCrimeChanged);
                        mAdapter.mCrimes.set(mCrimeChanged, crimeToUpdate) ;
                        mAdapter.notifyItemChanged(mCrimeChanged);
                    }
                    if (mCrimeChanged == -1) {
                        mAdapter.mCrimes = mCrimeLab.getCrimes().getValue() ;
                        mAdapter.notifyDataSetChanged();
                    }
                    updateSubtitle();

                    if (crimes.size() > 0) {
                        mListIsEmpty.setVisibility(View.INVISIBLE);
                        mcCrimeRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mListIsEmpty.setVisibility(View.VISIBLE);
                        mcCrimeRecyclerView.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    private void updateSomeCrimes(final int left, final int right) {
        if (mAdapter != null) {
            GetAllCrimesAsync task = new GetAllCrimesAsync(getContext(), left, right);
            task.execute();
        }
    }

    private void startActivityToAddNewCrime(){
        Crime crime = new Crime();
        Log.d(LOG, "startActivityToAddNewCrime() : crime.getDate = " + crime.getDate().toString()) ;
        CrimeLab.get(getActivity())
                .addCrime(crime);
        Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID());
        mCrimeChanged = -1 ;
        startActivity(intent);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView ;
        private TextView mDateTextView ;
        private ImageView mCrimeSolved ;
        private Crime mCrime ;

        CrimeHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.list_item_crime, viewGroup, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_list_title) ;
            mDateTextView = itemView.findViewById(R.id.crime_list_date) ;
            mCrimeSolved = itemView.findViewById(R.id.crime_solved_image_view) ;
        }

        void bind(Crime crime) {
            mCrime = crime ;
//            Log.d(LOG, "CrimeHolder bind() : crime.getTitle() = " + crime.getTitle()) ;
//            Log.d(LOG, "mTitleTextView == null:" + (mTitleTextView == null) ) ;
            mTitleTextView.setText(crime.getTitle());
            String dateString = DateFormat
                    .format( "EEEE, dd MMM, yyyy", mCrime.getDate())
                    .toString() ;
            mDateTextView.setText(dateString);
            mCrimeSolved.setVisibility(mCrime.getSolved()? View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID()) ;
            int absAdapterPosition = CrimeHolder.this.getAbsoluteAdapterPosition() ;
            Log.d(LOG, "getAbsoluteAdapterPosition() = " + absAdapterPosition) ;
            int bindAdapterPosition = CrimeHolder.this.getBindingAdapterPosition() ;
            Log.d(LOG, "getBindingAdapterPosition() = " + bindAdapterPosition) ;
            mCrimeChanged = bindAdapterPosition ;
            startActivityForResult(intent, REQUEST_CODE_FOR_UPDATE);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private List<Crime> mCrimes ;

        CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes ;
            Log.d(LOG, "CrimeAdapter constructor: mCrimes.length = " + mCrimes.size()) ;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity()) ;
            return new CrimeHolder(inflater, parent) ;
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position) ;
            holder.bind(crime);
        }

        @Override
        public int getItemCount() {
            if (mCrimes == null) {
                return 0 ;
            } else return mCrimes.size();
        }

        List<Crime> getAdaptersList() {
            return mCrimes ;
        }
    }

    class GetAllCrimesAsync extends AsyncTask<Void, Void, List<Crime>> {

        Context mContext ;
        int left ;
        int right ;

        private GetAllCrimesAsync(Context context, int left, int right) {
            mContext = context ;
            this.left = left ;
            this.right = right ;
        }

        @Override
        protected List<Crime> doInBackground(Void ... args) {
            final CrimeLab lab = CrimeLab.get(mContext) ;
            return lab.getListCrimes();
        }

        @Override
        protected void onPostExecute(List<Crime> crimes) {
            super.onPostExecute(crimes);
            List<Crime> list = mcCrimeRecyclerView.getAdapter().getAdaptersList();
        }
    }
}

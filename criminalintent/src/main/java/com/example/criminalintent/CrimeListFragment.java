package com.example.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CrimeListFragment extends Fragment {

    private static final String LOG = "CrimeListFragment_log" ;
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle" ;
    private static final int REQUEST_CODE_FOR_UPDATE = 1114 ;

    private TextView mListIsEmpty ;
    private ImageButton mCrimeAdd ;
    private RecyclerView mCrimeRecyclerView ;
    private CrimeAdapter mAdapter ;
    private int mCrimeChanged ;
    private boolean mSubtitleVisible ;
    private Callbacks mCallbacks ;

    private ItemTouchHelper.Callback mItemTouchHelperCallback;
    private ItemTouchHelper mItemTouchHelper ;

    public interface Callbacks {
        void onCrimeSelected(Crime crime) ;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context ;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false) ;
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view) ;
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

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
    public void onDetach() {
        super.onDetach();
        mCallbacks = null ;
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
        } else {
            subtitleItem.setTitle(R.string.show_subtitle) ;
        }
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
        CrimeLab crimeLab = CrimeLab.get(getActivity()) ;
        int crimeCount = crimeLab.getCrimes().size() ;

        String subtitle = getResources().getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount) ;
        if (!mSubtitleVisible) {
            subtitle = null ;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity() ;
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    void updateUI() {
        CrimeLab mCrimeLab = CrimeLab.get(getActivity()) ;
        List<Crime> crimes = mCrimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
            mCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));

            mItemTouchHelperCallback = new SimpleItemTouchHelperCallback(mAdapter) ;
            mItemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback) ;
            mItemTouchHelper.attachToRecyclerView(mCrimeRecyclerView);
        }
        if (mCrimeChanged >= 0 && crimes.size() >= mCrimeChanged+1) {
//            UUID uuid = crimes.get(mCrimeChanged).getID() ;
//            Crime crimeToUpdate = mCrimeLab.getCrime(uuid) ;
//            mAdapter.mCrimes.set(mCrimeChanged, crimeToUpdate) ;
//            mAdapter.notifyItemChanged(mCrimeChanged);
            mCrimeChanged = -1 ;
        }
        if (mAdapter != null) {                                                                     // чтобы работало без onActivityResult(),
            if (mAdapter.mCrimes.size() != crimes.size()) {                                         //но он не будет работать
                mCrimeChanged = -1 ;
            }
        }
        if (mCrimeChanged == -1) {
            mAdapter.mCrimes = crimes ;
            mAdapter.notifyDataSetChanged();
        }
        updateSubtitle();

        if (crimes.size() > 0) {
            mListIsEmpty.setVisibility(View.INVISIBLE);
            mCrimeRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mListIsEmpty.setVisibility(View.VISIBLE);
            mCrimeRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void updateSomeCrimes(int left, int right) {
        if (mAdapter != null) {
            List<Crime> adaptersList = mAdapter.getList() ;
            if (right < adaptersList.size()) {
                CrimeLab lab = CrimeLab.get(getContext()) ;
                if (right-left < adaptersList.size()/2) {
                    for (int i = left; i <= right; i++) {
                        UUID id = adaptersList.get(i).getID() ;
                        Crime crimeToUpdate = lab.getCrime(id) ;
                        adaptersList.set(i, crimeToUpdate) ;
                        mAdapter.notifyItemChanged(i);
                        Log.d(LOG, "updateSomeCrimes : mAdapter.notifyItemChanged(i) , i = " + i) ;
                    }
                } else {
                    mAdapter.mCrimes = lab.getCrimes() ;
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void startActivityToAddNewCrime(){
        Crime crime = new Crime();
        Log.d(LOG, "startActivityToAddNewCrime() : crime.getDate = " + crime.getDate().toString()) ;
        CrimeLab.get(getActivity())
                .addCrime(crime);
        mCrimeChanged = -1 ;
        updateUI();
        mCallbacks.onCrimeSelected(crime);
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
            mDateTextView.setText(DateFormatter.getFormattedDate(mCrime.getDate(), getContext()));
            mCrimeSolved.setVisibility(mCrime.getSolved()? View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View v) {
//            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getID()) ;         // чтобы работал onCrimeSelected()
//            startActivityForResult(intent, REQUEST_CODE_FOR_UPDATE);
            mCallbacks.onCrimeSelected(mCrime);

            int absAdapterPosition = CrimeHolder.this.getAbsoluteAdapterPosition() ;
            Log.d(LOG, "getAbsoluteAdapterPosition() = " + absAdapterPosition) ;
            int bindAdapterPosition = CrimeHolder.this.getBindingAdapterPosition() ;
            Log.d(LOG, "getBindingAdapterPosition() = " + bindAdapterPosition) ;
            mCrimeChanged = bindAdapterPosition ;

            updateUI();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>
    implements ItemTouchHelperAdapter{

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
            return mCrimes.size();
        }

        List<Crime> getList(){
            return mCrimes ;
        }

        @Override
        public void onItemDismiss(int position) {
            Crime crime = mCrimes.get(position) ;
            CrimeLab.get(getContext()).deleteCrime(crime.getID());
            updateUI();
        }
    }
}

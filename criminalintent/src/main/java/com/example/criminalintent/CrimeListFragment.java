package com.example.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class CrimeListFragment extends Fragment {

    private static final String LOG = "CrimeListFragment_log" ;
    private static final int REQUEST_CODE_FOR_UPDATE = 1114 ;

    private RecyclerView mcCrimeRecyclerView ;
    private CrimeAdapter mAdapter ;
    private int mCrimeChanged;

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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_FOR_UPDATE && data != null) {
            if (resultCode == RESULT_OK) {
                int left = CrimePagerActivity.getLeftEvent(data) ;
                int right = CrimePagerActivity.getRightEvent(data) ;
                Log.d(LOG, "I get Right = " + right + " and Left = " + left);
                updateSomeCrimes(left, right) ;
            } else {
                updateUI();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_crime :
                Crime crime = new Crime();
                CrimeLab.get(getActivity())
                        .addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getID());
                mCrimeChanged = -1 ;
                startActivity(intent);
                return true ;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateUI() {
        CrimeLab mCrimeLab = CrimeLab.get(getActivity()) ;
        List<Crime> crimes = mCrimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mcCrimeRecyclerView.setAdapter(mAdapter);
            mcCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        }
        if (mCrimeChanged >= 0) {
            mAdapter.notifyItemChanged(mCrimeChanged);
        }
        if (mCrimeChanged == -1) {
            mAdapter.mCrimes = CrimeLab.get(getActivity()).getCrimes() ;
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateSomeCrimes(int left, int right) {
        if (mAdapter != null && right < mAdapter.getList().size()) {
            for (int i = left; i <= right; i++) {
                mAdapter.notifyItemChanged(i);
                Log.d(LOG, "updateSomeCrimes : mAdapter.notifyItemChanged(i) , i = " + i) ;
            }
        }
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
            return mCrimes.size();
        }

        List<Crime> getList(){
            return mCrimes ;
        }
    }
}

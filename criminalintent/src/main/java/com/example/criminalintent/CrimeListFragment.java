package com.example.criminalintent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CrimeListFragment extends Fragment {

    private static final String LOG = "CrimeListFragment" ;
    private RecyclerView mcCrimeRecyclerView ;
    private RecyclerView.Adapter<CrimeHolder> mAdapter ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime_list, container, false) ;
        mcCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view) ;
        mcCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI() ;
        return view ;
    }

    private void updateUI() {
        CrimeLab mCrimeLab = CrimeLab.get(getActivity()) ;
        List<Crime> crimes = mCrimeLab.getCrimes() ;
        mAdapter = new CrimeAdapter(crimes) ;
        mcCrimeRecyclerView.setAdapter(mAdapter);
        mcCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private class CrimeHolder extends RecyclerView.ViewHolder{
        private TextView mTitleTextView ;
        private TextView mDateTextView ;
        private Crime mCrime ;

        CrimeHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.list_item_crime, viewGroup, false));

            mTitleTextView = itemView.findViewById(R.id.crime_list_title) ;
            mDateTextView = itemView.findViewById(R.id.crime_list_date) ;
        }

        void bind(Crime crime) {
            mCrime = crime ;
            Log.d(LOG, "CrimeHolder bind() : crime.getTitle() = " + crime.getTitle()) ;
            Log.d(LOG, "mTitleTextView == null:" + (mTitleTextView == null) ) ;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
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
            return new CrimeHolder(inflater, parent);
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
    }
}

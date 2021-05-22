package com.example.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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

public class CrimeListFragment extends Fragment {

    private static final String LOG = "CrimeListFragment" ;
    private RecyclerView mcCrimeRecyclerView ;
    private CrimeAdapter mAdapter ;
    private int mCrimeChanged;

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
        updateUI();
    }

    private void updateUI() {
        CrimeLab mCrimeLab = CrimeLab.get(getActivity()) ;
        List<Crime> crimes = mCrimeLab.getCrimes();
        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mcCrimeRecyclerView.setAdapter(mAdapter);
            mcCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        } else {
            if (mCrimeChanged >= 0) {
                mAdapter.notifyItemChanged(mCrimeChanged);
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
            Log.d(LOG, "CrimeHolder bind() : crime.getTitle() = " + crime.getTitle()) ;
            Log.d(LOG, "mTitleTextView == null:" + (mTitleTextView == null) ) ;
            mTitleTextView.setText(crime.getTitle());
            String dateString = DateFormat
                    .format( "EEEE, dd MMM, yyyy", mCrime.getDate())
                    .toString() ;
            mDateTextView.setText(dateString);
            mCrimeSolved.setVisibility(mCrime.getSolved()? View.VISIBLE:View.GONE);
        }

        @Override
        public void onClick(View v) {
            Intent intent = CrimeActivity.newIntent(getActivity(), mCrime.getID()) ;
            int absAdapterPosition = CrimeHolder.this.getAbsoluteAdapterPosition() ;
            Log.d(LOG, "getAbsoluteAdapterPosition() = " + absAdapterPosition) ;
            int bindAdapterPosition = CrimeHolder.this.getBindingAdapterPosition() ;
            Log.d(LOG, "getBindingAdapterPosition() = " + bindAdapterPosition) ;
            mCrimeChanged = bindAdapterPosition ;
            startActivity(intent);
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
    }
}

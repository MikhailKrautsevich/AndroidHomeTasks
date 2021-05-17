package com.example.criminalintent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        mAdapter = new CrimeAdapter(crimes);
        mcCrimeRecyclerView.setAdapter(mAdapter);
        mcCrimeRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView ;
        private TextView mDateTextView ;
        private Crime mCrime ;

        CrimeHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.list_item_crime, viewGroup, false));
            itemView.setOnClickListener(this);

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

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + getString(R.string.is_clicked),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class SeriousCrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView ;
        private TextView mDateTextView ;
        private Button mCallThePoliceBtn ;
        private Crime mCrime ;

        SeriousCrimeHolder(LayoutInflater inflater, ViewGroup viewGroup) {
            super(inflater.inflate(R.layout.list_item_crime_with_police, viewGroup, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.crime_list_title) ;
            mDateTextView = itemView.findViewById(R.id.crime_list_date) ;
            mCallThePoliceBtn = itemView.findViewById(R.id.btn_call_the_police) ;
            mCallThePoliceBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),
                            getText(R.string.button_was_clicked),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }

        void bind(Crime crime) {
            mCrime = crime ;
            Log.d(LOG, "SeriousCrimeHolder bind() : crime.getTitle() = " + crime.getTitle()) ;
            Log.d(LOG, "mTitleTextView == null:" + (mTitleTextView == null) ) ;
            mTitleTextView.setText(crime.getTitle());
            mDateTextView.setText(crime.getDate().toString());
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(),
                    mCrime.getTitle() + getString(R.string.is_clicked),
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes ;

        CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes ;
            Log.d(LOG, "CrimeAdapter constructor: mCrimes.length = " + mCrimes.size()) ;
        }

        @Override
        public int getItemViewType(int position) {
            Crime crime = mCrimes.get(position) ;
            if (crime.getRequiresPolice()) {
                return 1 ;
            } else return 0 ;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity()) ;
            if (viewType == 0) {
            return new CrimeHolder(inflater, parent) ;
            } else return new SeriousCrimeHolder(inflater, parent) ;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position) ;
            if (holder instanceof CrimeHolder) {
                CrimeHolder crimeHolder = (CrimeHolder) holder ;
                crimeHolder.bind(crime);
            } else if (holder instanceof SeriousCrimeHolder) {
                SeriousCrimeHolder seriousCrimeHolder = (SeriousCrimeHolder) holder ;
                seriousCrimeHolder.bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }
}

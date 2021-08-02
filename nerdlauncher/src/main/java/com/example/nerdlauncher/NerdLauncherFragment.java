package com.example.nerdlauncher;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView ;
    
    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false) ;

        mRecyclerView = v.findViewById(R.id.app_recycler_view) ;
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();

        return v;
    }

    private void setAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN) ;
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER) ;

        PackageManager pm = getActivity().getPackageManager() ;
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0) ;

        Log.i("NerdLauncherInfo", "Found " + activities.size() + " activities") ;

    }
}

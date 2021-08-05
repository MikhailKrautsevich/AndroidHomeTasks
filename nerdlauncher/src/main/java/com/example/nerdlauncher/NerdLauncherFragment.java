package com.example.nerdlauncher;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.Collections;
import java.util.List;

public class NerdLauncherFragment extends Fragment {

    private RecyclerView mRecyclerView;

    public static NerdLauncherFragment newInstance() {
        return new NerdLauncherFragment();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_nerd_launcher, container, false);

        mRecyclerView = v.findViewById(R.id.app_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter();
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        return v;
    }

    private void setAdapter() {
        Intent startupIntent = new Intent(Intent.ACTION_MAIN);
        startupIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PackageManager pm = getActivity().getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(startupIntent, 0);
        Collections.sort(activities, (resolveInfo, resolveInfo1) -> String.CASE_INSENSITIVE_ORDER.compare(
                resolveInfo.loadLabel(pm).toString(),
                resolveInfo1.loadLabel(pm).toString()));

        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ResolveInfo mInfo ;
        private TextView mNameTextView ;
        private ImageView mImageView ;

        public ActivityHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.app_title_textview) ;
            mImageView = itemView.findViewById(R.id.icon_image) ;
        }

        public void bindActivity(ResolveInfo info) {
            mInfo = info ;
            PackageManager pm = getActivity().getPackageManager() ;
            String name = info.loadLabel(pm).toString() ;
            mNameTextView.setText(name);
            mNameTextView.getRootView().setOnClickListener(this);
            Drawable icon = info.loadIcon(pm) ;
            if (icon != null) {
                mImageView.setImageDrawable(icon);
            }
        }

        @Override
        public void onClick(View view) {
            ActivityInfo activityInfo = mInfo.activityInfo ;
            Intent i = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName, activityInfo.name) ;
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
            startActivity(i);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder> {
        private final List<ResolveInfo> mActivities ;

        public ActivityAdapter(List<ResolveInfo> activities){
            mActivities = activities ;
        }

        @NonNull
        @Override
        public ActivityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity()) ;
 //           View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false) ;    //simple default layout
            View view = inflater.inflate(R.layout.activity_element, parent, false) ;
            return new ActivityHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityHolder holder, int position) {
            ResolveInfo info = mActivities.get(position) ;
            holder.bindActivity(info);
        }

        @Override
        public int getItemCount() {
            return mActivities.size();
        }
    }
}

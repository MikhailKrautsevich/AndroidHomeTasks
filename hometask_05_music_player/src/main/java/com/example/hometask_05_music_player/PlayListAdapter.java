package com.example.hometask_05_music_player;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListHolder> {

    final private String LOG_TAG = "myLogs";
    private ArrayList<Song> playlist ;

    PlayListAdapter(ArrayList<Song> playlist) {
        this.playlist = playlist ;
    }

    @NonNull
    @Override
    public PlayListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = LayoutInflater.from(context) ;
        View view = inflater.inflate(R.layout.playlist_element , parent , false) ;
        return new PlayListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayListHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }

    class PlayListHolder extends RecyclerView.ViewHolder {
        TextView title ;
        ImageView playing ;
        ImageView pause ;
        int position = -1 ;

        PlayListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titlePlace) ;
            pause = itemView.findViewById(R.id.pause) ;
            playing = itemView.findViewById(R.id.playing) ;
        }

        void bind(final int position) {
            allGone();
            this.position = position ;
            title.setText(playlist.get(position).getTitle());
            if (playlist.get(position).getPlaying()) {
                showPlaying();}
            if (playlist.get(position).getPaused()) {
                showPaused();
            }
            itemView.setOnClickListener(new StartListener());
        }

        class StartListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(v.getContext() , PlayerService.class) ;
                Log.d(LOG_TAG, "onClick , position = " + position) ;
                if (position != -1) {
                    startIntent.putExtra("songPosition", position);
                    v.getContext().getApplicationContext().startService(startIntent);
                }
            }
        }

        void allGone() {
            playing.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
        }

        void showPaused() {
            playing.setVisibility(View.GONE);
            pause.setVisibility(View.VISIBLE);
        }

        void showPlaying() {
            playing.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        }
    }
}

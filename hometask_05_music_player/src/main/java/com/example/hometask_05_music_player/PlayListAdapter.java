package com.example.hometask_05_music_player;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListHolder> {

    final private String LOG_TAG = "myLogs";
    private int playListSize ;
    private ArrayList<PlayListHolder> holders;
    PlayListAdapter(int playListSize1 ) {
        playListSize = playListSize1 ;
        holders = new ArrayList<>() ;
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
        return playListSize;
    }

     int getHoldersSize() {
        return holders.size() ;
    }

     void refreshIcons(int playingPosition) {
        Log.d(LOG_TAG, "Adapter - Метод  refreshIcons - точка входа " + playingPosition);
        for (PlayListHolder holder : holders) {
            if (playingPosition != holder.position) {
                holder.allGone();
                Log.d(LOG_TAG, "adapter - Метод  refreshIcons - ветка allGone " + holder.position);
            } else if (playingPosition == holder.position) {
                holder.showPlaying();
                Log.d(LOG_TAG, "adapter - Метод  refreshIcons - ветка isPlaying " + holder.position);
            }
        }
    }

    class PlayListHolder extends RecyclerView.ViewHolder {
        TextView title ;
        ImageView playing , pause ;
        int position = -1 ;

        PlayListHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titlePlace) ;
            pause = itemView.findViewById(R.id.pause) ;
            playing = itemView.findViewById(R.id.playing) ;
            holders.add(this) ;
        }

        void bind(final int position) {
            this.position = position ;
            playing.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
            title.setText(MainActivity.playListMain.get(position).get("songTitle"));
            itemView.setOnClickListener(new StartListener());
        }

        class StartListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                setCustomListenerToService();
                if (playing.getVisibility() == View.GONE && pause.getVisibility() == View.GONE) {
                    Intent startIntent = new Intent(v.getContext() , PlayerService.class) ;
                    Log.d(LOG_TAG, "onClick , position " + position) ;
                    if (position != -1) {
                        startIntent.putExtra(PlayerService.SONGPOSITION, position);
                        v.getContext().getApplicationContext().startService(startIntent);
                        playing.setVisibility(View.VISIBLE);
                        Log.d(LOG_TAG, "onClick , start playing " + position) ;
                    }
                    if (position == -1) {
                        Toast.makeText(v.getContext(), "I have some problems here", Toast.LENGTH_LONG).show();
                    }
                } else if (playing.getVisibility() == View.GONE
                        && pause.getVisibility() == View.VISIBLE) {
                    MediaPlayer player = MainActivity.playerService.mediaPlayer ;
                    player.start();
                    pause.setVisibility(View.GONE);
                    playing.setVisibility(View.VISIBLE);
                    Log.d(LOG_TAG, "onClick , continued playing position " + position) ;
                } else if (playing.getVisibility() == View.VISIBLE
                        && pause.getVisibility() == View.GONE) {
                    MediaPlayer player = MainActivity.playerService.mediaPlayer ;
                    player.pause();
                    playing.setVisibility(View.GONE);
                    pause.setVisibility(View.VISIBLE);
                    Log.d(LOG_TAG, "onClick , paused position " + position) ;
                }
            }
        }

        void allGone() {
            playing.setVisibility(View.GONE);
            pause.setVisibility(View.GONE);
        }

        void showPlaying() {
            playing.setVisibility(View.VISIBLE);
            pause.setVisibility(View.GONE);
        }

        void setCustomListenerToService(){
            Log.d(LOG_TAG, "OnClick - садим Листенера");
            MainActivity.playerService.setListener(new PlayerService.CustomListener() {
                @Override
                public void refreshIcons(int playingPosition) {
                PlayListAdapter.this.refreshIcons(playingPosition);
                }
            });
        }
    }
}

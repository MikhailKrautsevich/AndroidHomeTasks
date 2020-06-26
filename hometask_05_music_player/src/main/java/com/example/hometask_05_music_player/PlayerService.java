package com.example.hometask_05_music_player;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import java.io.IOException;
import java.util.ArrayList;

public class PlayerService extends Service {

    private final String LOG_TAG = "myLogs";
    private ArrayList<Song> playList ;
    private MediaPlayer mediaPlayer ;
    private int playListSize = 0 ;
    private int playlistPosition = 0 ;
    private boolean isBinded = false ;
    private String currentTitle ;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service OnCreate") ;
        SongsManager songsManager = new SongsManager() ;
        playList = songsManager.getPlayList() ;
        playListSize = playList.size() ;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service onStartCommand " + startId);
        final String songPosition = "songPosition";
        if (intent.hasExtra(songPosition)) {
            int curPosition = intent.getIntExtra(songPosition, -2) ;
            if (curPosition >= 0 ) {
                if (curPosition != playlistPosition) {
                    playlistPosition = curPosition ;
                    setAllSongBooleanFalse();
                    Song song = playList.get(playlistPosition) ;
                    String startPath = song.getPath();
                    String startTitle = song.getTitle();
                    song.setIsPlaying(true);
                    currentTitle = startTitle ;
                    if (mediaPlayer != null) {
                        releaseResourses();
                    }
                    mediaPlayer = new MediaPlayer() ;
                    if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                        try {
                            mediaPlayer.setDataSource(startPath);
                            mediaPlayer.setLooping(false);
                            mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                            Log.d(LOG_TAG, "Service onStartCommand : Service trying to set " + startPath + " _ " + startId);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Service onStartCommand : Service crashed trying to set  " + startPath);
                        }
                        try {
                            mediaPlayer.prepare();
                            Log.d(LOG_TAG, "Service onStartCommand : Service trying to prepare " + startPath + " _ " + startId);
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(LOG_TAG, "Service onStartCommand : Service crashed trying to prepare " + startPath);
                        }
                        mediaPlayer.start();
                        Log.d(LOG_TAG, "Service onStartCommand : Service is playing " + startPath);
                        if (!isBinded && mediaPlayer.isPlaying()) showNotification(startTitle);
                    } else {
                        Log.d(LOG_TAG, "Service onStartCommand : Media is just playing");
                    }
                    if (isBinded) {
                        MainActivity.setPlaylist(playList);
                        MainActivity.notifyChanges();
                    }
                } else if (curPosition == playlistPosition) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.pause();
                            setAllSongBooleanFalse();
                            playList.get(playlistPosition).setIsPaused(true);
                        } else if (!mediaPlayer.isPlaying()) {
                                mediaPlayer.start();
                                setAllSongBooleanFalse();
                                playList.get(playlistPosition).setIsPlaying(true) ;
                        }
                    }
                    if (isBinded) {
                        MainActivity.setPlaylist(playList);
                        MainActivity.notifyChanges();
                    }
                }
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        releaseResourses();
        Log.d(LOG_TAG, "Service onDestroy : Метод releaseResourses()") ;
        super.onDestroy();
        Log.d(LOG_TAG, "Service onDestroy : Service OnDestroy") ;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "Service : Метод onBind") ;
        isBinded = true ;
        return new PlayerBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "Service : Метод onUnBind") ;
        isBinded = false ;
        return false;
    }

    class PlayerBinder extends Binder {
        PlayerService getPlayerService() {
            Log.d(LOG_TAG, "Service : Создали новый PlayerBinder") ;
            return PlayerService.this ;
        }
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mp != null) {
                releaseResourses();
                Log.d(LOG_TAG, "Service : Отработал блок кода в MediaPlayer.OnCompletionListener() - releaseResourses()");
            }
            playlistPosition++;
            setAllSongBooleanFalse();
            if (playlistPosition > playListSize - 1) {
                Log.d(LOG_TAG, "Service : Отработал блок кода в MediaPlayer.OnCompletionListener() - конец плейлиста");
                playlistPosition = -1;
                if (isBinded) {
                    MainActivity.setPlaylist(playList);
                    MainActivity.notifyChanges();
                }
                stopSelf();
            } else {
                mp = new MediaPlayer();
                mediaPlayer = mp;
                Song curSong = playList.get(playlistPosition) ;
                String curPath = curSong.getPath();
                currentTitle = curSong.getTitle();
                curSong.setIsPlaying(true);
                mp.setOnCompletionListener(this);
                try {
                    mp.setDataSource(curPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.setLooping(false);
                try {
                    mp.prepare();
                    Log.d(LOG_TAG, "Service trying to prepare " + curPath);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Service crashed trying to prepare " + curPath);
                }
                mp.start();
                if (isBinded) {
                    MainActivity.setPlaylist(playList);
                    MainActivity.notifyChanges();
                }
                if (!isBinded && mediaPlayer.isPlaying()) {
                    showNotification(currentTitle);}
                Log.d(LOG_TAG, "Service is playing " + curPath);
            }
        }
    }

    private void showNotification(String text) {
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(this, MainActivity.CHANNEL_ID)
                .setContentTitle("Task5 Player")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground) ;
        Notification notification = noteBuilder.build() ;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE) ;
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                notificationManager.createNotificationChannel(MainActivity.CreateChannel());}
            notificationManager.notify(1, notification);
        }
    }

    void releaseResourses() {
        if (mediaPlayer != null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null ;
        }
    }

    public void setIsBinded() {
        isBinded = true ;
    }

    public void setIsUnBinded() {
        isBinded = false ;
    }

    public String getTitle() {
        return currentTitle ;
    }

    boolean playerIsPlaying() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) return true;
        else return false ;
    }

    ArrayList<Song> getPlayList() { return playList ;}

    void setAllSongBooleanFalse() {
        for (Song song : playList) {
            song.setIsPlaying(false);
            song.setIsPaused(false);
        }
    }
}

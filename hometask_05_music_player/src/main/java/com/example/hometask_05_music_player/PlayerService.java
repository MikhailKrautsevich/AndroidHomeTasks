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
import java.util.HashMap;

public class PlayerService extends Service {

    private static String SONGTITLE = "songTitle" ;
    private static String SONGPATH = "songPath" ;
    private final String LOG_TAG = "myLogs";
    ArrayList<HashMap<String, String>> playList = null ;
    MediaPlayer mediaPlayer = null ;
    private int playListSize = 0 ;
    private int playlistPosition = 0 ;
    private CustomListener listener = null ;
    private boolean isBinded = false ;
    private String currentTitle = null ;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "Service OnCreate") ;
        playList = MainActivity.playListMain ;
        playListSize = playList.size() ;
        Log.d(LOG_TAG, "Service OnCreate : Playlist size = " + playListSize) ;

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "Service onStartCommand " + startId);
        String SONGPOSITION = "songPosition";
        if (intent.hasExtra(SONGPOSITION)) {
            if (mediaPlayer != null) releaseResourses();
            mediaPlayer = new MediaPlayer();
            playlistPosition = intent.getIntExtra(SONGPOSITION, 8);
            Log.d(LOG_TAG, "Service onStartCommand : Позиция в плейлисте при старте плеера = " + playlistPosition);
            String startPath = playList.get(playlistPosition).get(SONGPATH);
            String startTitle = playList.get(playlistPosition).get(SONGTITLE);
            currentTitle = startTitle ;
            Log.d(LOG_TAG, "Service onStartCommand : Service create mediaplayer, playlistPosition = " + playlistPosition);
            if (listener != null) {
                listener.refreshIcons(playlistPosition);
                Log.d(LOG_TAG, "listener - Метод refreshIcons");
            }

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
            if (listener != null) {setRightIcons();}
            return PlayerService.this ;
        }
    }

    void releaseResourses() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null ;
    }

    private class MyOnCompletionListener implements MediaPlayer.OnCompletionListener {
        @Override
        public void onCompletion(MediaPlayer mp) {
            if (mp != null) {
                releaseResourses();
                Log.d(LOG_TAG, "Service : Отработал блок кода в MediaPlayer.OnCompletionListener() - releaseResourses()");
            }
            playlistPosition++;
            if (playlistPosition > playListSize - 1) {
                Log.d(LOG_TAG, "Service : Отработал блок кода в MediaPlayer.OnCompletionListener() - конец плейлиста");
                playlistPosition = -1;
                if (listener != null) {
                    listener.refreshIcons(playlistPosition);
                    Log.d(LOG_TAG, "Service : Отработал блок кода в MediaPlayer.OnCompletionListener() - refreshIcons (конец плейлиста)");
                }
            } else {
                mp = new MediaPlayer();
                mediaPlayer = mp;
                String pathCurrent = playList.get(playlistPosition).get(SONGPATH);
                String titleCurrent = playList.get(playlistPosition).get(SONGTITLE);
                currentTitle = titleCurrent ;
                if (listener != null) {
                    listener.refreshIcons(playlistPosition);
                    Log.d(LOG_TAG, "Service : Отработал блок кода в MediaPlayer.OnCompletionListener() - refreshIcons");
                }
                mp.setOnCompletionListener(this);
                try {
                    mp.setDataSource(pathCurrent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.setLooping(false);
                try {
                    mp.prepare();
                    Log.d(LOG_TAG, "Service trying to prepare " + pathCurrent);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(LOG_TAG, "Service crashed trying to prepare " + pathCurrent);
                }
                mp.start();
                if (!isBinded && mediaPlayer.isPlaying()) showNotification(titleCurrent);
                Log.d(LOG_TAG, "Service is playing " + pathCurrent);
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

    public interface CustomListener{
        void refreshIcons (int playingPosition) ;
    }

    public void setListener(CustomListener listener) {
        this.listener = listener ;
    }

     int getCurrentPosition() {
        return playlistPosition ;
     }

    public void setIsBinded() {
        isBinded = true ;
    }

    public void setRightIcons() {
        listener.refreshIcons(playlistPosition);
    }

    public String getTitle() {
        return currentTitle ;
    }

    boolean playerIsPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}

package com.example.hometask_05_music_player;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    static final String CHANNEL_ID = "Channel_ID" ;
    static ArrayList<HashMap<String, String>> playListMain ;
    private final int REQUEST_FOR_PERM = 123;
    final private String LOG_TAG = "myLogs";
    static PlayerService playerService ;
    private Intent serviceIntent ;
    private SongsManager manager ;
    private RecyclerView recyclerView ;
    private PlayListAdapter adapter ;

    final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceIBinder) {
            PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) serviceIBinder ;
            playerService = playerBinder.getPlayerService() ;
            playerService.setIsBinded();
            Log.d(LOG_TAG, "MainActivity : onServiceConnected") ;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(LOG_TAG, "MainActivity : onServiceDISConnected") ;
            playerService = null ;
            Log.d(LOG_TAG, "MainActivity : onServiceDISConnected - занулили ссылку на сервис") ;
        }
    } ;


    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity OnCreate started") ;
        setContentView(R.layout.activity_main);

        serviceIntent = new Intent(this , PlayerService.class);
        Log.d(LOG_TAG, "MainActivity : I create intent");
        bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE) ;
        Log.d(LOG_TAG, "OnCreate - I tried to bind service");

        TextView sorryText = findViewById(R.id.sorry);
        recyclerView = findViewById(R.id.recyclerPlaylist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) ;
        recyclerView.setLayoutManager(linearLayoutManager) ;
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));

        getPlaylist();       // если сервис создан, получаем плейлист оттуда

        if (playListMain != null) {
            adapter = new PlayListAdapter(playListMain.size());
            recyclerView.setAdapter(adapter);
            sorryText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MainActivity OnResume") ;
        if (playerService == null) {
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE) ;
            Log.d(LOG_TAG, "MainActivity OnResume : I tried to bind service");
        }

        // Вот тут проблемный кусок кода

        int sizeOfHoldersArray = adapter.getHoldersSize();
        Log.d(LOG_TAG, "MainActivity OnResume : size " + sizeOfHoldersArray);
        if (sizeOfHoldersArray != 0) {
            final int current = playerService.getCurrentPosition() ;
            if (playerService.playerIsPlaying()) {
                playerService.setListener(new PlayerService.CustomListener() {
                    @Override
                    public void refreshIcons(int playingPosition) {
                        adapter.refreshIcons(playingPosition);
                    }
                });
                adapter.refreshIcons(current);
                recyclerView.refreshDrawableState();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "MainActivity OnPause") ;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "MainActivity OnStop") ;
        String text = "Player is playing " + playerService.getTitle() ;
        if (playerService != null && playerService.mediaPlayer.isPlaying()) {
            playerService.startForeground(1 , buildNotification(text));
            Log.d(LOG_TAG, "MainActivity OnStop : player is playing");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "MainActivity OnDestroy") ;
        if (playerService != null ) {
            if (!playerService.mediaPlayer.isPlaying()) {
                playerService.mediaPlayer.stop();
                playerService.mediaPlayer.release();
                playerService.mediaPlayer = null ;
                Log.d(LOG_TAG, "MainActivity OnDestroy : Освободили ресурсы плеера");
            }
            unbindService(serviceConnection);
            playerService = null ;
        } else if (playerService == null) {
            Log.d(LOG_TAG, "I tried to unBind service but playerService = null");
        }
        super.onDestroy();
    }

    @RequiresApi(api = VERSION_CODES.M)
    private void getPlaylist() {
        if (playerService == null || playerService.playList == null ) {
            manager = new SongsManager();
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED) {
                playListMain = manager.getPlayList();
                Log.d(LOG_TAG, "OnCreate - I get new playlist without problems") ;
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE}, REQUEST_FOR_PERM);
            }
            manager = null ;
        } else if (playerService!= null && playerService.playList != null) {
            playListMain = playerService.playList ;
            Log.d(LOG_TAG, "OnCreate - I get playlist from service") ;
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FOR_PERM) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                manager = new SongsManager();
                playListMain = manager.getPlayList();
                manager = null;
            } else {
                Toast.makeText(getApplicationContext(), "I need permissions", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(LOG_TAG, "onRequestPermissionsResult - Default branch");
        }
    }

    private Notification buildNotification(String text) {
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Task5 Player")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground) ;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE) ;
        Notification notification = noteBuilder.build() ;
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            notificationManager.createNotificationChannel(CreateChannel());}
        return notification;
    }

    static NotificationChannel CreateChannel(){
        NotificationChannel playerChannel = null;
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            String name = "Channel" ;
            String description = "Channel for music player" ;
            playerChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH) ;
            playerChannel.setDescription(description);
            }
        return playerChannel ;
    }
}

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

public class MainActivity extends AppCompatActivity {

    static final String CHANNEL_ID = "Channel_ID" ;
    private static final String LOG_TAG = "myLogs";
    private static ArrayList<Song> playListMain ;
    private static RecyclerView recyclerView ;
    private TextView sorryText ;
    private final int REQUEST_FOR_PERM = 123;
    private PlayerService playerService ;

    final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder serviceIBinder) {
            PlayerService.PlayerBinder playerBinder = (PlayerService.PlayerBinder) serviceIBinder ;
            playerService = playerBinder.getPlayerService() ;
            playerService.setIsBinded();
            playListMain = playerService.getPlayList() ;
            recyclerView.setAdapter(new PlayListAdapter(playListMain));
            notifyChanges();
            if (playListMain.size() > 0) {
                sorryText.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
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

        sorryText = findViewById(R.id.sorry);
        recyclerView = findViewById(R.id.recyclerPlaylist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) ;
        recyclerView.setLayoutManager(linearLayoutManager) ;
        recyclerView.addItemDecoration(new DividerItemDecoration(getBaseContext(), DividerItemDecoration.VERTICAL));
        playListMain = new ArrayList<>();

        if (playListMain != null) {
            PlayListAdapter adapter = new PlayListAdapter(playListMain);
            recyclerView.setAdapter(adapter);
            if (playListMain.size() == 0) {
                sorryText.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "MainActivity OnResume") ;
        if (playerService == null) {
            startPlayerService();
            Log.d(LOG_TAG, "MainActivity OnResume : startPlayerService() ");
        }
        if (playerService != null) {
            playerService.setIsBinded();                                  // теперь перестает менять нотификацию в развернутом состоянии
            playListMain = playerService.getPlayList() ;
            notifyChanges();
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
        if (playerService != null && playerService.playerIsPlaying()) {
            playerService.setIsUnBinded();                                          // чтобы меняло нотификацию в свернутом состоянии
            playerService.startForeground(1 , buildNotification(text));
            Log.d(LOG_TAG, "MainActivity OnStop : player is playing");
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "MainActivity OnDestroy") ;
        if (playerService != null ) {
            if (!playerService.playerIsPlaying()) {
                playerService.releaseResourses();
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
    private void startPlayerService() {
        if (playerService == null) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED) {
                Intent serviceIntent = new Intent(this , PlayerService.class);
                Log.d(LOG_TAG, "MainActivity : StartPlayerService() I create intent");
                bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE) ;
            } else {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE}, REQUEST_FOR_PERM);
            }
        }
    }

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FOR_PERM) {
            if (grantResults.length > 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                startPlayerService();
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

    static void setPlaylist(ArrayList<Song> playList) {
        playListMain = playList ;
        Log.d(LOG_TAG, "Main Activity - Метод setPlaylist (Получил Playlist от сервиса, size = " + playList.size() + ")");
    }

    static void notifyChanges() {
        if (recyclerView != null) {
            if (recyclerView.getAdapter() != null) {
                recyclerView.getAdapter().notifyDataSetChanged();
                Log.d(LOG_TAG, "Main Activity - Метод notifyChanges (обновил данные)");
            }
        }
    }
}

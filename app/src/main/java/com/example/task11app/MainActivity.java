package com.example.task11app;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private WebView webviewContent;
    private MediaPlayer mediaPlayer;
    private ImageButton nextTaskButton;
    private static final String CHANNEL_ID = "example_channel";
    public static final String CHANNEL_DELAY_ID = "delayed_channel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        createNotificationChannel2();

        // task 1
        webviewContent = (WebView) findViewById(R.id.webview_content);
        webviewContent.setWebViewClient(new WebViewClient());
        webviewContent.getSettings().setJavaScriptEnabled(true);
        webviewContent.loadUrl("https://github.com/poborn1k/task11");

        // task 2, 3

        ImageButton playButton = findViewById(R.id.play_music_button);
        mediaPlayer = new MediaPlayer();

        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(playButton, "rotation", 0f, 360f);
        rotateAnim.setDuration(2000);
        rotateAnim.setRepeatCount(ObjectAnimator.INFINITE);
        rotateAnim.setRepeatMode(ObjectAnimator.RESTART);

        try {
            mediaPlayer.setDataSource("https://www.bensound.com/bensound-music/bensound-ukulele.mp3");
            mediaPlayer.prepare();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, CHANNEL_ID).setSmallIcon(R.drawable.notifications_icon).setContentTitle("Уводомление музыки").setContentText("Музыка начала производиться").setPriority(NotificationCompat.PRIORITY_DEFAULT);
                    NotificationManager notificationManager = getSystemService(NotificationManager.class);
                    notificationManager.notify(1, builder.build());
                    scheduleNotification(5000);

                    playButton.setImageResource(R.drawable.pause_icon);
                    mediaPlayer.start();
                    rotateAnim.start();
                } else {
                    mediaPlayer.pause();
                    rotateAnim.end();
                    playButton.setImageResource(R.drawable.play_icon);
                }
            }
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Example Channel";
            String description = "Channel for example notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationChannel2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Delayed Notifications";
            String description = "Channel for delayed example notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_DELAY_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void scheduleNotification(long delay) {
        Intent notificationIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long futureInMillis = System.currentTimeMillis() + delay;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
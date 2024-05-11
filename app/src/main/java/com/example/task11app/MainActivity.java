package com.example.task11app;

import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private WebView webviewContent;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // task 1
        webviewContent = (WebView) findViewById(R.id.webview_content);
        webviewContent.setWebViewClient(new WebViewClient());
        webviewContent.getSettings().setJavaScriptEnabled(true);
        webviewContent.loadUrl("https://github.com/poborn1k/firstTRPP");

        // task 2
        ImageButton playButton = findViewById(R.id.play_music_button);
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource("???");
            mediaPlayer.prepare(); // Можно использовать prepareAsync() для сетевых потоков
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                } else {
                    mediaPlayer.pause();
                }
            }
        });

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
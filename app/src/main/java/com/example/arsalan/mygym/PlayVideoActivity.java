package com.example.arsalan.mygym;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.arsalan.mygym.models.MyConst;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity {
    public static final String KEY_VIDEO_URL = "video url key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        VideoView videoView = (VideoView) findViewById(R.id.videoPlayer);
//Use a media controller so that you can scroll the video contents
//and also to pause, start the video.
        String url= MyConst.BASE_CONTENT_URL + getIntent().getStringExtra(KEY_VIDEO_URL);
        String fileName = url.substring(url.lastIndexOf('/') + 1);

        String filePath = getExternalFilesDir(null) + File.separator + fileName;//ApiClient.BASE_API_URL+ getIntent().getStringExtra(KEY_VIDEO_URL);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(filePath);//setVideoURI(Uri.parse(url));

        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

    }
}

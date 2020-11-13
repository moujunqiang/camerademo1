package com.example.camerademo1;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;


/**
 * @描述: 视频播放类
 */
public class PictureVideoPlayActivity extends AppCompatActivity {
    private VideoView videoView;

    private ImageView mIvVideoPlay;
    //第一帧图片
    private ImageView mIvVideoFirst;

    private RelativeLayout mPreViewContain;

    private String videoPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        videoPath = getIntent().getStringExtra("path");


        mPreViewContain = findViewById(R.id.rl_preview_contain);
        videoView = new VideoView(getApplicationContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        videoView.setLayoutParams(params);
        mPreViewContain.addView(videoView);

        mIvVideoFirst = new ImageView(getApplicationContext());
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        mIvVideoFirst.setLayoutParams(imageParams);
        mPreViewContain.addView(mIvVideoFirst);

        mIvVideoPlay = findViewById(R.id.iv_video_play);


        mIvVideoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoView != null && videoView.isPlaying()) {
                    pauseVideo();
                } else {
                    playVideo();
                }
            }
        });
        videoView.setVideoPath(videoPath);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ViewGroup.LayoutParams lp = videoView.getLayoutParams();
                float videoWidth = mp.getVideoWidth();
                float videoHeight = mp.getVideoHeight();
                float viewWidth = videoView.getWidth();
                lp.height = (int) (viewWidth * (videoHeight / videoWidth));
                videoView.setLayoutParams(lp);


            }
        });

        if (videoPath != null) {
            mIvVideoFirst.setImageBitmap(getFirstBitMap());
        }
    }

    /**
     * 播放视频
     */
    void playVideo() {
        if (!videoView.isPlaying()) {
            videoView.start();
            mIvVideoFirst.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        pauseVideo();
    }

    void pauseVideo() {
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    /**
     * 获取第一帧作为封面
     */
    public Bitmap getFirstBitMap() {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {

            retriever.setDataSource(videoPath);
            //获得第一帧图片
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            retriever.release();
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        if (videoView != null) {
            videoView.stopPlayback();
            videoView.setOnCompletionListener(null);
            videoView.setOnPreparedListener(null);
            videoView = null;
            mPreViewContain.removeAllViews();
        }
        super.onDestroy();
        if (!isChangingConfigurations()) {
        }

    }


}


package com.swdrts.swvideo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.swdrts.swvideo.Constant;
import com.swdrts.swvideo.R;
import com.swdrts.swvideo.srcparse.VideoSrcCallback;
import com.swdrts.swvideo.srcparse.Youku;
import com.swdrts.swvideo.utils.SLog;

import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.widget.MediaController;
import tv.danmaku.ijk.media.widget.VideoView;

public class VideoPlayerActivity extends Activity {
    
    private Context mContext;
    private VideoView mVideoView;
    private View mBufferingIndicator;
    private MediaController mMediaController;

    private String mVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_player);
        
        mBufferingIndicator = findViewById(R.id.buffering_indicator);
        mMediaController = new MediaController(this);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);
        mVideoView.requestFocus();
        mVideoView.setOnCompletionListener(new OnCompletionListener() {
            
            @Override
            public void onCompletion(IMediaPlayer mp) {
                // TODO Auto-generated method stub
                mVideoView.stopPlayback();
                finish();
            }
        });

        Intent intent = getIntent();
        if (null != intent && !TextUtils.isEmpty(intent.getStringExtra(Constant.VIDEO_PATH))
                && -1 != intent.getIntExtra(Constant.TYPE_VIDEO, -1)) {
            String videoUrl = "";
            videoUrl = intent.getStringExtra(Constant.VIDEO_PATH);
            SLog.debug("video url-->" + mVideoPath);
            parseUrl(intent, videoUrl);
            return;
        }
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)
                && intentAction.equals(Intent.ACTION_VIEW)) {
            mVideoPath = intent.getDataString();
        }
        if (TextUtils.isEmpty(mVideoPath)) {
            SLog.debug("video path is empty");
            return;
        }
        SLog.debug("video path-->" + mVideoPath);

        mVideoView.setVideoPath(mVideoPath);
        mVideoView.requestFocus();
        mVideoView.start();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        mVideoView.stopPlayback();
        super.onDestroy();
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		
		super.onConfigurationChanged(newConfig);
	}
    private void parseUrl(Intent intent, String url) {
        switch (intent.getIntExtra(Constant.TYPE_VIDEO, -1)) {
            case Constant.TYPE_YOUKU:
                Youku.getVideoRealSrc(url, new VideoSrcCallback() {

                    @Override
                    public void onFinish(String src) {
                        // TODO Auto-generated method stub
                        SLog.debug("video real src-->" + src);
                        mVideoView.setVideoPath(src);
                        mVideoView.requestFocus();
                        mVideoView.start();
                    }

                    @Override
                    public void onFailed(String reason) {
                        // TODO Auto-generated method stub
                        SLog.error("onFaild-->" + reason);
                        Toast.makeText(mContext, reason, Toast.LENGTH_SHORT).show();
                    }
                });
                break;

            default:
                break;
        }
    }

}

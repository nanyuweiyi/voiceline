package com.carlos.voiceline;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nanyuweiyi.voiceline.VoiceLineView;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * ================================================
 * 描   述: 波形显示
 * 作   者：tnn
 * 创建日期：2018/5/3
 * 版   本：1.0.1
 * 修改日期：2018/5/4
 * ================================================
 */
public class MainActivity extends AppCompatActivity implements Runnable {
    private static MediaRecorder mMediaRecorder;
    private boolean isAlive = true;
    private static VoiceLineView voiceLineView;
    private MyHandler handler;
    private TextView tvStart, tvStop;

    private static class MyHandler extends Handler {
        WeakReference<Activity> mWeakReference;

        private MyHandler(Activity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final Activity activity = mWeakReference.get();
            if (activity != null) {
                if (mMediaRecorder == null) return;
                double ratio = (double) mMediaRecorder.getMaxAmplitude() / 100;
                double db = 0;// 分贝
                //默认的最大音量是100,可以修改，但其实默认的，在测试过程中就有不错的表现
                //你可以传自定义的数字进去，但需要在一定的范围内，比如0-200，就需要在xml文件中配置maxVolume
                //同时，也可以配置灵敏度sensibility
                if (ratio > 1) {
                    db = 20 * Math.log10(ratio);
                }
                //只要有一个线程，不断调用这个方法，就可以使波形变化
                voiceLineView.setVolume((int) (db));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        voiceLineView = findViewById(R.id.voicLine);
        tvStart = findViewById(R.id.tvstart);
        tvStop = findViewById(R.id.tvstop);
        handler = new MyHandler(this);

        Thread thread = new Thread(this);
        thread.start();

        tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMediaRecorder == null){
                    initMediaRecorder();
                }
                mMediaRecorder.start();
                Toast.makeText(MainActivity.this, "波形显示已开始", Toast.LENGTH_SHORT).show();
            }
        });
        tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                Toast.makeText(MainActivity.this, "波形显示已结束~~~", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initMediaRecorder(){
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "data.log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder.setOutputFile(file.getAbsolutePath());
        mMediaRecorder.setMaxDuration(1000 * 60 * 10);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void stop(){
        if (mMediaRecorder != null) {
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                mMediaRecorder = null;
                initMediaRecorder();
            }
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
    }

    @Override
    protected void onDestroy() {
        isAlive = false;
        mMediaRecorder.release();
        mMediaRecorder = null;
        super.onDestroy();
    }

    @Override
    public void run() {
        while (isAlive) {
            handler.sendEmptyMessage(0);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

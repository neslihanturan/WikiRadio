package wikiradio.neslihan.tur.org.wikiradio;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;
import wikiradio.neslihan.tur.org.wikiradio.ui.SeekBarController;

public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback{
    private String LOG_TAG = RadioActivity.class.getName();
    private Button playButton;
    private Button nextButton;
    private SeekBar seekBar;
    private int duration;
    private int amoungToupdate = -1;
    private Handler handler;
    private Runnable runnable;
    private int prevPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegate = this;
        startService(new Intent(RadioActivity.this, NotificationService.class));
        initViews();
        setListeners();


        //final SeekBar mSeelBar = new SeekBar(this);

        /*try {
            MediaPlayerController.play("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }

    public void initViews(){
        playButton = (Button) findViewById(R.id.playButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
    }
    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayerController.playOrPause("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        nextButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MediaPlayerController.playOrPause("https://upload.wikimedia.org/wikipedia/commons/b/ba/Aboun.ogg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {

                Log.d(LOG_TAG,"timer running");
                int targetPosition =  SingleMediaPlayer.getInstance().getCurrentPosition();
                //if (!(amoungToupdate * seekBar.getProgress() >= duration)) {

                        //int p = seekBar.getProgress();
                        //p += 1;
                /*while(prevPosition != targetPosition){
                    prevPosition++;
                    seekBar.setProgress(prevPosition);
                    Log.d(LOG_TAG, "Inner Handler, target pos:"+targetPosition+" prevpos:"+prevPosition);
                }*/

                seekBar.setProgress(targetPosition);

                //}

                prevPosition = targetPosition;
                if(amoungToupdate != Constant.SEEKBAR.STOP_SEEKBAR){
                    handler.postDelayed(this, amoungToupdate);
                }
                else{
                    handler.removeCallbacks(this);
                    Log.d("Runnable","ok");
                }

            }
        };




/*
        Timer mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Log.d(LOG_TAG,"timer running");
                        if (!(amoungToupdate * seekBar.getProgress() >= duration)) {
                            int p = seekBar.getProgress();
                            p += 1;
                            seekBar.setProgress(p);
                        }
                    }
                });
            }
        },amoungToupdate);*/
        //seekBar.setOnSeekBarChangeListener(new SeekBarListener());
        /*
        ObjectAnimator animation = ObjectAnimator.ofInt(seekBar, "progress", SingleMediaPlayer.getInstance().getDuration());
        animation.setDuration(500); // 0.5 second
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
        */
        /*
        handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                seekBar.setMax(SingleMediaPlayer.getInstance().getDuration());
                seekBar.setProgress(SingleMediaPlayer.getInstance().getCurrentPosition());
                handler.postDelayed(this, 200);

            }
        };
        handler.postDelayed(r, 200);
        */
    }

    /*private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
        private int smoothnessFactor = 10;
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            progress = Math.round(progress / smoothnessFactor);
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            seekBar.setProgress(Math.round((seekBar.getProgress() + (smoothnessFactor / 2)) / smoothnessFactor) * smoothnessFactor);
        }
    }*/





    @Override
    public void onMediaPlayerPaused() {
        stopSeekBar();
    }

    @Override
    public void onMediaPlayerPlaying() {
        startSeekBar();
    }

    private void stopSeekBar(){
        amoungToupdate = Constant.SEEKBAR.STOP_SEEKBAR;
    }
    private void startSeekBar(){
        duration = SingleMediaPlayer.getInstance().getDuration();
        seekBar.setMax(duration);
        Log.d(LOG_TAG,"onMediaPlayerPlaying");
        amoungToupdate = duration / 100;
        Log.d(LOG_TAG,"duration: "+duration+"\n" +
                "amoungToupdate: "+amoungToupdate);
        handler.postDelayed(runnable, amoungToupdate);
    }

}

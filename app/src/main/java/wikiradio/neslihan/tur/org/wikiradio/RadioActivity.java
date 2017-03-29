package wikiradio.neslihan.tur.org.wikiradio;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.CacheListener;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import wikiradio.neslihan.tur.org.wikiradio.action.AudioFileButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.action.AudioSourceSelector;
import wikiradio.neslihan.tur.org.wikiradio.action.TTSButtonListener;
import wikiradio.neslihan.tur.org.wikiradio.data.callback.AudioInfoCallbak;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerCallback;
import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.MediaPlayerController;
import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.notification.NotificationService;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CacheControlCallback;
import wikiradio.neslihan.tur.org.wikiradio.proxy.CommonsCacheController;
import wikiradio.neslihan.tur.org.wikiradio.ttscache.WikipediaSummaryCacheController;


public class RadioActivity extends AppCompatActivity implements MediaPlayerCallback, CacheListener,AudioInfoCallbak{
    private String LOG_TAG = RadioActivity.class.getName();
    private FloatingActionButton playButton;
    private FloatingActionButton nextButton;
    private FloatingActionButton fastForwardButton;
    private FloatingActionButton rewindButton;
    private static ProgressDialog waitingDialog;

    private static SeekBar seekBar;
    private TextView infoTextView;
    private TextView secondsTextView;
    private Button webButton;
    private int duration;
    private int amoungToupdate = -1;
    private Handler handler;
    private Runnable runnable;
    private static Toast toast;
    public static CacheControlCallback cacheControlCallback;
    public static CacheControlCallback ttsCacheControlCallback;
    private int prevPosition;
    //public static AudioFile nowPlayingAudio;
    public static Context context;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            // do something here
            Intent intent = new Intent(this, UserSettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WikipediaSummaryCacheController.getInstance(this).startToCaching();
        CommonsCacheController.getInstance(this).startToCaching();

        setContentView(R.layout.activity_radio);
        MediaPlayerController.delegateActivity = this;
        context = this;
        startService(new Intent(RadioActivity.this, NotificationService.class));
        Log.i(LOG_TAG,"created on thread:"+Thread.currentThread());
        initViews();
        setListeners();
    }

    public void initViews(){
        playButton = (FloatingActionButton) findViewById(R.id.playButton);
        nextButton = (FloatingActionButton)findViewById(R.id.nextButton);
        fastForwardButton = (FloatingActionButton)findViewById(R.id.forwardButton);
        rewindButton = (FloatingActionButton)findViewById(R.id.rewindButton);
        secondsTextView = (TextView)findViewById(R.id.secondTextView);
        waitingDialog = new ProgressDialog(RadioActivity.this);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        infoTextView = (TextView) findViewById(R.id.textView);
        webButton = (Button) findViewById(R.id.webButton);
    }

    public void setListeners(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(LOG_TAG,"playButton.OnClickListener");
                    lock();
                    AudioSourceSelector.operate(Constant.ACTION.PLAY_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        nextButton.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.i(LOG_TAG,"nextButton.OnClickListener");
                    lock();
                    AudioSourceSelector.operate(Constant.ACTION.NEXT_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        fastForwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AudioSourceSelector.operate(Constant.ACTION.FORWARD_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        rewindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AudioSourceSelector.operate(Constant.ACTION.REWIND_ACTION,context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        webButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.isAudioPlaying){
                    if(Constant.nowPlayingAudio!=null){
                        Log.i(LOG_TAG,Constant.nowPlayingAudio.getTitle());
                        Log.i(LOG_TAG,Constant.nowPlayingAudio.getPageUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.nowPlayingAudio.getPageUrl()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }



                }else{
                    if(Constant.nowPlayingFile!=null){
                        Log.i(LOG_TAG,Constant.nowPlayingFile.getTitle());
                        Log.i(LOG_TAG,Constant.nowPlayingFile.getPageUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.nowPlayingFile.getPageUrl()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    //webButton.setText("Dont click:)");
                }

            }
        });

        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                int targetPosition =  MediaPlayerController.getCurrentPosition();
                seekBar.setProgress(targetPosition);
                prevPosition = targetPosition;
                secondsTextView.setText(String.format("Time %02d:%02d:%02d: / %02d:%02d:%02d:",
                        TimeUnit.MILLISECONDS.toHours(targetPosition),
                        TimeUnit.MILLISECONDS.toMinutes(targetPosition) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(targetPosition)),
                        TimeUnit.MILLISECONDS.toSeconds(targetPosition) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(targetPosition)),

                        TimeUnit.MILLISECONDS.toHours(MediaPlayerController.getDuration()),
                        TimeUnit.MILLISECONDS.toMinutes(MediaPlayerController.getDuration()) -
                                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(MediaPlayerController.getDuration())),
                        TimeUnit.MILLISECONDS.toSeconds(MediaPlayerController.getDuration()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(MediaPlayerController.getDuration()))
                ));

                if(amoungToupdate != Constant.SEEKBAR.STOP_SEEKBAR){
                    handler.postDelayed(this, amoungToupdate);
                }
                else{
                    handler.removeCallbacks(this);
                    Log.i("Runnable","ok");
                }

            }
        };

    }

    public static void unregisterCacheListener(){
        Constant.proxy.unregisterCacheListener((CacheListener) context);
    }
    public static void registerCacheListener(){
        Constant.proxy.registerCacheListener((CacheListener) context, Constant.nowPlayingAudio.getAudioUrl());
    }

    public static void setSecondarySeekbarMax(){
        seekBar.setSecondaryProgress(seekBar.getMax());
    }

    private void lock(){
        playButton.setEnabled(false);
        nextButton.setEnabled(false);

    }
    private void unlock(){
        playButton.setEnabled(true);
        nextButton.setEnabled(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WikipediaSummaryCacheController.getInstance(context).destroyTTS();
        Log.i(LOG_TAG,"on stop activity");
        TTSButtonListener.onStopActivity();

    }

    @Override
    public void onMediaPlayerPaused() {
        unlock();
        updateText();
        setPausedView();
        stopSeekBar();
    }

    @Override
    public void onMediaPlayerPlaying() {
        unlock();
        updateText();
        setPlayingView();
        startSeekBar();
    }

    private void updateText(){
        if(Constant.isAudioPlaying){ //means audio file is playing
            infoTextView.setText("Source: commons.wikimedia.org"+"\nAudio Title: "+Constant.nowPlayingAudio.getTitle()+"\n Category:"+Constant.nowPlayingAudio.getCategory());
        }else {
            infoTextView.setText("Source: en.wikipedia.org"+"\nAudio Title: "+Constant.nowPlayingFile.getTitle());
        }
    }

    private void setPausedView(){
        playButton.setImageResource(android.R.drawable.ic_media_play);
    }

    private void setPlayingView(){
        playButton.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void stopSeekBar(){
        amoungToupdate = Constant.SEEKBAR.STOP_SEEKBAR;
    }
    private void startSeekBar(){
        duration = MediaPlayerController.getDuration();
        seekBar.setMax(duration);
        Log.i(LOG_TAG,"onMediaPlayerPlaying");
        amoungToupdate = duration / 1000;
        Log.i(LOG_TAG,"duration: "+duration+"\n" +
                "amoungToupdate: "+amoungToupdate);
        handler.postDelayed(runnable, amoungToupdate);
    }

    @Override
    public void onCacheAvailable(File cacheFile, String url, int percentsAvailable) {
        Log.i(LOG_TAG, String.format("onCacheAvailable. percents: %d, file: %s", percentsAvailable, cacheFile));
        if(percentsAvailable==100){
            //cacheControlCallback.onCurrentFileCached();
        }
        seekBar.setSecondaryProgress(seekBar.getMax()*percentsAvailable/100);
    }

    @Override
    public void onSuccess(AudioFile audioFile, Class sender) {
        AudioFileButtonListener.onSuccess(audioFile, this);
    }

    @Override
    public void onError(Class sender) {

    }

    public static void waitAnimation(){
        waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingDialog.setMessage("Loading. Please wait...");
        waitingDialog.show();
    }

    public static void dismissWaitAnimation(){
        waitingDialog.dismiss();
    }

    public static void replaceToast(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    /*
    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG,"on stop activity");
        TTSButtonListener.onStopActivity();
        super.onStop();
    }*/
}

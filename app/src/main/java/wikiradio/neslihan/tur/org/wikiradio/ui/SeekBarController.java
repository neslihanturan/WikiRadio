package wikiradio.neslihan.tur.org.wikiradio.ui;

import android.widget.SeekBar;

import wikiradio.neslihan.tur.org.wikiradio.mediaplayer.SingleMediaPlayer;

/**
 * Created by nesli on 27.12.2016.
 */

public class SeekBarController implements Runnable {
    SeekBar seekBar;

    public SeekBarController(SeekBar seekBar){
        this.seekBar = seekBar;
    }
    @Override
    public void run() {
        seekBar.setProgress(SingleMediaPlayer.getInstance().getCurrentPosition());
    }
}

package wikiradio.neslihan.tur.org.wikiradio.mediaplayer;

/**
 * Created by nesli on 24.12.2016.
 */

public class MediaPlayerState {
        public static final int STATE_IDLE = 1;
        public static final int STATE_INITIALIZED = 2;
        public static final int STATE_PREPARING = 3;
        public static final int STATE_PREPARED = 4;
        public static final int STATE_STARTED = 5;
        public static final int STATE_PAUSED = 6;
        public static final int STATE_RELEASED = -2;
        public static final int STATE_STOPPED = -3;
        public static final int STATE_ERROR = -4;
        public static final int STATE_PLAYBACK_COMPLETED = -5;

}

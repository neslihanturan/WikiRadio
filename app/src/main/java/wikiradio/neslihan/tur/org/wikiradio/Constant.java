package wikiradio.neslihan.tur.org.wikiradio;

import com.danikula.videocache.HttpProxyCacheServer;

import java.io.FileDescriptor;
import java.util.HashSet;

import wikiradio.neslihan.tur.org.wikiradio.model.AudioFile;
import wikiradio.neslihan.tur.org.wikiradio.model.TTSFile;

/**
 * Created by nesli on 15.12.2016.
 */

public class Constant {
    public static HashSet<String> categorySet = new HashSet<>();
    public static final String EMPTY_STRING = "";
    public static final String COMMONS_BASE_URL = "https://commons.wikimedia.org/";
    public static final String EN_WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/";
    public static final String MEDIA_WIKI_API = "MediaWikiAPI";
    public static final String REST_API = "RestAPI";
    public static final String AUDIO_STREAM = "AudioStream";
    public static int NOTIFICATION_ID=0;
    public static int MAX_CACHED_FILE=4;
    public static HttpProxyCacheServer proxy;
    public static AudioFile nowPlayingAudio;
    public static TTSFile nowPlayingFile;
    public static boolean isAudioPlaying = true; //false means a tts file is playing

    public interface SEEKBAR {
        public static int STOP_SEEKBAR = -1;
    }

    public interface ISPLAYING{
        public static String PLAYING = "state.playing";
        public static String PAUSED = "state.paused";
    }

    public interface ACTION {
        public static String MAIN_ACTION = "action.activity";
        public static String PLAY_ACTION = "action.play";
        public static String NEXT_ACTION = "action.next";
        public static String FORWARD_ACTION = "action.skip_forward";
        public static String REWIND_ACTION = "action.skip_backwards ";
    }
}

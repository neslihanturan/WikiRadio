package wikiradio.neslihan.tur.org.wikiradio;

/**
 * Created by nesli on 15.12.2016.
 */

public class Constant {
    public static final String COMMONS_BASE_URL = "https://commons.wikimedia.org/";
    public static final String EN_WIKIPEDIA_BASE_URL = "https://en.wikipedia.org/";
    public static final String MEDIA_WIKI_API = "MediaWikiAPI";
    public static final String REST_API = "RestAPI";
    public static final String AUDIO_STREAM = "AudioStream";
    public static int NOTIFICATION_ID=0;

    public interface SEEKBAR {
        public static int STOP_SEEKBAR = -1;
    }

    public interface ACTION {
        public static String MAIN_ACTION = "action.activity";
        public static String PLAY_ACTION = "action.play";
        public static String NEXT_ACTION = "action.next";
        public static String SKIP_FORWARD_ACTION = "action.skip_forward";
        public static String SKIP_BACKWARD_ACTION = "action.skip_backwards ";
    }
}

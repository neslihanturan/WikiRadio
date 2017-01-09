package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by nesli on 09.01.2017.
 */
public class App extends Application {

    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }
}

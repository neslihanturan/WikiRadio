package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by nesli on 06.01.2017.
 */

public class ProxyCacheServer {
    private final String LOG_TAG = ProxyCacheServer.class.getName();
    //private static final Logger LOG = LoggerFactory.getLogger("HttpProxyCacheServer");
    private static final String PROXY_HOST = "127.0.0.1";

    private final Object clientsLock = new Object();
    private final ExecutorService socketProcessor = Executors.newFixedThreadPool(8);
    //private final Map<String, HttpProxyCacheServerClients> clientsMap = new ConcurrentHashMap<>();
    private final ServerSocket serverSocket;
    private final int port;
    private final Thread waitConnectionThread;
    //private final Config config;
    //private final Pinger pinger;
    public ProxyCacheServer(Context context, ServerSocket serverSocket, int port, Thread waitConnectionThread) {
        this.serverSocket = serverSocket;
        this.port = port;
        //this(new Builder(context).buildConfig());
        this.waitConnectionThread = waitConnectionThread;
    }

    //private ProxyCacheServer(Config config) {
    public ProxyCacheServer() {
        //this.config = checkNotNull(config);
        try {
            InetAddress inetAddress = InetAddress.getByName(PROXY_HOST);
            this.serverSocket = new ServerSocket(0, 8, inetAddress);
            this.port = serverSocket.getLocalPort();
            CountDownLatch startSignal = new CountDownLatch(1);
            this.waitConnectionThread = new Thread(new WaitRequestsRunnable(startSignal));
            this.waitConnectionThread.start();
            startSignal.await(); // freeze thread, wait for server starts
            //this.pinger = new Pinger(PROXY_HOST, port);
            //LOG.info("Proxy cache server started. Is it alive? " + isAlive());
        } catch (IOException | InterruptedException e) {
            socketProcessor.shutdown();
            throw new IllegalStateException("Error starting local proxy server", e);
        }
    }
    private final class WaitRequestsRunnable implements Runnable {

        private final CountDownLatch startSignal;

        public WaitRequestsRunnable(CountDownLatch startSignal) {
            this.startSignal = startSignal;
        }

        @Override
        public void run() {
            startSignal.countDown();
            waitForRequest();
        }
    }
    private void waitForRequest() {
        try {
            Log.d(LOG_TAG,"waits for request");
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                Log.d(LOG_TAG,"Accept new socket");
                //LOG.debug("Accept new socket " + socket);
                Log.d(LOG_TAG,"Accept new socket " + socket);
                socketProcessor.submit(new SocketProcessorRunnable(socket));
            }
        } catch (IOException e) {
            //onError(new ProxyCacheException("Error during waiting connection", e));
        }
    }

    private final class SocketProcessorRunnable implements Runnable {

        private final Socket socket;

        public SocketProcessorRunnable(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            processSocket(socket);
        }
    }
    private void processSocket(Socket socket) {
        Log.d(LOG_TAG,"socket is processing");
    }



}

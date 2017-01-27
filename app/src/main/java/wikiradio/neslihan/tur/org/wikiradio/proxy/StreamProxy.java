package wikiradio.neslihan.tur.org.wikiradio.proxy;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;


public class StreamProxy implements Runnable{
    private static final String LOG_TAG = StreamProxy.class.getName();

    public int getPort() {
        return port;
    }

    private int port = 0;
    //public final static String LOCAL_ADDRESS = "127.0.0.1";
    public final static String LOCAL_ADDRESS = "192.168.0.127";
    //public static String LOCAL_ADDRESS = "10.0.3.15";
    //public final static String LOCAL_ADDRESS = "10.0.2.2";
    private ServerSocket socket;
    private Thread thread;
    private boolean isRunning;
    int key;

    public StreamProxy(Context context){
        try {
            //LOCAL_ADDRESS = getLocalIpAddress();
            socket = new ServerSocket(port, 0, InetAddress.getByAddress(new byte[] {127,0,0,1}));
            socket.setSoTimeout(10000);
            port = socket.getLocalPort();
            Log.d(LOG_TAG, "Stream Proxy object created, port " + port +", local adress "+socket.getInetAddress()+ " obtained");

        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start(Context context) {
        this.key = key;
        if (socket == null) {
            Log.d(LOG_TAG,"Cannot start proxy; it has not been initialized.");
            throw new IllegalStateException("Cannot start proxy; it has not been initialized.");
        }
        if(socket.isBound()){
            thread = new Thread(this);
            thread.start();
        }


    }

    @Override
    public void run() {
        Log.d(LOG_TAG, socket.toString());
        isRunning = true;

        while (isRunning) {
            Socket client = new Socket();
            Log.d(LOG_TAG, "running");
            try {
                Log.d(LOG_TAG, "in try");
                client = socket.accept();
                Log.d(LOG_TAG,"client inet address"+client.getInetAddress().toString());
                if (client == null) {
                    Log.d(LOG_TAG, "client is null");
                    continue;
                }
                Log.d(LOG_TAG, "client connected, get audio streams method is calling");

                //WmCommonsDataUtil.getAudioStreams(this, remoteUrl, client, cahceRefference, delegate2, key , bytePointer);

            } catch (Exception e) {
                if(client.isConnected()){
                    Log.d(LOG_TAG, "client connected");
                }
                e.printStackTrace();
                //e.initCause(e);

                Log.d(LOG_TAG, "socket timeout Exception"+e.getCause());
                // Do nothing
            }
        }
        Log.d(LOG_TAG, "Proxy interrupted. Shutting down.");
        Log.d(LOG_TAG, "Proxy interrupted. Shutting down.");
    }
    public String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        catch (SocketException ex)
        {
            Log.e(LOG_TAG, ex.toString());
        }
        return "";
    }
}
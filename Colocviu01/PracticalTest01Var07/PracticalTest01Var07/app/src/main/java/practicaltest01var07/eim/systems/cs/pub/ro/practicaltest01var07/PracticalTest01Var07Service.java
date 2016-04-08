package practicaltest01var07.eim.systems.cs.pub.ro.practicaltest01var07;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

class ProcessingThread extends Thread {

    private Context context;
    private String name = null;
    private String group = null;

    public ProcessingThread(Context context, String name, String group) {
        this.context = context;
        this.name = name;
        this.group = group;
    }

    public void run() {

        while(true) {

            Log.d("TEST", "In thread");

            Intent intent = new Intent();
            intent.setAction("userMessage");
            intent.putExtra("userName", this.name);
            context.sendBroadcast(intent);
            sleep();

            intent = new Intent();
            intent.setAction("groupMessage");
            intent.putExtra("groupName", this.group);
            context.sendBroadcast(intent);
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
    }


}

public class PracticalTest01Var07Service extends Service {
    public PracticalTest01Var07Service() {
        processingThread = null;
    }

    public void onCreate() {
        super.onCreate();
    }

    ProcessingThread processingThread;
    boolean flag = false;

    @Override
    public int onStartCommand(Intent intent,
                              int flags,
                              int startId) {

        if(flag == true) {
            Log.d("TEST", "Serviciul a instantiat deja un thread");
            return -1;
        }

        Log.d("TEST", "In start command pe serviciu");

        flag = true;
        if(intent != null && intent.getExtras() != null
                && intent.getExtras().containsKey("name") == false || intent.getExtras().containsKey("group") == false){
            Log.d("TEST", "In service nu am primit name sau group");
            return -1;
        }

        if(processingThread == null) {
            processingThread = new ProcessingThread(this, intent.getStringExtra("name"), intent.getStringExtra("group"));
            processingThread.start();
        }

        return 0;
    }

    @Override
    public IBinder onBind(Intent intent) {
      return null;
    }
}

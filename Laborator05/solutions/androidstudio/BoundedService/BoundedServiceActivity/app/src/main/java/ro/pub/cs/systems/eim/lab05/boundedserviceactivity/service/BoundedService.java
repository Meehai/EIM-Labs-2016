package ro.pub.cs.systems.eim.lab05.boundedserviceactivity.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

import ro.pub.cs.systems.eim.lab05.boundedserviceactivity.general.Constants;

public class BoundedService extends Service {

    final private IBinder boundedServiceBinder = new BoundedServiceBinder();
    final private Random random = new Random();

    public class BoundedServiceBinder extends Binder {
        public BoundedService getService() {
            return BoundedService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Constants.TAG, "onCreate() method from service was invoked");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.TAG, "onBind() method was invoked");
        return boundedServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(Constants.TAG, "onUnbind() method was invoked");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.TAG, "onDestroy() method from service was invoked");
        super.onDestroy();
    }

    public String getMessage() {
        return Constants.MESSAGES.get(random.nextInt(Constants.MESSAGES.size()));
    }
}

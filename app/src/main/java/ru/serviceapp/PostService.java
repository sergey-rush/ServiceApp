package ru.serviceapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import ru.customservice.IPostService;
import ru.customservice.Post;

/**
 * Created by sergey-rush on 14.04.2018.
 */
public class PostService extends Service {

    private void log(String message) {
        Log.v("PostService", message);
    }
    private WebContext webContext;
    private Context context;
    private Scheduler scheduler;

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("Received start command.");
        webContext = WebContext.getInstance();
        context = getApplicationContext();
        scheduler = Scheduler.getInstance(context);
        scheduler.startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        scheduler.stopTimer();
        Toast.makeText(getApplicationContext(), "PostService onDestroy", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }


    @Override
    public IBinder onBind(Intent intent) {
        log("Received binding.");
        Toast.makeText(getApplicationContext(), "Received binding", Toast.LENGTH_SHORT).show();
        return mBinder;
    }

    private final IPostService.Stub mBinder = new IPostService.Stub() {

        @Override
        public Post getLastPost() throws RemoteException {
            log("Received getLastPost command");
            return webContext.getPost();
        }

        @Override
        public void exit() throws RemoteException {
            log("Received exit command.");
            stopSelf();
        }
    };
}


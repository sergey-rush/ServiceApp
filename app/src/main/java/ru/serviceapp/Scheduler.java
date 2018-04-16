package ru.serviceapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ru.customservice.Post;

/**
 * Created by sergey-rush on 14.04.2018.
 */
public class Scheduler {
    private WebContext webContext;
    private Context context;
    private static Random random = new Random();
    private static Timer timer;
    private final Handler handler = new Handler();
    private static Scheduler current;

    public static Scheduler getInstance(Context context) {
        if (current == null) {
            current = new Scheduler(context);
        }
        return current;
    }

    private Scheduler(Context context) {
        this.context = context;
        webContext = WebContext.getInstance();
    }

    private final Runnable runnable = new Runnable() {
        public void run() {
            startRequesting();
        }
    };

    public void startTimer() {
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, 1000, 10000);
        }
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void startRequesting() {
        int randomPostId = random.nextInt(100);
        PostAsyncTask postAsyncTask = new PostAsyncTask(randomPostId);
        postAsyncTask.execute();
    }

    private void loadDataCallback() {
        Post post = webContext.getPost();
        Intent intent = new Intent(context, PostActivity.class);
        intent.putExtra("title", post.getTitle());
        intent.putExtra("body", post.getBody());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "channel_id_01")
                //.setDefaults(Notification.DEFAULT_ALL)
                //.setWhen(System.currentTimeMillis())
                .setTicker("New Post")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(post.getTitle())
                .setContentText(post.getBody())
                //.setAutoCancel(true)
                //.setSound(sound)
                .setContentIntent(pi);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(0, builder.build());
    }

    private class PostAsyncTask extends AsyncTask<Void, Void, Void> {

        private int responseCode;
        private int postId;

        public PostAsyncTask(int postId) {
            this.postId = postId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            PostProvider postProvider = new PostProvider();
            responseCode = postProvider.getPost(postId);
            return null;
        }

        @Override
        protected void onPostExecute(Void output) {
            super.onPostExecute(output);
            if (responseCode == 200) {
                loadDataCallback();
            }
        }
    }
}
package ru.serviceapp;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatDelegate;
import android.widget.Toast;

import java.util.List;

/**
 * Created by sergey-rush on 14.04.2018.
 */

public class PostApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        context.startService(new Intent(context, PostService.class));
    }
}
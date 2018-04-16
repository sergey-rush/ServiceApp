package ru.serviceapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sergey-rush on 14.04.2018.
 */
public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String body = intent.getStringExtra("body");
        TextView tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        TextView tvBody = (TextView)findViewById(R.id.tvBody);
        tvBody.setText(body);
    }

    public void onClose(View view){
        finish();
    }
}

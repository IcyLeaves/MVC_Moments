package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;


public class MomentsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        Intent intent=getIntent();
        String text=intent.getStringExtra("username");
        //设置username

    }
}

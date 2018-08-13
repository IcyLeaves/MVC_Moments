package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.*;
import com.example.leaves.WcfMomentsApplication.BooleanGson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//        new AlertDialog.Builder(this)
//                .setTitle("标题")
//               .setMessage("简单的消息提示框")
//                .setPositiveButton("登录Activity", null)
//                .show();
public class LoginActivity extends Activity implements View.OnClickListener {
    final private DataAnalyze DA = new DataAnalyze();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                new Thread() {
                    @Override
                    public void run() {
                        EditText user = findViewById(R.id.et_username);
                        EditText pass = findViewById(R.id.et_password);
                        boolean isLoginIn = DA.loginIn(user.getText().toString(),pass.getText().toString());
                        if (isLoginIn) {
                            Intent intent = new Intent(LoginActivity.this, MomentsActivity.class);
                            intent.putExtra("username", user.getText().toString());
                            startActivity(intent);
                        }

                    }
                }.start();
                break;
        }
    }
}

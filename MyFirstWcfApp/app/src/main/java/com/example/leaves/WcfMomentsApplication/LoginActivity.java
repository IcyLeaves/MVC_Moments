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
                        String result = getDataByHttp();
                        Gson gson = new Gson();
                        BooleanGson data = gson.fromJson(result, BooleanGson.class);
                        if (data.isD()) {
                            Intent intent = new Intent(LoginActivity.this, MomentsActivity.class);
                            EditText et = findViewById(R.id.et_username);
                            intent.putExtra("username", et.getText().toString());
                            startActivity(intent);
                        }

                    }
                }.start();
                break;
        }
    }

    private String getDataByHttp() {
        EditText user = findViewById(R.id.et_username);
        EditText pass = findViewById(R.id.et_password);
        String url_path = "http://10.0.2.2:8010/Moments.svc/LoginIn?Email=" + user.getText().toString() + "&password=" + pass.getText().toString();
        try {
            //使用该地址创建一个 URL 对象
            URL url = new URL(url_path);
            //使用创建的URL对象的openConnection()方法创建一个HttpURLConnection对象
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            /**
             * 设置HttpURLConnection对象的参数
             */
            // 设置请求方法为 GET 请求
            httpURLConnection.setRequestMethod("GET");
            //使用输入流
            httpURLConnection.setDoInput(true);
            //GET 方式，不需要使用输出流
            httpURLConnection.setDoOutput(false);
            //连接
            httpURLConnection.connect();
            //还有很多参数设置 请自行查阅

            //连接后，创建一个输入流来读取response
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String response;
            //每次读取一行，若非空则添加至 stringBuilder
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //读取所有的数据后，赋值给 response
            response = stringBuilder.toString().trim();

            bufferedReader.close();
            httpURLConnection.disconnect();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

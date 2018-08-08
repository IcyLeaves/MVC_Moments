package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class MomentsActivity extends Activity implements View.OnClickListener {
    private LinearLayout ly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        Intent intent = getIntent();
        String text = intent.getStringExtra("username");
        //设置username
        TextView u = findViewById(R.id.tv_username);
        u.setText(text);
        //设置title
        TextView t = findViewById(R.id.tv_main_title);
        t.setText("朋友圈");
        //设置返回按钮点击事件
        findViewById(R.id.tv_back).setOnClickListener(this);
        //创建多个Notes
        ly = findViewById(R.id.moments_noteLayout);
        initNotes();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                new Thread() {
                    @Override
                    public void run() {

                    }
                }.start();
        }
    }

    private String getDataByHttp(String url_path) {
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

    private void initNotes() {
        new Thread() {
            @Override
            public void run() {
                String allNotes = getDataByHttp("http://10.0.2.2:8010/Moments.svc/OrderAllNotesByDescending");
                Gson gson = new Gson();
                NotesGson data = gson.fromJson(allNotes, NotesGson.class);
                List<NotesGson.DBean> notes = data.getD();
                for (int i = 0; i < data.getD().size(); i++) {
                    NotesGson.DBean note = notes.get(i);
                    String currentUser = getDataByHttp("http://10.0.2.2:8010/Moments.svc/GetUserById?userId=" + note.getUserId());
                    String currentLikes = getDataByHttp("http://10.0.2.2:8010/Moments.svc/GetLikesOnNote?noteId=" + note.getNoteId());
                    UsersGson userdata = gson.fromJson(currentUser, UsersGson.class);
                    StringGson likes = gson.fromJson(currentLikes, StringGson.class);
                    //用户名：xxx
                    addTextView("用户名：" + userdata.getD().getNickname());
                    //“帖子内容”
                    addTextView(note.getText());
                    //时间：（靠右）
                    addTextViewAlignRight(note.getTime());
                    //点赞者：(靠右)
                    addTextViewAlignRight("点赞者：" + likes.getD());
                    //加根线
                    addLine();
                }
            }
        }.start();

    }

    private void addLine() {
        final ImageView line = new ImageView(this);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10000, 1);
        line.setBackgroundColor(Color.GRAY);
        params.setMargins(0, 5, 0, 5);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(line, params);
            }
        });
    }

    private void addTextView(String text) {
        final TextView tempTv = new TextView(this);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 0, 30, 0);
        tempTv.setText(text);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(tempTv,params);
            }
        });
    }

    private void addTextViewAlignRight(String text) {
        final TextView tempTv = new TextView(this);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 0, 30, 0);
        params.gravity=Gravity.END;
        tempTv.setText(text);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(tempTv,params);
            }
        });
    }

}

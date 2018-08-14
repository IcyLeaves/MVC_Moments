package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CommentActivity extends Activity implements View.OnClickListener {
    final private DataAnalyze DA = new DataAnalyze();
    private String loginUsername;
    private int scrollLocation;
    private int commentTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent=getIntent();
        loginUsername=intent.getStringExtra("username");
        scrollLocation=intent.getIntExtra("scrollLocation",-1);
        commentTo=Integer.parseInt(intent.getStringExtra("noteId"));
        initUI();
        findViewById(R.id.tv_comment_submit).setOnClickListener(this);
        findViewById(R.id.tv_comment_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_reply_back:
                Intent intent=new Intent(CommentActivity.this,MomentsActivity.class);
                intent.putExtra("username",loginUsername);
                intent.putExtra("scrollLocation",scrollLocation);
                startActivity(intent);
                break;
            case R.id.tv_reply_submit:
                SubmitNewComment();
                Intent intent1=new Intent(CommentActivity.this,MomentsActivity.class);
                intent1.putExtra("username",loginUsername);
                intent1.putExtra("scrollLocation",scrollLocation);
                startActivity(intent1);
                break;
        }
    }

    private void SubmitNewComment()
    {
        new Thread(){
            @Override
            public void run()
            {
                EditText et=findViewById(R.id.et_comment);
                DA.createNewComment(et.getText().toString(),loginUsername,commentTo);
            }
        }.start();
    }

    private void initUI()
    {
        new Thread(){
            @Override
            public void run()
            {
                final String forwardContent = DA.getCurrentNote(commentTo).getText();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv_origin = findViewById(R.id.tv_comment_origin);
                        String text = tv_origin.getText().toString() + forwardContent;
                        tv_origin.setText(text);
                    }
                });
            }
        }.start();
    }

}

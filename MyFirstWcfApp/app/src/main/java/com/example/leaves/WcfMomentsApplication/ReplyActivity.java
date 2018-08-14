package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ReplyActivity extends Activity implements View.OnClickListener {
    final private DataAnalyze DA = new DataAnalyze();
    private String loginUsername;
    private int scrollLocation;
    private int replyTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);
        Intent intent=getIntent();
        loginUsername=intent.getStringExtra("username");
        scrollLocation=intent.getIntExtra("scrollLocation",-1);
        replyTo=Integer.parseInt(intent.getStringExtra("commentId"));
        initUI();
        findViewById(R.id.tv_reply_submit).setOnClickListener(this);
        findViewById(R.id.tv_reply_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_reply_back:
                Intent intent=new Intent(ReplyActivity.this,MomentsActivity.class);
                intent.putExtra("username",loginUsername);
                intent.putExtra("scrollLocation",scrollLocation);
                startActivity(intent);
                break;
            case R.id.tv_reply_submit:
                SubmitNewComment();
                Intent intent1=new Intent(ReplyActivity.this,MomentsActivity.class);
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
                CommentGson.DBean currentComment=DA.getCurrentComment(replyTo);
                int underId,followId;
                if(currentComment.getUnderCommentId()!=null)
                {
                    underId=(int)currentComment.getUnderCommentId();
                    followId=(int)currentComment.getFollowCommentId();
                }
                else{
                    underId=followId=currentComment.getCommentId();
                }
                EditText et=findViewById(R.id.et_reply);
                DA.createNewChildComment(et.getText().toString(),loginUsername,replyTo,underId,followId);
            }
        }.start();
    }

    private void initUI()
    {

        new Thread(){
            @Override
            public void run()
            {
                final String replyContent = DA.getCurrentComment(replyTo).getText();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv_origin = findViewById(R.id.tv_reply_origin);
                        String text = tv_origin.getText().toString() + replyContent;
                        tv_origin.setText(text);
                    }
                });
            }
        }.start();
    }
}

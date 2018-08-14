package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ForwardActivity extends Activity implements View.OnClickListener {
    final private DataAnalyze DA = new DataAnalyze();
    private String loginUsername;
    private int scrollLocation;
    private int replyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forward);
        Intent intent=getIntent();
        loginUsername=intent.getStringExtra("username");
        scrollLocation=intent.getIntExtra("scrollLocation",-1);
        int noteId=Integer.parseInt(intent.getStringExtra("noteId"));
        initUI(noteId);
        findViewById(R.id.tv_forward_submit).setOnClickListener(this);
        findViewById(R.id.tv_forward_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.tv_forward_back:
                Intent intent=new Intent(ForwardActivity.this,MomentsActivity.class);
                intent.putExtra("username",loginUsername);
                intent.putExtra("scrollLocation",scrollLocation);
                startActivity(intent);
                break;
            case R.id.tv_forward_submit:
                SubmitNewNote();
                Intent intent1=new Intent(ForwardActivity.this,MomentsActivity.class);
                intent1.putExtra("username",loginUsername);
                intent1.putExtra("scrollLocation",scrollLocation);
                startActivity(intent1);
                break;
        }
    }

    private void SubmitNewNote()
    {
        new Thread(){
            @Override
            public void run()
            {
                EditText et=findViewById(R.id.et_forward);
                DA.createForwardNote(et.getText().toString(),loginUsername,replyId);
            }
        }.start();
    }

    private void initUI(final int noteId)
    {
        new Thread(){
            @Override
            public void run()
            {
                NoteGson.DBean currentNote=DA.getCurrentNote(noteId);
                NoteGson.DBean forwardNote;
                if(currentNote.getForward()!=null)
                {
                    forwardNote=DA.getForwardedNote(noteId);
                    replyId=forwardNote.getNoteId();
                }
                else
                {
                    forwardNote=currentNote;
                    replyId=currentNote.getNoteId();
                }
                final String forwardContent = forwardNote.getText();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv_origin = findViewById(R.id.tv_forward_origin);
                        String text = tv_origin.getText().toString() + forwardContent;
                        tv_origin.setText(text);
                    }
                });
            }
        }.start();
    }

}

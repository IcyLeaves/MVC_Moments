package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CreateActivity extends Activity implements View.OnClickListener {
    final private DataAnalyze DA = new DataAnalyze();
    private String loginUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        loginUsername=getIntent().getStringExtra("username");
        findViewById(R.id.tv_create_back).setOnClickListener(this);
        findViewById(R.id.tv_create_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_create_back:
                Intent intent = new Intent(CreateActivity.this, MomentsActivity.class);
                intent.putExtra("username",loginUsername);
                startActivity(intent);
                break;
            case R.id.tv_create_submit:
                SubmitNewNote();
                Intent intent1 = new Intent(CreateActivity.this, MomentsActivity.class);
                intent1.putExtra("username",loginUsername);
                startActivity(intent1);
                break;
        }
    }

    private void SubmitNewNote() {
        new Thread(){
            @Override
            public void run()
            {
                EditText et=findViewById(R.id.et_create);
                DA.createNewNote(et.getText().toString(),loginUsername);
            }
        }.start();
    }
}

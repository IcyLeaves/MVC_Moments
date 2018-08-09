package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class LikeTag {
    int noteId;
    boolean isLiked;

    LikeTag(int i, boolean b) {
        noteId = i;
        isLiked = b;
    }


}

public class MomentsActivity extends Activity {
    private LinearLayout ly;
    private UsersGson.DBean loginUser;
    final private String urlPrefix = "http://10.0.2.2:8010/Moments.svc/";
    final private DataAnalyze DA = new DataAnalyze();
    final private int textView_margin = 30;
    final private float textView_textSize = 16;
    private ArrayList<TextView> tv_LikesBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moments);
        tv_LikesBoxes.clear();
        Intent intent = getIntent();
        String text = intent.getStringExtra("username");
        getLoginUser(text);

        //设置title
        TextView t = findViewById(R.id.tv_main_title);
        t.setText("朋友圈");
        //设置返回按钮点击事件
        //findViewById(R.id.tv_back).setOnClickListener(this);
        //创建多个Notes
        ly = findViewById(R.id.moments_noteLayout);
        initNotes();
    }


    //点赞按钮点击事件
    class MyLikeListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            final LikeTag tag = (LikeTag) v.getTag();
            if (tag.isLiked) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setTag(new LikeTag(tag.noteId, false));
                        v.setBackgroundResource(R.drawable.ic_operation_dislike);
                    }
                });
                DA.subLikes(tag.noteId, loginUser.getEmail());

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setTag(new LikeTag(tag.noteId, true));
                        v.setBackgroundResource(R.drawable.ic_operation_like);
                    }
                });
                DA.addLikes(tag.noteId, loginUser.getEmail());
            }
            final String likes = DA.getLikesOnNote(tag.noteId);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv_temp = null;
                    //查找对应的“点赞数”TextView
                    for (int i = 0; i < tv_LikesBoxes.size(); i++) {
                        if ((int) tv_LikesBoxes.get(i).getTag() == tag.noteId) {
                            tv_temp = tv_LikesBoxes.get(i);
                            break;
                        }
                    }
                    assert tv_temp != null;
                    tv_temp.setText(String.format(getResources().getString(R.string.moments_likes_format), likes));
                }
            });
        }
    }

//    @Override
//    public void onClick(View view) {
//        int id=(int)view.getTag();
//        String oldLikes=tv_LikesBoxes.get(id).getText().toString();
//
//    }

    private void initNotes() {
        new Thread() {
            @Override
            public void run() {
                List<NotesGson.DBean> notes = DA.orderAllNotesByDescending();
                for (int i = 0; i < notes.size(); i++) {
                    NotesGson.DBean note = notes.get(i);
                    UsersGson.DBean userdata = DA.getUserById(note.getUserId());
                    String likes = DA.getLikesOnNote(note.getNoteId());
                    boolean isLiked = DA.isCurrentUserLikesNote(note.getNoteId(), loginUser.getEmail());
                    //用户名：xxx
                    addTextView_Username("用户名：" + userdata.getNickname());
                    //“帖子内容”
                    addTextView_Text(note.getText());
                    //时间：（靠右）
                    addTextView_Time(note.getTime());
                    //点赞者：(靠右+id)
                    addTextView_Likes("点赞者：" + likes, note.getNoteId());
                    //从左到右依次是 点赞 转发 评论
                    addOperations(isLiked, note.getNoteId());
                    //加根线
                    addLine();
                }
            }
        }.start();

    }

    private void getLoginUser(final String username) {
        new Thread() {
            @Override
            public void run() {
                loginUser = DA.getUserByEmail(username);
                //设置username
                TextView u = findViewById(R.id.tv_username);
                u.setText(loginUser.getNickname());
            }
        }.start();
    }

    private void addLine() {
        final ImageView line = new ImageView(this);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10000, 1);
        line.setBackgroundColor(Color.GRAY);
        params.setMargins(0, 15, 0, 15);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(line, params);
            }
        });
    }

    private void addTextView_Text(String text) {
        final TextView tempTv = new TextView(this);
        tempTv.setTextSize(textView_textSize);
        tempTv.setText(text);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(textView_margin, 0, textView_margin, 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(tempTv, params);
            }
        });
    }

    private void addTextView_Time(String text) {
        final TextView tempTv = new TextView(this);
        tempTv.setTextSize(textView_textSize);
        tempTv.setText(text);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(textView_margin, 0, textView_margin, 0);
        params.gravity = Gravity.END;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(tempTv, params);
            }
        });
    }

    private void addTextView_Likes(String text, int id) {
        final TextView tempTv = new TextView(this);
        tempTv.setTextSize(textView_textSize);
        tempTv.setText(text);
        tempTv.setTag(id);
        tv_LikesBoxes.add(tempTv);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(textView_margin, 0, textView_margin, 0);
        params.gravity = Gravity.END;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(tempTv, params);
            }
        });
    }

    private void addTextView_Username(String text) {
        final TextView tempTv = new TextView(this);
        tempTv.setText(text);
        tempTv.setTextSize(textView_textSize);
        TextPaint paint = tempTv.getPaint();
        paint.setFakeBoldText(true);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(textView_margin, 0, textView_margin, 0);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(tempTv, params);
            }
        });
    }

    private void addOperations(boolean isLiked, int id) {
        final LinearLayout ly_operation = new LinearLayout(this);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ly_operation.setOrientation(LinearLayout.HORIZONTAL);
        params.setMargins(textView_margin, 10, textView_margin, 0);
        params.gravity = Gravity.END;

        final LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final ImageButton imgBtn_like = new ImageButton(this);
        final ImageButton imgBtn_forward = new ImageButton(this);
        final ImageButton imgBtn_comment = new ImageButton(this);

        if (isLiked) {
            imgBtn_like.setBackgroundResource(R.drawable.ic_operation_like);
            imgBtn_like.setTag(new LikeTag(id, true));
        } else {
            imgBtn_like.setBackgroundResource(R.drawable.ic_operation_dislike);
            imgBtn_like.setTag(new LikeTag(id, false));
        }
        imgBtn_like.setOnClickListener(new MyLikeListener());

        imgBtn_forward.setTag(id);
        imgBtn_forward.setBackgroundResource(R.drawable.ic_operation_forward);

        imgBtn_comment.setTag(id);
        imgBtn_comment.setBackgroundResource(R.drawable.ic_operation_comment);

        int icon_margin = 20;
        iconParams.setMarginEnd(icon_margin);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly_operation.addView(imgBtn_like, iconParams);
                ly_operation.addView(imgBtn_forward, iconParams);
                ly_operation.addView(imgBtn_comment, iconParams);
                ly.addView(ly_operation, params);
            }
        });

    }
}

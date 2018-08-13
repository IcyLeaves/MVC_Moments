package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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

public class MomentsActivity extends Activity implements View.OnClickListener {
    private LinearLayout ly;
    private UsersGson.DBean loginUser;
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
        //发表按钮点击事件
        findViewById(R.id.tv_moments_plus).setOnClickListener(this);
        //返回按钮点击事件
        findViewById(R.id.tv_moments_back).setOnClickListener(this);
        //创建多个Notes
        ly = findViewById(R.id.moments_noteLayout);
        initNotes();
    }


    //点赞按钮点击事件
    class MyLikeListener implements View.OnClickListener {
        @Override
        public void onClick(final View v) {
            final LikeTag tag = (LikeTag) v.getTag();
            new Thread() {
                @Override
                public void run() {
                    if (tag.isLiked) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                v.setTag(new LikeTag(tag.noteId, false));
                                v.setBackgroundResource(R.drawable.sl_moments_dislike);
                            }
                        });
                        DA.subLikes(tag.noteId, loginUser.getEmail());
                    } else

                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                v.setTag(new LikeTag(tag.noteId, true));
                                v.setBackgroundResource(R.drawable.sl_moments_like);
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
            }.start();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_moments_plus:
                Intent intent = new Intent(MomentsActivity.this, CreateActivity.class);
                intent.putExtra("username", loginUser.getEmail());
                startActivity(intent);
                break;
            case R.id.tv_moments_back:
                Intent intent1 = new Intent(MomentsActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;
            default:
                if (((String) view.getTag()).contains("forward")) {
                    String[] strings = ((String)view.getTag()).split("&");
                    Intent intent2 = new Intent(MomentsActivity.this, ForwardActivity.class);
                    intent2.putExtra("username", loginUser.getEmail());
                    intent2.putExtra("noteId",strings[1]);
                    startActivity(intent2);
                }
                break;
        }
    }

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
                    //“转发内容”
                    if (note.getForward() != null) {
                        NoteGson.DBean forward = DA.getForwardedNote(note.getNoteId());
                        UsersGson.DBean forwardUser = DA.getUserById(forward.getUserId());
                        addTextView_Forward(forward, forwardUser.getNickname());
                    }
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

    private void addTextView_Username(String text) {
        final TextView tempTv = new TextView(this);
        tempTv.setText(text);
        tempTv.setTextColor(Color.BLACK);
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

    private void addTextView_Text(String text) {
        final TextView tempTv = new TextView(this);
        tempTv.setTextSize(textView_textSize);
        tempTv.setTextColor(Color.BLACK);
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

    private void addTextView_Forward(NoteGson.DBean forward, String userNickname) {
        final TextView tempTv = new TextView(this);
        tempTv.setGravity(Gravity.TOP);
        tempTv.setBackgroundResource(R.color.colorLightGray);
        tempTv.setMinLines(1);
        tempTv.setTextSize(textView_textSize);
        tempTv.setTextColor(Color.BLACK);
        int tv_padding = 15;
        tempTv.setPadding(tv_padding, tv_padding, tv_padding, tv_padding);
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(userNickname + "：" + forward.getText());
//style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
//size  为0 即采用原始的正常的 size大小
        spanBuilder.setSpan(new TextAppearanceSpan(null, Typeface.BOLD, 0, null, null), 0, userNickname.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        tempTv.setText(spanBuilder);

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(textView_margin, 10, textView_margin, 0);

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
        tempTv.setTextColor(Color.BLACK);
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
        tempTv.setTextColor(Color.BLACK);
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
            imgBtn_like.setBackgroundResource(R.drawable.sl_moments_like);
            imgBtn_like.setTag(new LikeTag(id, true));
        } else {
            imgBtn_like.setBackgroundResource(R.drawable.sl_moments_dislike);
            imgBtn_like.setTag(new LikeTag(id, false));
        }
        imgBtn_like.setOnClickListener(new MyLikeListener());

        imgBtn_forward.setTag("forward&" + id);
        imgBtn_forward.setBackgroundResource(R.drawable.sl_moments_forward);
        imgBtn_forward.setOnClickListener(this);

        imgBtn_comment.setTag("comment&" + id);
        imgBtn_comment.setBackgroundResource(R.drawable.sl_moments_comment);

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

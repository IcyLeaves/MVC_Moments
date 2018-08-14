package com.example.leaves.WcfMomentsApplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
                    String[] strings = ((String) view.getTag()).split("&");
                    Intent intent2 = new Intent(MomentsActivity.this, ForwardActivity.class);
                    intent2.putExtra("username", loginUser.getEmail());
                    intent2.putExtra("noteId", strings[1]);
                    intent2.putExtra("scrollLocation",findViewById(R.id.moments_scroll).getScrollY());
                    startActivity(intent2);
                }
                if (((String) view.getTag()).contains("comment")) {
                    String[] strings = ((String) view.getTag()).split("&");
                    Intent intent2 = new Intent(MomentsActivity.this, CommentActivity.class);
                    intent2.putExtra("username", loginUser.getEmail());
                    intent2.putExtra("noteId", strings[1]);
                    intent2.putExtra("scrollLocation",findViewById(R.id.moments_scroll).getScrollY());
                    startActivity(intent2);
                }
                if(((String)view.getTag()).contains("reply")){
                    String[] strings = ((String) view.getTag()).split("&");
                    Intent intent2 = new Intent(MomentsActivity.this, ReplyActivity.class);
                    intent2.putExtra("username", loginUser.getEmail());
                    intent2.putExtra("commentId", strings[1]);
                    intent2.putExtra("scrollLocation",findViewById(R.id.moments_scroll).getScrollY());
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
                    //"评论内容"
                    List<CommentsGson.DBean> parentComments = DA.getCommentsOnNote(note.getNoteId());
                    if (parentComments.size() != 0) {
                        addComment(parentComments);
                    }
                    //加根线
                    addLine();
                }
                if(getIntent().getIntExtra("scrollLocation",-1)!=-1)
                {
                    setScroll(getIntent().getIntExtra("scrollLocation",-1));
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
        imgBtn_comment.setOnClickListener(this);

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

    private void addComment(final List<CommentsGson.DBean> pComments) {
        final LinearLayout ll = new LinearLayout(MomentsActivity.this);
        //ll.setBackgroundResource(R.color.colorLightGray);
        ll.setOrientation(LinearLayout.VERTICAL);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(textView_margin,0,textView_margin,0);
        for (int i = 0; i < pComments.size(); i++) {
            CommentsGson.DBean currentComment = pComments.get(i);
            final LinearLayout ll_parent = new LinearLayout(MomentsActivity.this);
            //ll_parent.setBackgroundResource(R.color.colorHintGray);
            ll_parent.setOrientation(LinearLayout.VERTICAL);
            final LinearLayout.LayoutParams params_parent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params_parent.setMargins(5, 0, 5, 0);
            final TextView tv_parent = new TextView(MomentsActivity.this);
            String parentNickname = DA.getUserById(currentComment.getUserId()).getNickname();
            String parentText = parentNickname + "：" + currentComment.getText();
            tv_parent.setText(parentText);
            tv_parent.setTextColor(Color.BLACK);
            tv_parent.setTextSize(15);
            tv_parent.setClickable(true);
            tv_parent.setFocusable(true);
            tv_parent.setBackgroundResource(R.drawable.sl_moments_follow);
            tv_parent.setTag("reply&"+currentComment.getCommentId());
            tv_parent.setOnClickListener(this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll_parent.addView(tv_parent, params_parent);
                }
            });
            List<CommentsGson.DBean> childComments = DA.getChildComments(currentComment.getCommentId());
            if (childComments.size() != 0) {
                final LinearLayout ll_child = new LinearLayout(MomentsActivity.this);
                //ll_child.setBackgroundResource(R.color.colorLightWhite);
                ll_child.setOrientation(LinearLayout.VERTICAL);
                final LinearLayout.LayoutParams params_child = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params_child.setMargins(50, 5, 5, 5);
                for (int j = 0; j < childComments.size(); j++) {
                    CommentsGson.DBean currentChild = childComments.get(j);

                    final TextView tv_child = new TextView(MomentsActivity.this);
                    String childNickname1 = DA.getUserById(currentChild.getUserId()).getNickname();
                    String childNickname2 = DA.getUserById(DA.getChildFollowComments(currentChild.getCommentId()).getUserId()).getNickname();
                    String childText = childNickname1 + " 回复 " + childNickname2 + "：" + currentChild.getText();
                    tv_child.setText(childText);
                    tv_child.setTextColor(Color.BLACK);
                    tv_child.setTextSize(15);
                    tv_child.setBackgroundResource(R.drawable.sl_moments_follow);
                    tv_child.setClickable(true);
                    tv_child.setFocusable(true);
                    tv_child.setTag("reply&"+currentChild.getCommentId());
                    tv_child.setOnClickListener(this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ll_child.addView(tv_child);
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ll_parent.addView(ll_child, params_child);
                    }
                });
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ll.addView(ll_parent, params_parent);
                }
            });
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ly.addView(ll, params);
            }
        });
    }

    private void setScroll(final int y)
    {
        final ScrollView sv=findViewById(R.id.moments_scroll);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0,y);
            }
        });
    }
}

package com.example.leaves.WcfMomentsApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NotesGson {

    private List<DBean> d;

    public List<DBean> getD() {
        return d;
    }

    public void setD(List<DBean> d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * __type : Notes:#MyWcfMoments
         * Forward : null
         * NoteId : 2
         * Text : 世界那么大，我想去看看。
         * Time : /Date(1532334197000+0800)/
         * UserId : 2
         */

        private Object Forward;
        private int NoteId;
        private String Text;
        private String Time;
        private int UserId;

        public Object getForward() {
            return Forward;
        }

        public void setForward(Object Forward) {
            this.Forward = Forward;
        }

        public int getNoteId() {
            return NoteId;
        }

        public void setNoteId(int NoteId) {
            this.NoteId = NoteId;
        }

        public String getText() {
            return Text;
        }

        public void setText(String Text) {
            this.Text = Text;
        }

        public String getTime() {
            try {
                SimpleDateFormat hms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                hms.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                Date tTime = new Date(Long.parseLong(Time.replace("/Date(", "").replace("+0800)/", ""), 10));//跑步用时
                String totalTime = hms.format(tTime);
                return totalTime;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }
    }
}

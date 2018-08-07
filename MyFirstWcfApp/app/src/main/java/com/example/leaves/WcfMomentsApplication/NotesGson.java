package com.example.leaves.WcfMomentsApplication;

import java.util.Date;
import java.util.List;

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

        public Date getTime() {
            return new Date(Integer.parseInt(Time.replace("/Date(", "").replace(")/", ""),10));
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

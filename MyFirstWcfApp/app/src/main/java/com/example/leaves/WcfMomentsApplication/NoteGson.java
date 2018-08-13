package com.example.leaves.WcfMomentsApplication;

public class NoteGson {

    /**
     * d : {"__type":"Notes:#MyWcfMoments","Forward":null,"NoteId":7,"Text":"一条记录","Time":"/Date(1532418562000+0800)/","UserId":2}
     */

    private DBean d;

    public DBean getD() {
        return d;
    }

    public void setD(DBean d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * __type : Notes:#MyWcfMoments
         * Forward : null
         * NoteId : 7
         * Text : 一条记录
         * Time : /Date(1532418562000+0800)/
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
            return Time;
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

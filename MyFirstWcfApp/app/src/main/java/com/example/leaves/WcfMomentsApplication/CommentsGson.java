package com.example.leaves.WcfMomentsApplication;

import java.util.List;

public class CommentsGson {

    private List<DBean> d;

    public List<DBean> getD() {
        return d;
    }

    public void setD(List<DBean> d) {
        this.d = d;
    }

    public static class DBean {
        /**
         * __type : Comments:#MyWcfMoments
         * CommentId : 1
         * FollowCommentId : null
         * NoteId : 14
         * Text : 失败了
         * Time : /Date(1533114405000+0800)/
         * UnderCommentId : null
         * UserId : 1
         */

        private int CommentId;
        private Object FollowCommentId;
        private int NoteId;
        private String Text;
        private String Time;
        private Object UnderCommentId;
        private int UserId;

        public int getCommentId() {
            return CommentId;
        }

        public void setCommentId(int CommentId) {
            this.CommentId = CommentId;
        }

        public Object getFollowCommentId() {
            return FollowCommentId;
        }

        public void setFollowCommentId(Object FollowCommentId) {
            this.FollowCommentId = FollowCommentId;
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

        public Object getUnderCommentId() {
            return UnderCommentId;
        }

        public void setUnderCommentId(Object UnderCommentId) {
            this.UnderCommentId = UnderCommentId;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }
    }
}

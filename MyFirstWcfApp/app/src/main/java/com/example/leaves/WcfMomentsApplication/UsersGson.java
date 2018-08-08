package com.example.leaves.WcfMomentsApplication;

public class UsersGson {

    /**
     * d : {"__type":"Users:#MyWcfMoments","Email":"534104198@qq.com","Nickname":"叶子","Password":"b","UserId":2}
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
         * __type : Users:#MyWcfMoments
         * Email : 534104198@qq.com
         * Nickname : 叶子
         * Password : b
         * UserId : 2
         */

        private String Email;
        private String Nickname;
        private String Password;
        private int UserId;

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getNickname() {
            return Nickname;
        }

        public void setNickname(String Nickname) {
            this.Nickname = Nickname;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String Password) {
            this.Password = Password;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int UserId) {
            this.UserId = UserId;
        }
    }
}

package com.example.leaves.WcfMomentsApplication;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class DataAnalyze {
    final private String urlPrefix = "http://10.0.2.2:8010/Moments.svc/";
    private Gson gson = new Gson();

    private String getDataByHttp(String url_path) {
        try {
            //使用该地址创建一个 URL 对象
            URL url = new URL(url_path);
            //使用创建的URL对象的openConnection()方法创建一个HttpURLConnection对象
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            /*
             * 设置HttpURLConnection对象的参数
             */
            // 设置请求方法为 GET 请求
            httpURLConnection.setRequestMethod("GET");
            //使用输入流
            httpURLConnection.setDoInput(true);
            //GET 方式，不需要使用输出流
            httpURLConnection.setDoOutput(false);
            //连接
            httpURLConnection.connect();
            //还有很多参数设置 请自行查阅

            //连接后，创建一个输入流来读取response
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            String response;
            //每次读取一行，若非空则添加至 stringBuilder
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            //读取所有的数据后，赋值给 response
            response = stringBuilder.toString().trim();

            bufferedReader.close();
            httpURLConnection.disconnect();
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    NoteGson.DBean getCurrentNote(int noteId)
    {
        String theNote=getDataByHttp(urlPrefix+"GetCurrentNote?noteId="+noteId);
        NoteGson data=gson.fromJson(theNote,NoteGson.class);
        return data.getD();
    }

    boolean loginIn(String Email,String password)
    {
        String theBool=getDataByHttp(urlPrefix+"LoginIn?Email="+Email+"&password="+password);
        BooleanGson data=gson.fromJson(theBool,BooleanGson.class);
        return data.isD();
    }

    UsersGson.DBean getUserByEmail(String username) {
        String theUser = getDataByHttp(urlPrefix + "GetUserByEmail?username=" + username);
        UsersGson data = gson.fromJson(theUser, UsersGson.class);
        return data.getD();
    }

    List<NotesGson.DBean> orderAllNotesByDescending() {
        String allNotes = getDataByHttp(urlPrefix + "OrderAllNotesByDescending");
        NotesGson data = gson.fromJson(allNotes, NotesGson.class);
        return data.getD();
    }

    UsersGson.DBean getUserById(int noteId) {
        String currentUser = getDataByHttp(urlPrefix + "GetUserById?userId=" + noteId);
        UsersGson data = gson.fromJson(currentUser, UsersGson.class);
        return data.getD();
    }

    String getLikesOnNote(int noteId) {
        String currentLikes = getDataByHttp(urlPrefix + "GetLikesOnNote?noteId=" + noteId);
        StringGson data = gson.fromJson(currentLikes, StringGson.class);
        return data.getD();
    }

    boolean isCurrentUserLikesNote(int noteId, String username) {
        String currentLiked = getDataByHttp(urlPrefix + "IsCurrentUserLikesNote?noteId=" + noteId + "&username=" + username);
        BooleanGson data = gson.fromJson(currentLiked, BooleanGson.class);
        return data.isD();
    }

    void subLikes(int noteId, String username) {
        getDataByHttp(urlPrefix + "SubLikes?noteId=" + noteId + "&username=" + username);
    }

    void addLikes(int noteId, String username) {
        getDataByHttp(urlPrefix + "AddLikes?noteId=" + noteId + "&username=" + username);
    }

    void createNewNote(String text,String username)
    {
        getDataByHttp(urlPrefix+"CreateNewNote?text="+text+"&username="+username);
    }

    NoteGson.DBean getForwardedNote(int noteId)
    {
        String note=getDataByHttp(urlPrefix+"GetForwardedNote?noteId="+noteId);
        NoteGson data=gson.fromJson(note,NoteGson.class);
        return data.getD();
    }

    void createForwardNote(String text,String username,int forwardId)
    {
        getDataByHttp(urlPrefix+"CreateForwardNote?text="+text+"&username="+username+"&forwardId="+forwardId);
    }

    void createNewComment(String text,String username,int noteId)
    {
        getDataByHttp(urlPrefix+"CreateNewComment?text="+text+"&username="+username+"&noteId="+noteId);
    }

    CommentGson.DBean getCurrentComment(int commentId)
    {
        String theComment=getDataByHttp(urlPrefix+"GetCurrentComment?commentId="+commentId);
        CommentGson data=gson.fromJson(theComment,CommentGson.class);
        return data.getD();
    }

    List<CommentsGson.DBean> getCommentsOnNote(int noteId)
    {
        String theComments=getDataByHttp(urlPrefix+"GetCommentsOnNote?noteId="+noteId);
        CommentsGson data=gson.fromJson(theComments,CommentsGson.class);
        return data.getD();
    }

    List<CommentsGson.DBean> getChildComments(int parentId)
    {
        String theChild=getDataByHttp(urlPrefix+"GetChildComments?parentId="+parentId);
        CommentsGson data=gson.fromJson(theChild,CommentsGson.class);
        return data.getD();
    }

    CommentGson.DBean getChildFollowComments(int childCommentId){
        String theFollow=getDataByHttp(urlPrefix+"GetChildFollowComments?childCommentId="+childCommentId);
        CommentGson data=gson.fromJson(theFollow,CommentGson.class);
        return data.getD();
    }

    void createNewChildComment(String text,String username,int noteId,int parentId,int followId){
        getDataByHttp(urlPrefix+"CreateNewChildComment?text="+text+"&username="+username+"&noteId="+noteId+"&parentId="+parentId+"&followId="+followId);
    }
}

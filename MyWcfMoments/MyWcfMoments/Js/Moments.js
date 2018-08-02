var svcURL = "http://localhost:60067/MomentsService.svc/";
$(document).ready(function () {
    //yyyy-MM-dd HH:mm:SS
    function getDateTime(date) {
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hh = date.getHours();
        var mm = date.getMinutes();
        var ss = date.getSeconds();
        if (hh < 10) {
            hh = "0" + hh;
        }
        if (mm < 10) {
            mm = "0" + mm;
        }
        if (ss < 10) {
            ss = "0" + ss;
        }
        return year + "-" + month + "-" + day + " " + hh + ":" + mm + ":" + ss;
    }
    //调用的是这个方法
    function ConvertJSONDateToJSDate(jsondate) {
        var date = new Date(parseInt(jsondate.replace("/Date(", "").replace(")/", ""), 10));
        return date;
    }
    //将一个Note底下的评论作为一块Html输出，参数strComment是JSON数组字符串
    function AddCommentHtml(id, Comments) {
        var Div = $('#' + id + '-comments')[0];
        Div.innerHTML += "<hr style='height:1px;border:none;border-top:1px solid #222222;' />";
        for (var i = 0; i < Comments.length; i++) {
            AddParentHtml(id, Comments[i]);
        }

    }
    //专门添加父评论
    function AddParentHtml(id, Comment) {
        var Commenter;//评论者
        var Div = $('#' + id + '-comments')[0];
        $.ajax({
            url: svcURL + "GetUserById",
            datatype: "json",
            method: "GET",
            async: false,
            data: "userId=" + Comment.UserId,
            success: function (data) {
                Commenter = data.d;
            },
            error: function () { }
        });//Commenter=GetUserById(userId Comment.UserId)
        Div.innerHTML += "<p><b>" + Commenter.Nickname + "：</b>" + Comment.Text + "<a href='javascript:void(0);' onclick='AnswerClick(" + Comment.CommentId + ")' style='margin:0px 0px 0px 6px'>回复</a></p>";
        $.ajax({
            url: svcURL + "GetChildComments",
            datatype: "json",
            method: "GET",
            async: false,
            data: "parentId=" + Comment.CommentId,
            success: function (data) {
                var ChildComments = data.d;
                if (ChildComments != null) {
                    Div.innerHTML += "<div id='" + id + "-comments-" + Comment.CommentId + "' style='margin:0px 0px 0px 20px'></div>";
                    for (var i = 0; i < ChildComments.length; i++) {
                        AddChildHtml(id, Comment.CommentId, ChildComments[i]);
                    }
                }
            },
            error: function () { }
        });//AddChildHtml?=GetChildComments(parentId Comment.CommentId)

    }
    //专门添加子评论
    function AddChildHtml(noteid, commentid, ChildComment) {
        var Commenter;//评论者
        var AnswerComment;//回复的评论
        var Answeror;//回复的评论的评论者
        var InnerDiv = $('#' + noteid + '-comments-' + commentid)[0];
        $.ajax({
            url: svcURL + "GetUserById",
            datatype: "json",
            method: "GET",
            async: false,
            data: "userId=" + ChildComment.UserId,
            success: function (data) {
                Commenter = data.d;
            },
            error: function () { }
        });//Commenter=GetUserById(userId ChildComment.UserId)
        $.ajax({
            url: svcURL + "GetChildFollowComments",
            datatype: "json",
            method: "GET",
            async: false,
            data: "childCommentId=" + ChildComment.CommentId,
            success: function (data) {
                AnswerComment = data.d;
            },
            error: function () { }
        });//AnswerComment=GetChildFollowComments(childCommentId ChildComment.CommentId)
        $.ajax({
            url: svcURL + "GetUserById",
            datatype: "json",
            method: "GET",
            async: false,
            data: "userId=" + AnswerComment.UserId,
            success: function (data) {
                Answeror = data.d;
            },
            error: function () { }
        });//Answeror=GetUserById(userId AnswerComment.UserId)

        InnerDiv.innerHTML += "<p><b>" + Commenter.Nickname + "</b> 回复 <b>" +Answeror.Nickname+ "</b>：" + ChildComment.Text + "<a href='javascript:void(0);' onclick='AnswerClick(" + ChildComment.CommentId + ")' style='margin:0px 0px 0px 6px'>回复</a></p>";
    }
    //将一个Note的信息作为一块Html输出，参数strNote是JSON字符串
    function AddNoteHtml(strNote) {
        var Note = JSON.parse(strNote);//Note的JSON对象
        var userid = Note.UserId;//User的id
        var noteid = Note.NoteId;//Note的id
        var User, Reply, likes;//User的JSON对象,转发Note的JSON对象,和拼接后的点赞者string
        var ContentDiv = $('#content')[0];//添加的html的位置
        $.ajax({
            url: svcURL + "GetUserById",
            datatype: "json",
            method: "GET",
            async: false,
            data: "userId=" + userid,
            success: function (data) {
                User = data.d;
            },
            error: function () { }
        });//User=GetUserById(userId userid)
        $.ajax({
            url: svcURL + "GetLikesOnNote",
            datatype: "json",
            method: "GET",
            async: false,
            data: "noteId=" + noteid,
            success: function (data) {
                likes = data.d;
            },
            error: function () { }
        });//likes=GetLikesOnNote(noteId noteid)
        $.ajax({
            url: svcURL + "GetForwardedNote",
            datatype: "json",
            method: "GET",
            async: false,
            data: "noteId=" + noteid,
            success: function (data) {
                Reply = data.d;
            },
            error: function () { }
        });//Reply=GetForwardedNote(noteId noteid)
        ContentDiv.innerHTML += "<p><b>用户名：" + User.Nickname + "</b></p>";
        ContentDiv.innerHTML += "<p>" + Note.Text + "</p>";
        if (Reply != null) {
            var ReplyUser;
            $.ajax({
                url: svcURL + "GetUserById",
                datatype: "json",
                method: "GET",
                async: false,
                data: "userId=" + Reply.UserId,
                success: function (data) {
                    ReplyUser = data.d;
                },
                error: function () { }
            });//ReplyUser=GetUserById(userId Reply.UserId)
            ContentDiv.innerHTML += "<div style='background-color:rgba(100, 100, 100, 0.15);padding:10px 5px;'><b>" + ReplyUser.Nickname + "</b>：" + Reply.Text + "</div>";
        }
        ContentDiv.innerHTML += "<p align='right'>" + getDateTime(ConvertJSONDateToJSDate(Note.Time)) + "</p>";
        ContentDiv.innerHTML += "<div id='" + noteid + "'><p align='right'>点赞者：" + likes + "</p></div>";
        if (sessionStorage.getItem("username") != null) {
            $.ajax({
                url: svcURL + "IsCurrentUserLikesNote",
                datatype: "json",
                method: "GET",
                async: false,
                data: "noteId=" + noteid + "&username=" + sessionStorage.getItem("username"),
                success: function (data) {
                    if (data.d == true) {
                        ContentDiv.innerHTML += "<a id='" + noteid + "-LikeLink' href='javascript:void(0);' onclick='UnLikedClick(" + noteid + ")'>取消点赞</a>";
                    }
                    else {
                        ContentDiv.innerHTML += "<a id='" + noteid + "-LikeLink' href='javascript:void(0);' onclick='LikedClick(" + noteid + ")'>点赞</a>";
                    }
                },
                error: function () { }
            });//点赞/取消点赞=IsCurrentUserLikesNote(noteId noteid,username sessionStorage[username])
        }
        ContentDiv.innerHTML += "<a href='javascript:void(0);' onclick='ReplyClick(" + noteid + ")' style='margin:0px 0px 0px 6px'>转发</a>";
        ContentDiv.innerHTML += "<a href='javascript:void(0);' onclick='CommentClick(" + noteid + ")' style='margin:0px 0px 0px 6px'>评论</a>";
        $.ajax({
            url: svcURL + "GetCommentsOnNote",
            datatype: "json",
            method: "GET",
            async: false,
            data: "noteId=" + noteid,
            success: function (data) {
                if (data.d.length != 0) {
                    //alert(JSON.stringify(data));
                    ContentDiv.innerHTML += "<div id='" + noteid + "-comments' style='margin:0px 20px'></div>";
                    AddCommentHtml(noteid, data.d);
                }
            },
            error: function () { }
        });//AddComments?=GetCommentsOnNote(noteId noteid)

        ContentDiv.innerHTML += "<hr style='height:1px;border:none;border-top:1px solid #555555;' />";
    }

    $('#user').html("您好，" + sessionStorage.getItem("username") + "!");
    var NotesArr = [];
    $.ajax({
        url: svcURL + "OrderAllNotesByDescending",
        datatype: "json",
        method: "GET",
        async: false,
        success: function (data) {
            //alert(JSON.stringify(data.d));
            var objs = data.d;
            for (var i = 0; i < objs.length; i++) {
                NotesArr[i] = objs[i];
                AddNoteHtml(JSON.stringify(NotesArr[i]));
            }
        },
        error: function () {
            alert("error");
        }
    });//OrderAllNotesByDescending()
});
//"点赞"按钮的点击事件
function LikedClick(id) {
    $.ajax({
        url: svcURL + "AddLikes",
        datatype: "json",
        method: "GET",
        //async: false,
        data: "noteId=" + id + "&username=" + sessionStorage.getItem("username"),
        success: function (data) {
            $('#' + id).html('<p align="right">点赞者：' + data.d + '</p>');
            $('#' + id + '-LikeLink').attr("onclick", "UnLikedClick(" + id + ")");
            $('#' + id + '-LikeLink').html("取消点赞");
        },
        error: function () { }
    });//点赞+=AddLikes(noteId noteid,username sessionStorage[username])
}
//"取消点赞"按钮的点击事件
function UnLikedClick(id) {
    $.ajax({
        url: svcURL + "SubLikes",
        datatype: "json",
        method: "GET",
        //async: false,
        data: "noteId=" + id + "&username=" + sessionStorage.getItem("username"),
        success: function (data) {
            $('#' + id).html('<p align="right">点赞者：' + data.d + '</p>');
            $('#' + id + '-LikeLink').attr("onclick", "LikedClick(" + id + ")");
            $('#' + id + '-LikeLink').html("点赞");
        },
        error: function () { }
    });//点赞-=SubLikes(noteId noteid,username sessionStorage[username])
}
//"转发"按钮的点击事件
function ReplyClick(id) {
    var Note;
    $.ajax({
        url: "http://localhost:60067/MomentsService.svc/GetCurrentNote",
        type: "GET",
        data: "noteId=" + id,
        dataType: "json",
        async: false,
        error: function () { alert("error") },
        success: function (data) {
            Note = data.d;
            //alert(JSON.stringify(Note));
        }
    });//Note=GetCurrentNote(noteId id)
    if (Note.Forward != null) {
        sessionStorage.setItem("replyid", Note.Forward);
    }
    else {
        sessionStorage.setItem("replyid", Note.NoteId);
    }
    window.location.href = "Reply.aspx";
}
//"评论"按钮的点击事件
function CommentClick(id) {
    sessionStorage.setItem("commentid", id);
    window.location.href = "Comment.aspx";
}
//"回复"按钮的点击事件
function AnswerClick(id) {
        sessionStorage.setItem("answerid", id);
    window.location.href = "Answer.aspx";
}

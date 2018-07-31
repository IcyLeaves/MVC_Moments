$(document).ready(function () {
    var svcURL = "http://localhost:60067/MomentsService.svc/";
    //yyyy-MM-dd HH:mm:SS
    function getDateTime(date) {
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hh = date.getHours();
        var mm = date.getMinutes();
        var ss = date.getSeconds();
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
    //将一个Note的信息作为一块Html输出，参数strNote是JSON字符串
    function AddNoteHtml(strNote) {
        var Note = JSON.parse(strNote);//Note的JSON对象
        var userid = Note.UserId;//User的id
        var noteid = Note.NoteId;//Note的id
        var User, Reply,likes;//User的JSON对象,转发Note的JSON对象,和拼接后的点赞者string
        $.ajax({
            url: svcURL + "GetUserById",
            datatype: "json",
            method: "GET",
            async: false,
            data:"userId="+userid,
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
                Reply=data.d;
            },
            error: function () { }
        });//Reply=GetForwardedNote(noteId noteid)
        $('#content')[0].innerHTML += "<p><b>用户名：" + User.Nickname + "</b></p>";
        $('#content')[0].innerHTML += "<p>" + Note.Text + "</p>";
        if (Reply != null)
        {
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
            $('#content')[0].innerHTML += "<div style='background-color:rgba(100, 100, 100, 0.15);padding:10px 5px;'><b>" + ReplyUser.Nickname + "</b>：" + Reply.Text + "</div>";
        }
        $('#content')[0].innerHTML += "<p align='right'>" + getDateTime(ConvertJSONDateToJSDate(Note.Time)) + "</p>";
        $('#content')[0].innerHTML+=  "<div id='"+noteid+"'><p align='right'>点赞者："+likes+"</p></div>";
        if(sessionStorage.getItem("username")!=null)
        {
            $.ajax({
                url: svcURL + "IsCurrentUserLikesNote",
                datatype: "json",
                method: "GET",
                async: false,
                data: "noteId=" + noteid + "&username=" + sessionStorage.getItem("username"),
                success: function (data) {
                    if(data.d==true)
                    {
                        $('#content')[0].innerHTML+="<a id='"+noteid+"-LikeLink<' href='javascript:void(0);' onclick='UnLikedClick("+noteid+",this)'>取消点赞</a>";
                    }
                    else
                    {
                        $('#content')[0].innerHTML += "<a id='" + noteid + "-LikeLink<' href='javascript:void(0);' onclick='LikedClick(" + noteid + ",this)'>点赞</a>";
                    }
                },
                error: function () { }
            });//点赞/取消点赞=IsCurrentUserLikesNote(noteId noteid,username sessionStorage[username])
        }
    }

    $('#user').html("您好，" + sessionStorage.getItem("username") + "!");
    var NotesArr = [];
    $.ajax({
        url: svcURL + "OrderAllNotesByDescending",
        datatype: "json",
        method: "GET",
        async:false,
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
    });

});
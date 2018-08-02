$(document).ready(function () {
    var FollowComment;
    $.ajax({
        url: "http://localhost:60067/MomentsService.svc/GetCurrentComment",
        type: "GET",
        data: "commentId=" + sessionStorage.getItem("answerid"),
        dataType: "json",
        async: false,
        error: function () { alert("error") },
        success: function (data) {
            FollowComment=data.d;
            sessionStorage.setItem("commentid", FollowComment.NoteId);
            $('#Origin').html(FollowComment.Text);
        }
    });//ChildComment=GetCurrentComment(commentId sessionStorage[answerid])
    $("#Submit").click(function (envent) {
        var textbox = $('#Text');
        var UnderId;
        if (FollowComment.UnderCommentId != null)
        {
            UnderId = FollowComment.UnderCommentId;
        }
        else
        {
            UnderId = FollowComment.CommentId;
        }
        $.ajax({
            url: "http://localhost:60067/MomentsService.svc/CreateNewChildComment",
            type: "GET",
            data: "text=" + textbox.val() + "&username=" + sessionStorage.getItem("username") + "&noteId=" + sessionStorage.getItem("commentid") + "&parentId=" + UnderId + "&followId=" + FollowComment.CommentId,
            dataType: "json",
            error: function () { alert("error") },
            success: function (data) {
                sessionStorage.removeItem("commentid");
                sessionStorage.removeItem("answerid");
                window.location.href = "Moments.aspx";
            }
        });//Comment+=CreateNewChildComment(text Text.text,username sessionStorage[username],noteId sessionStorage[commentid],parentId sessionStorage[answerid],followId FollowComment.CommentId)
    });
});
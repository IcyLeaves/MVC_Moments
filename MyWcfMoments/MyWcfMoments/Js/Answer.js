$(document).ready(function () {
    $.ajax({
        url: "http://localhost:60067/MomentsService.svc/GetCurrentComment",
        type: "GET",
        data: "commentId=" + sessionStorage.getItem("answerid"),
        dataType: "json",
        async: false,
        error: function () { alert("error") },
        success: function (data) {
            sessionStorage.setItem("commentid", data.d.NoteId);
            $('#Origin').html(data.d.Text);
        }
    });//Note=GetCurrentComment(commentId sessionStorage[answerid])
    $("#Submit").click(function (envent) {
        var textbox = $('#Text');
        $.ajax({
            url: "http://localhost:60067/MomentsService.svc/CreateNewChildComment",
            type: "GET",
            data: "text=" + textbox.val() + "&username=" + sessionStorage.getItem("username") + "&noteId=" + sessionStorage.getItem("commentid")+"&parentId="+sessionStorage.getItem("answerid"),
            dataType: "json",
            error: function () { alert("error") },
            success: function (data) {
                sessionStorage.removeItem("commentid");
                sessionStorage.removeItem("answerid");
                window.location.href = "Moments.aspx";
            }
        });//Comment+=CreateNewChildComment(text Text.text,username sessionStorage[username],noteId sessionStorage[commentid],parentId sessionStorage[answerid])
    });
});
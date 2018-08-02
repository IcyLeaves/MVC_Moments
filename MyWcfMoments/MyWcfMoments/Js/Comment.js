$(document).ready(function () {
    $.ajax({
        url: "http://localhost:60067/MomentsService.svc/GetCurrentNote",
        type: "GET",
        data: "noteId=" + sessionStorage.getItem("commentid"),
        dataType: "json",
        async: false,
        error: function () { alert("error") },
        success: function (data) {
            $('#Origin').html(data.d.Text);
        }
    });//Note=GetCurrentNote(noteId sessionStorage[commentid])
    $("#Submit").click(function (envent) {
        var textbox = $('#Text');
        $.ajax({
            url: "http://localhost:60067/MomentsService.svc/CreateNewComment",
            type: "GET",
            data: "text=" + textbox.val() + "&username=" + sessionStorage.getItem("username") + "&noteId=" + sessionStorage.getItem("commentid"),
            dataType: "json",
            error: function () { alert("error") },
            success: function (data) {
                sessionStorage.removeItem("commentid");
                window.location.href = "Moments.aspx";
            }
        });//Notes+=CreateNewNote(text Text.text,username sessionStorage[username],noteId sessionStorage[commentid])
    });
});
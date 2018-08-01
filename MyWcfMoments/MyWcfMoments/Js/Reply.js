$(document).ready(function () {
    $.ajax({
        url: "http://localhost:60067/MomentsService.svc/GetCurrentNote",
        type: "GET",
        data: "noteId=" + sessionStorage.getItem("replyid"),
        dataType: "json",
        async:false,
        error: function () { alert("error") },
        success: function (data) {
            $('#Origin').html(data.d.Text);
        }
    });//Note=GetCurrentNote(noteId sessionStorage[replyid])
    $("#Submit").click(function (envent) {
        var textbox = $('#Text');
        $.ajax({
            url: "http://localhost:60067/MomentsService.svc/CreateForwardNote",
            type: "GET",
            data: "text=" + textbox.val() + "&username=" + sessionStorage.getItem("username") + "&forwardId=" + sessionStorage.getItem("replyid"),
            dataType: "json",
            error: function () { alert("error") },
            success: function (data) {
                sessionStorage.removeItem("replyid");
                window.location.href = "Moments.aspx";
            }
        });//Notes+=CreateNewNote(text Text.text,username sessionStorage[username],forwardId sessionStorage[replyid])
    });
});
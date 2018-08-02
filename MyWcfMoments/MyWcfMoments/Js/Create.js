$(document).ready(function () {
    $("#Submit").click(function (envent) {
        var textbox = $('#Text');
        $.ajax({
            url: "http://localhost:60067/MomentsService.svc/CreateNewNote",
            type: "GET",
            data: "text="+textbox.val()+"&username="+sessionStorage.getItem("username"),
            dataType: "json",
            error: function () { alert("error") },
            success: function (data) {
                if (data.d == true) {
                    window.location.href = "Moments.aspx";
                }
            }
        });//Notes+=CreateNewNote(text Text.text,username sessionStorage[username])
    });
});
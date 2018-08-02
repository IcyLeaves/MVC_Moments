//$("#form1").ajaxForm(function (data) {
//        if (data == true)
//            window.location.href = "Moments.html";
//    }
//function login() {
//    var formdata = $("#form1").serialize();
//    $("p").text("1234");
//    //$("p").text($("#form1").serialize());
//    $.ajax({
//        //几个参数需要注意一下
//        type: "POST",//方法类型
//        dataType: "json",//预期服务器返回的数据类型
//        url: "http://localhost:60067/MomentsService.svc/LoginIn",//url
//        data: "Email=leavescy@126.com&Password=a",
//        success: function (ret) {
//            alert(2);
//            if (ret == 1) {
//                window.location.href = "Moments.aspx";
//            }
//        },
//        error: function () {
//            alert("异常！");
//        }
//    });
//}
$(document).ready(function () {
    $("#form1").submit(function (envent) {
        envent.preventDefault();
        var form = $(this);
        var user = document.getElementById("Email").value;
        $.ajax({
            url: "http://localhost:60067/MomentsService.svc/LoginIn",
            type: "GET",
            data: form.serialize(),
            dataType: "json",
            error: function () { alert("error")},
            success: function (data) {
                if (data.d == true) {
                    sessionStorage.setItem("username", user);
                    window.location.href = "Moments.aspx";
                }
            }

        });

    });
});
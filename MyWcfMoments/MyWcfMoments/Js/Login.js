//$("#form1").ajaxForm(function (data) {
//        if (data == true)
//            window.location.href = "Moments.html";
//    }
function login() {
    alert($('#form1').serialize());
    $.ajax({
        //几个参数需要注意一下
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "http://localhost:60067/MomentsService.svc/LoginIn",//url
        data: $('#form1').serialize(),
        success: function (ret) {
            alert(2);
            if (ret == 1) {
                window.location.href = "Moments.aspx";
            }
        },
        error: function () {
            alert("异常！");
        }
    });
}
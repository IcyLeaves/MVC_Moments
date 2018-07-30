<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Login.aspx.cs" Inherits="MyWcfMoments.Login" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>登录</title>
    <script src="Js/Login.js" type="text/javascript"></script>
</head>
<body>
    <form id="form1" runat="server" method="post">
        <div>
            邮箱：<input id="Email" type="text" /><br />
            密码：<input id="Password" type="text" /><br />
            <input id="complete" type="submit" value="提交" onclick="login()" />
        </div>
    </form>
</body>
</html>

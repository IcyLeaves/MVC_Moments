using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace MyWcfMoments
{
    public partial class Login : System.Web.UI.Page
    {
        private MomentsService ms = new MomentsService();
        protected void Page_Load(object sender, EventArgs e)
        {
        }
        private bool GetLoginMessage()
        {
            string n = Request.Form["username"];
            string p = Request.Form["password"];
            if(ms.LoginIn(n,p)==true)
            {
                Session["username"] = n;
                return true;
            }
            return false;
        }
    }
}
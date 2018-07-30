using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Activation;
using System.ServiceModel.Web;
using System.Text;

namespace MyWcfMoments
{
    [ServiceContract(Namespace = "")]
    [AspNetCompatibilityRequirements(RequirementsMode = AspNetCompatibilityRequirementsMode.Allowed)]
    public class MomentsService
    {
        // 要使用 HTTP GET，请添加 [WebGet] 特性。(默认 ResponseFormat 为 WebMessageFormat.Json)
        // 要创建返回 XML 的操作，
        //     请添加 [WebGet(ResponseFormat=WebMessageFormat.Xml)]，
        //     并在操作正文中包括以下行:
        //         WebOperationContext.Current.OutgoingResponse.ContentType = "text/xml";
        protected ICY_SqlEntities db = new ICY_SqlEntities();
        /// <summary>
        /// 登录校验
        /// </summary>
        /// <param name="Email"></param>
        /// <param name="Password"></param>
        /// <returns></returns>
        [OperationContract]
        public bool LoginIn(string Email, string Password)
        {
            Users user = db.Users.FirstOrDefault(u => u.Email == Email);
            if (user == null) return false;
            else if (user.Password != Password) return false;
            else return true;
        }
        /// <summary>
        /// 查询所有帖子
        /// </summary>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public IEnumerable<Notes> SelectAllNotes()
        {
            return db.Notes.ToList();
        }

        /// <summary>
        /// 对所有帖子按时间降序
        /// </summary>
        /// <returns></returns>
        [OperationContract]
        public IEnumerable<Notes> OrderAllNotesByDescending()
        {
            return db.Notes.OrderByDescending(i => i.Time).ToList();
        }

        /// <summary>
        /// 查询帖子
        /// </summary>
        /// <param name="noteId"></param>
        /// <returns></returns>
        [OperationContract]
        public Notes GetCurrentNote(int noteId)
        {
            return db.Notes.First(n => n.NoteId == noteId);
        }

        /// <summary>
        /// 查询当前帖子的发帖者昵称
        /// </summary>
        /// <param name="note">当前帖子的实体</param>
        /// <returns></returns>
        [OperationContract]
        public Users GetUserByNote(Notes note)
        {
            return db.Users.First(u => u.UserId == note.UserId);
        }

        /// <summary>
        /// 判断当前帖子是否为转发
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        [OperationContract]
        public bool IsForward(Notes note)
        {
            return note.Forward != null;
        }

        /// <summary>
        /// 查询当前帖子所转发的原帖
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        [OperationContract]
        public Notes GetForwardedNote(Notes note)
        {
            Notes n = db.Notes.FirstOrDefault(u => u.NoteId == note.Forward);
            if (n == null) throw new ArgumentNullException();
            else return n;
        }

        /// <summary>
        /// 查询为当前帖子点赞的用户们的昵称串（以','连接）
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        [OperationContract]
        public string GetLikesOnNote(Notes note)
        {
            IQueryable<Likes> likes = db.Likes.Where(l => l.NoteId == note.NoteId);
            var cnt = 0;
            var ret = "";
            foreach (var liker in likes)
            {
                if (cnt != 0) { ret += ","; }
                cnt++;
                ret += db.Users.First(u => u.UserId == liker.UserId).Nickname;
            }
            if (cnt == 0)
            {
                ret = "无";
            }
            return ret;
        }

        /// <summary>
        /// 判断当前用户是否为当前帖子点赞
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        /// <returns></returns>
        [OperationContract]
        public bool IsCurrentUserLikesNote(Notes note, string username)
        {
            Users user = db.Users.FirstOrDefault(u => u.Email == username);
            return db.Likes.FirstOrDefault(l => l.UserId == user.UserId && l.NoteId == note.NoteId) != null;
        }

        /// <summary>
        /// 增加帖子的一名未点赞用户
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        [OperationContract]
        public bool AddLikes(Notes note, string username)
        {
            Users user = db.Users.FirstOrDefault(u => u.Email == username);
            int noteId = note.NoteId;
            if (user == null) return false;
            db.Likes.Add(new Likes { NoteId = noteId, UserId = user.UserId });
            db.SaveChanges();
            return true;
        }

        /// <summary>
        /// 减少帖子的一名已点赞用户
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        [OperationContract]
        public bool SubLikes(Notes note, string username)
        {
            Users user = db.Users.FirstOrDefault(u => u.Email == username);
            int noteId = note.NoteId;
            if (user == null) return false;
            db.Likes.Remove(db.Likes.First(l => l.UserId == user.UserId && l.NoteId == noteId));
            db.SaveChanges();
            return true;
        }

        // 在此处添加更多操作并使用 [OperationContract] 标记它们
    }
}

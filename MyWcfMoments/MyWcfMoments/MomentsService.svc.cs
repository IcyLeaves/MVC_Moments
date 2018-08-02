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
        /// <summary>
        /// 登录校验
        /// </summary>
        /// <param name="Email"></param>
        /// <param name="Password"></param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public bool LoginIn(string Email, string Password)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users user = db.Users.FirstOrDefault(u => u.Email == Email);
                if (user == null) return false;
                else
                {
                    if (user.Password != Password) return false;
                    else return true;
                }
            }
        }
        /// <summary>
        /// 查询所有帖子
        /// </summary>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public IEnumerable<Notes> SelectAllNotes()
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Notes.ToList();
            }
        }

        /// <summary>
        /// 对所有帖子按时间降序
        /// </summary>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public IEnumerable<Notes> OrderAllNotesByDescending()
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Notes.OrderByDescending(i => i.Time).ToList();
            }
        }

        /// <summary>
        /// 查询帖子
        /// </summary>
        /// <param name="noteId"></param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public Notes GetCurrentNote(int noteId)
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Notes.FirstOrDefault(n => n.NoteId == noteId);
            }
        }

        [OperationContract]
        [WebGet]
        public Comments GetCurrentComment(int commentId)
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Comments.FirstOrDefault(n => n.CommentId == commentId);
            }
        }

        /// <summary>
        /// 查询当前帖子的发帖者昵称
        /// </summary>
        /// <param name="note">当前帖子的实体</param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public Users GetUserById(int userId)
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Users.FirstOrDefault(u => u.UserId == userId);
            }
        }

        [OperationContract]
        [WebGet]
        public Users GetUserByEmail(string username)
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Users.FirstOrDefault(u => u.Email == username);
            }
        }

        /// <summary>
        /// 判断当前帖子是否为转发
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public bool IsForward(int noteId)
        {
            using (var db = new ICY_SqlEntities())
            {
                return GetCurrentNote(noteId).Forward != null;
            }
        }

        /// <summary>
        /// 查询当前帖子所转发的原帖
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public Notes GetForwardedNote(int noteId)
        {
            using (var db = new ICY_SqlEntities())
            {
                Notes note = GetCurrentNote(noteId);
                Notes n = db.Notes.FirstOrDefault(u => u.NoteId == note.Forward);
                return n;
            } 
        }

        /// <summary>
        /// 查询为当前帖子点赞的用户们的昵称串（以','连接）
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public string GetLikesOnNote(int noteId)
        {
            using (var db = new ICY_SqlEntities())
            {
                Notes note = GetCurrentNote(noteId);
                IQueryable<Likes> likes = db.Likes.Where(l => l.NoteId == note.NoteId);
                var cnt = 0;
                var ret = "";
                foreach (var liker in likes)
                {
                    if (cnt != 0) { ret += ","; }
                    cnt++;
                    ret += db.Users.FirstOrDefault(u => u.UserId == liker.UserId).Nickname;
                }
                if (cnt == 0)
                {
                    ret = "无";
                }
                return ret;
            }
                
        }

        /// <summary>
        /// 判断当前用户是否为当前帖子点赞
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        /// <returns></returns>
        [OperationContract]
        [WebGet]
        public bool IsCurrentUserLikesNote(int noteId, string username)
        {
            using (var db = new ICY_SqlEntities())
            {
                Notes note = GetCurrentNote(noteId);
                Users user = db.Users.FirstOrDefault(u => u.Email == username);
                return db.Likes.FirstOrDefault(l => l.UserId == user.UserId && l.NoteId == note.NoteId) != null;
            }
        }

        /// <summary>
        /// 增加帖子的一名未点赞用户
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        [OperationContract]
        [WebGet]
        public string AddLikes(int noteId, string username)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users user = db.Users.FirstOrDefault(u => u.Email == username);
                db.Likes.Add(new Likes { NoteId = noteId, UserId = user.UserId });
                db.SaveChanges();
                return GetLikesOnNote(noteId);
            }         
        }

        /// <summary>
        /// 减少帖子的一名已点赞用户
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        [OperationContract]
        [WebGet]
        public string SubLikes(int noteId, string username)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users user = db.Users.FirstOrDefault(u => u.Email == username);
                db.Likes.Remove(db.Likes.FirstOrDefault(l => l.UserId == user.UserId && l.NoteId == noteId));
                db.SaveChanges();
                return GetLikesOnNote(noteId);
            }
        }

        [OperationContract]
        [WebGet]
        public bool CreateNewNote(string text,string username)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users u = GetUserByEmail(username);
                if (u == null) return false;
                Notes n = new Notes() { UserId = u.UserId,Text=text,Forward=null,Time=DateTime.Now };
                db.Notes.Add(n);
                db.SaveChanges();
                return true;
            }
        }

        [OperationContract]
        [WebGet]
        public bool CreateForwardNote(string text, string username,int forwardId)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users u = GetUserByEmail(username);
                if (u == null) return false;
                Notes n = new Notes() { UserId = u.UserId, Text = text, Forward = forwardId, Time = DateTime.Now };
                db.Notes.Add(n);
                db.SaveChanges();
                return true;
            }
        }

        [OperationContract]
        [WebGet]
        public bool CreateNewComment(string text,string username,int noteId)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users u = GetUserByEmail(username);
                if (u == null) return false;
                Comments c = new Comments() { NoteId = noteId, UserId = u.UserId, Text = text, Time = DateTime.Now };
                db.Comments.Add(c);
                db.SaveChanges();
                return true;
            }
        }

        [OperationContract]
        [WebGet]
        public bool CreateNewChildComment(string text, string username, int noteId,int parentId,int followId)
        {
            using (var db = new ICY_SqlEntities())
            {
                Users u = GetUserByEmail(username);
                if (u == null) return false;
                Comments c = new Comments() { NoteId = noteId, UserId = u.UserId, Text = text, Time = DateTime.Now, UnderCommentId=parentId,FollowCommentId=followId};
                db.Comments.Add(c);
                db.SaveChanges();
                return true;
            }
        }

        [OperationContract]
        [WebGet]
        public IEnumerable<Comments> GetCommentsOnNote(int noteId)
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Comments.Where(c => c.NoteId == noteId && c.UnderCommentId==null).OrderBy(cc => cc.Time).ToList();
            }
        }

        [OperationContract]
        [WebGet]
        public IEnumerable<Comments> GetChildComments(int parentId)
        {
            using (var db = new ICY_SqlEntities())
            {
                return db.Comments.Where(c => c.UnderCommentId == parentId).OrderBy(cc=>cc.Time).ToList();
            }
        }

        [OperationContract]
        [WebGet]
        public Comments GetChildFollowComments(int childCommentId)
        {
            using (var db = new ICY_SqlEntities())
            {
                Comments childComment = GetCurrentComment(childCommentId);
                return GetCurrentComment(childComment.FollowCommentId.Value);
            }
        }
        // 在此处添加更多操作并使用 [OperationContract] 标记它们
    }
}

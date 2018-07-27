using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MVCTest.Repository
{
    public class MomentsRepository : IMomentsRepository
    {
        protected Icy_SqlEntities db = new Icy_SqlEntities();
        /// <summary>
        /// 查询所有帖子
        /// </summary>
        /// <returns></returns>
        public IQueryable<Notes> SelectAllNotes()
        {
            return db.Notes;
        }

        /// <summary>
        /// 对所有帖子按时间降序
        /// </summary>
        /// <returns></returns>
        public IQueryable<Notes> OrderAllNotesByDescending()
        {
            return db.Notes.OrderByDescending(i => i.Time);
        }

        /// <summary>
        /// 查询帖子
        /// </summary>
        /// <param name="noteId"></param>
        /// <returns></returns>
        public Notes GetCurrentNote(int noteId)
        {
            return db.Notes.First(n => n.NoteId == noteId);
        }

        /// <summary>
        /// 查询当前帖子的发帖者昵称
        /// </summary>
        /// <param name="note">当前帖子的实体</param>
        /// <returns></returns>
        public string GetNickNameByNote(Notes note)
        {
            return db.Users.First(u => u.UserId == note.UserId).Nickname;
        }
        /// <summary>
        /// 查询当前帖子的发帖者昵称
        /// </summary>
        /// <param name="noteId">当前帖子的序号</param>
        /// <returns></returns>
        public string GetNickNameByNote(int noteId)
        {
            Notes note = GetCurrentNote(noteId);
            return db.Users.First(u => u.UserId ==note.UserId).Nickname;
        }

        /// <summary>
        /// 判断当前帖子是否为转发
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        public bool IsForward(Notes note)
        {
            return note.Forward != null;
        }

        /// <summary>
        /// 查询当前帖子所转发的原帖
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
        public Notes GetForwardedNote(Notes note)
        {
            return db.Notes.First(u => u.NoteId == note.Forward);
        }

        /// <summary>
        /// 查询为当前帖子点赞的用户们的昵称串（以','连接）
        /// </summary>
        /// <param name="note"></param>
        /// <returns></returns>
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
        public bool IsCurrentUserLikesNote(Notes note, string username)
        {
            Users user = db.Users.First(u => u.Email == username);
            return db.Likes.FirstOrDefault(l => l.UserId == user.UserId && l.NoteId==note.NoteId) != null;
        }

        /// <summary>
        /// 增加帖子的一名未点赞用户
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        public void AddLikes(Notes note, string username)
        {
            int userId=db.Users.First(u => u.Email == username).UserId;
            int noteId = note.NoteId;
            db.Likes.Add(new Likes { NoteId = noteId, UserId=userId });
            db.SaveChanges();
        }

        /// <summary>
        /// 减少帖子的一名已点赞用户
        /// </summary>
        /// <param name="note"></param>
        /// <param name="username"></param>
        public void SubLikes(Notes note, string username)
        {
            int userId = db.Users.FirstOrDefault(u => u.Email == username).UserId;
            int noteId = note.NoteId;
            db.Likes.Remove(db.Likes.First(l => l.UserId == userId && l.NoteId == noteId));
            db.SaveChanges();
        }



        //

    }
    //基本的类似有外键的多表查询，弥补缺少的导航属性


}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVCTest.Repository
{
    public interface IMomentsRepository
    {
        //查询所有帖子
        IQueryable<Notes> SelectAllNotes();

        //对所有帖子按时间降序
        IQueryable<Notes> OrderAllNotesByDescending();

        //查询帖子
        Notes GetCurrentNote(int noteId);

        //查询帖子的发帖者昵称
        string GetNickNameByNote(Notes note);
        string GetNickNameByNote(int noteId);

        //判断帖子是否为转发
        bool IsForward(Notes note);

        //查询帖子所转发的原帖
        Notes GetForwardedNote(Notes note);

        //查询为帖子点赞的用户们的昵称串（以，连接）
        string GetLikesOnNote(Notes note);

        //判断当前用户是否为帖子点赞
        bool IsCurrentUserLikesNote(Notes note, string username);

        //增加帖子的一名未点赞用户
        void AddLikes(Notes note, string username);

        //减少帖子的一名已点赞用户
        void SubLikes(Notes note, string username);

    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace MomentsService
{
    // 注意: 使用“重构”菜单上的“重命名”命令，可以同时更改代码和配置文件中的接口名“IMomentsService”。
    [ServiceContract]
    public interface IMomentsService
    {
        //查询所有帖子
        [OperationContract]
        IEnumerable<Notes> SelectAllNotes();

        //对所有帖子按时间降序
        [OperationContract]
        IEnumerable<Notes> OrderAllNotesByDescending();

        //查询帖子
        [OperationContract]
        Notes GetCurrentNote(int noteId);

        //查询帖子的发帖者昵称
        [OperationContract]
        Users GetUserByNote(Notes note);

        //判断帖子是否为转发
        [OperationContract]
        bool IsForward(Notes note);

        //查询帖子所转发的原帖
        [OperationContract]
        Notes GetForwardedNote(Notes note);

        //查询为帖子点赞的用户们的昵称串（以，连接）
        [OperationContract]
        string GetLikesOnNote(Notes note);

        //判断当前用户是否为帖子点赞
        [OperationContract]
        bool IsCurrentUserLikesNote(Notes note, string username);

        //增加帖子的一名未点赞用户
        [OperationContract]
        bool AddLikes(Notes note, string username);

        //减少帖子的一名已点赞用户
        [OperationContract]
        bool SubLikes(Notes note, string username);
    }
}

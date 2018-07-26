using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace MVCTest.Repository
{
    public class MomentsRepository : IMomentsRepository
    {
        protected Icy_SqlEntities db = new Icy_SqlEntities();

        //查询所有帖子
        public IQueryable<Notes> SelectAllNotes()
        {
            return db.Notes;
        }
    }
        //基本的类似有外键的多表查询，弥补缺少的导航属性

        
}
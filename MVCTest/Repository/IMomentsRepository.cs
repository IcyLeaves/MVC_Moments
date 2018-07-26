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

        //
    }
}

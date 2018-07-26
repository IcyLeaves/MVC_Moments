using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using MVCTest.Models;
using Microsoft.AspNet.Identity;

/*
 * leavescy@126.com
 * Bb19980824.
 * 
 * 534104198@qq.com
 * Aa19980824.
 * 
 * IcyLeaves@google.com
 * Cc19980824.
*/
namespace MVCTest.Controllers
{
    public class NotesController : Controller
    {
        #region Private
        private NoteDBContext db = new NoteDBContext();
        private void AddLikes(NoteDB n, string user)
        {
            if (n.Likes == "无")
                n.Likes = user;
            else
            {
                n.Likes += ",";
                n.Likes += user;
            }
            db.Entry(n).State = EntityState.Modified;
            db.SaveChanges();
        }
        private void SubLikes(NoteDB n, string user)
        {
            string[] Users = n.Likes.Split(',');
            //使用lambda表达式过滤掉user
            Users = Users.Where(s => s != user).ToArray();
            if (Users.Count() == 0)
            {
                Users = new string[] { "无" };
            }
            n.Likes = string.Join(",", Users);
            db.Entry(n).State = EntityState.Modified;
            db.SaveChanges();
        }
        #endregion
        // GET: Notes
        public async Task<ActionResult> Index()
        {
            return View(await db.Notes.ToListAsync());
        }

        // GET: Notes/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            NoteDB noteDB = await db.Notes.FindAsync(id);
            if (noteDB == null)
            {
                return HttpNotFound();
            }
            return View(noteDB);
        }

        // GET: Notes/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Notes/Create
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "Id,Username,Text,Time,Likes,Forward")] NoteDB noteDB)
        {
            if (ModelState.IsValid)
            {
                noteDB.Username = User.Identity.GetUserName();
                noteDB.Likes = "无";
                noteDB.Time = DateTime.Now;
                db.Notes.Add(noteDB);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            return View(noteDB);
        }

        // GET: Notes/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            NoteDB noteDB = await db.Notes.FindAsync(id);
            if (noteDB == null)
            {
                return HttpNotFound();
            }
            return View(noteDB);
        }

        // POST: Notes/Edit/5
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "Id,Username,Text,Time,Likes,Forward")] NoteDB noteDB)
        {
            if (ModelState.IsValid)
            {
                noteDB.Time = DateTime.Now;
                db.Entry(noteDB).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            return View(noteDB);
        }

        // GET: Notes/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            NoteDB noteDB = await db.Notes.FindAsync(id);
            if (noteDB == null)
            {
                return HttpNotFound();
            }
            return View(noteDB);
        }

        // POST: Notes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            NoteDB noteDB = await db.Notes.FindAsync(id);
            db.Notes.Remove(noteDB);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        //  Notes/
        public ActionResult Liked(int id)
        {
            NoteDB noteDB = db.Notes.Find(id);
            AddLikes(noteDB, User.Identity.GetUserName());
            return PartialView("_LikesPartialView", noteDB);
            //return RedirectToAction("Index");
        }

        public JsonResult Liked1(int id)
        {
            NoteDB noteDB = db.Notes.Find(id);
            AddLikes(noteDB, User.Identity.GetUserName());
            return new JsonResult() { Data = noteDB, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
            //return RedirectToAction("Index");
        }

        //  Notes/
        public ActionResult UnLiked(int id)
        {
            NoteDB noteDB = db.Notes.Find(id);
            SubLikes(noteDB, User.Identity.GetUserName());
            return PartialView("_LikesPartialView", noteDB);
            //return RedirectToAction("Index");
        }

        // POST: Notes/Relay/5
        public async Task<ActionResult> Relay(int? id, [Bind(Include = "Id,Username,Text,Time,Likes,Forward")]NoteDB n)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            NoteDB noteDB = await db.Notes.FindAsync(id);
            if (noteDB == null)
            {
                return HttpNotFound();
            }
            //填写后
            if (ModelState.IsValid && n.Text != null)
            {
                n.Username = User.Identity.GetUserName();
                if (noteDB.Forward == 0) n.Forward = noteDB.Id;
                else n.Forward = noteDB.Forward;
                n.Likes = "无";
                n.Time = DateTime.Now;
                db.Notes.Add(n);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            //填写前
            if (noteDB.Forward == 0)
                return View(noteDB);
            else
                return View(db.Notes.Find(noteDB.Forward));
        }

        //Partial Likes
        public PartialViewResult likes()
        {
            return PartialView("_LikesPartialView");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}

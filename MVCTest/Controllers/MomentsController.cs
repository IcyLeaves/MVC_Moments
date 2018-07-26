using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using MVCTest;

namespace MVCTest.Controllers
{
    public class MomentsController : Controller
    {
        private Icy_SqlEntities db = new Icy_SqlEntities();
        private void AddLikes(Notes n,int userid)
        {

        }

        // GET: Moments
        public async Task<ActionResult> Index()
        {
            var notes = db.Notes.Include(n => n.Users);
            return View(await notes.ToListAsync());
        }

        // GET: Moments/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Notes notes = await db.Notes.FindAsync(id);
            if (notes == null)
            {
                return HttpNotFound();
            }
            return View(notes);
        }

        // GET: Moments/Create
        public ActionResult Create()
        {
            ViewBag.UserId = new SelectList(db.Users, "UserId", "Nickname");
            return View();
        }

        // POST: Moments/Create
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "NoteId,UserId,Text,Time,Forward")] Notes notes)
        {
            if (ModelState.IsValid)
            {
                db.Notes.Add(notes);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            ViewBag.UserId = new SelectList(db.Users, "UserId", "Nickname", notes.UserId);
            return View(notes);
        }

        // GET: Moments/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Notes notes = await db.Notes.FindAsync(id);
            if (notes == null)
            {
                return HttpNotFound();
            }
            ViewBag.UserId = new SelectList(db.Users, "UserId", "Nickname", notes.UserId);
            return View(notes);
        }

        // POST: Moments/Edit/5
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "NoteId,UserId,Text,Time,Forward")] Notes notes)
        {
            if (ModelState.IsValid)
            {
                db.Entry(notes).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            ViewBag.UserId = new SelectList(db.Users, "UserId", "Nickname", notes.UserId);
            return View(notes);
        }

        // GET: Moments/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            Notes notes = await db.Notes.FindAsync(id);
            if (notes == null)
            {
                return HttpNotFound();
            }
            return View(notes);
        }

        // POST: Moments/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            Notes notes = await db.Notes.FindAsync(id);
            db.Notes.Remove(notes);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        // GET: Moments/Liked/5
        public JsonResult Liked(int id)
        {
            Notes notes = db.Notes.Find(id);
            AddLikes(notes,);
            return new JsonResult() { Data = notes, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
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

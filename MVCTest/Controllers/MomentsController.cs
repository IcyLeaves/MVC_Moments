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
using MVCTest.Repository;
using Microsoft.AspNet.Identity;

namespace MVCTest.Controllers
{
    public class MomentsController : Controller
    {
        private IMomentsRepository mo = new MomentsRepository();

        // GET: Moments
        public ActionResult Index()
        {
            return View(mo);
        }
        
        // GET: Moments/Liked/5
        public JsonResult Liked(int id)
        {
            Notes n = mo.GetCurrentNote(id);
            string username = User.Identity.GetUserName();
            mo.AddLikes(n, username);
            string ret = mo.GetLikesOnNote(n);
            return new JsonResult(){ Data = ret, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
        }

        //GET: Moments/UnLiked/5
        public JsonResult UnLiked(int id)
        {
            Notes n = mo.GetCurrentNote(id);
            string username = User.Identity.GetUserName();
            mo.SubLikes(n, username);
            string ret = mo.GetLikesOnNote(n);
            return new JsonResult() { Data = ret, JsonRequestBehavior = JsonRequestBehavior.AllowGet };
        }
    }
}
/*
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
            return View();
        }

        // POST: Moments/Create
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "NoteId,UserId,Text,Forward,Time")] Notes notes)
        {
            if (ModelState.IsValid)
            {
                db.Notes.Add(notes);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

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
            return View(notes);
        }

        // POST: Moments/Edit/5
        // 为了防止“过多发布”攻击，请启用要绑定到的特定属性，有关 
        // 详细信息，请参阅 http://go.microsoft.com/fwlink/?LinkId=317598。
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "NoteId,UserId,Text,Forward,Time")] Notes notes)
        {
            if (ModelState.IsValid)
            {
                db.Entry(notes).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
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

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
        */

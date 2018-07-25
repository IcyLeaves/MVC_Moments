using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;

namespace MVCTest.Models
{
    public class NoteDB
    {
        public int Id { get; set; }
        public string Username { get; set; }
        public string Text { get; set; }
        public DateTime Time { get; set; }
        public string Likes { get; set; }
        public int Forward { get; set; }
    }

    public class NoteDBContext:DbContext
    {
        public DbSet<NoteDB> Notes { get; set; }
    }
}
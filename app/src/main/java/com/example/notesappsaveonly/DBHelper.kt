package com.example.notesappsaveonly

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

//details.dp is the database name
class DBHelper(
    context: Context
) : SQLiteOpenHelper(context, "note.dp", null, 3) {
    var sqLightDatabase: SQLiteDatabase = writableDatabase
    override fun onCreate(dp: SQLiteDatabase?) {
        if (dp != null) {
            dp.execSQL("create table note ( Id INTEGER PRIMARY KEY,Note text,Color text)")
        }

    }

    override fun onUpgrade(dp: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion != newVersion && dp != null) {
            dp.execSQL("DROP TABLE IF EXISTS note")
            onCreate(dp)
        }
    }

    fun saveData(noteObj: Note): Long {
        val cv = ContentValues()
        cv.put("Note", noteObj.note)
        cv.put("Color", noteObj.color)

        var status = sqLightDatabase.insert("note", null, cv)//status
        return status

    }

    @SuppressLint("Range")
    fun readData() :ArrayList<Note>{
        var selectQuery = "SELECT  * FROM note"
        var cursor: Cursor? = null
        try {

            cursor = sqLightDatabase.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            sqLightDatabase.execSQL(selectQuery)
        }
        var notes=ArrayList<Note>()
        var id:Int
        var note: String
        var color: String
        if (cursor != null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    id=cursor.getInt(cursor.getColumnIndex("Id"))
                    note = cursor.getString(cursor.getColumnIndex("Note"))
                    color = cursor.getString(cursor.getColumnIndex("Color"))
                    notes.add(Note(id,note,color))

                    Log.d("DATA:"," $note $color")
                } while (cursor.moveToNext())
            }
        }
        return notes
    }
    fun updateNote(noteOBJ:Note): Int {
        val cv = ContentValues()
        cv.put("Note", noteOBJ.note)
        cv.put("Color", noteOBJ.color)

        var rowNum = sqLightDatabase.update("note", cv,"Id = ${noteOBJ.id}",null)
        return rowNum

    }
    fun deleteNote(noteOBJ:Note){
        sqLightDatabase.delete("note","Id = ${noteOBJ.id}",null)
    }
}
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
) : SQLiteOpenHelper(context, "note.dp", null, 1) {
    var sqlightDB: SQLiteDatabase = writableDatabase
    override fun onCreate(dp: SQLiteDatabase?) {
        if (dp != null) {
            dp.execSQL("create table note (Title text ,Note text,Color text)")
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
        cv.put("Title", noteObj.title)
        cv.put("Note", noteObj.note)
        cv.put("Color", noteObj.color)

        var status = sqlightDB.insert("note", null, cv)//status
        return status

    }

    @SuppressLint("Range")
    fun readData() :ArrayList<Note>{
        var selectQuery = "SELECT  * FROM note"
        var cursor: Cursor? = null
        try {

            cursor = sqlightDB.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            sqlightDB.execSQL(selectQuery)
        }
        var notes=ArrayList<Note>()
        var title: String
        var note: String
        var color: String
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    title = cursor.getString(cursor.getColumnIndex("Title"))
                    note = cursor.getString(cursor.getColumnIndex("Note"))
                    color = cursor.getString(cursor.getColumnIndex("Color"))
                    notes.add(Note(title,note,color))

                    Log.d("DATA:","$title $note $color")
                } while (cursor.moveToNext())
            }
        }
        return notes
    }
}
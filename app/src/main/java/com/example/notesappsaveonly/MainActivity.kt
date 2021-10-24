package com.example.notesappsaveonly

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.RadioButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialogue_view.*
import kotlinx.android.synthetic.main.dialogue_view.view.*

class MainActivity : AppCompatActivity() {
    lateinit var rvMain: RecyclerView
    lateinit var gridView: GridView
    var notes = ArrayList<Note>()
    private val dbHelper by lazy {  DBHelper(applicationContext)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain = findViewById(R.id.rvMain)
        gridView = findViewById(R.id.glMain)

        gridView.adapter = GridViewAdapter(notes, this@MainActivity)

        rvMain.adapter = RecyclerViewAdapter(notes)
        rvMain.layoutManager = LinearLayoutManager(this)
        readFromDB()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.view->{
                if( rvMain.visibility== View.GONE) {
                    rvMain.visibility = View.VISIBLE
                    gridView.visibility = View.GONE
                }else{
                    rvMain.visibility = View.GONE
                    gridView.visibility = View.VISIBLE
                }
            }
            R.id.addNote -> {
                var title = ""
                var note = ""
                var colorText = "Red"
                val builder = AlertDialog.Builder(this)
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialogue_view, null)
                builder.setView(dialogView)
                dialogView.colorsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                        when (checkedId) {
                            R.id.rbRed -> {
                                colorText = "Red"
                            }
                            R.id.rbBlue -> {
                                colorText = "Blue"
                            }
                            R.id.rbGreen -> {
                                colorText = "Green"
                            }
                            R.id.rbYellow -> {
                                colorText = "Yellow"
                            }

                        }
                        dialogView.tvColor.setText(colorText)
                    }
                dialogView.btAddNote.setOnClickListener {
                    title=dialogView.etNoteTitle.text.toString()
                    note=dialogView.etNote.text.toString()
                    if (title.isNotEmpty() && note.isNotEmpty()) {
                        saveToDB(Note(title, note, colorText))
                        readFromDB()
                        dialogView.etNoteTitle.setText("")
                        dialogView.etNote.setText("")
                        dialogView.rbRed.isChecked = true
                        dialogView.rbBlue.isChecked = false
                        dialogView.rbGreen.isChecked = false
                        dialogView.rbYellow.isChecked = false

                    }else{
                        dialogView.tvColor.setText("Fill all info!")
                        dialogView.tvColor.setTextColor(Color.RED)

                    }
                }
                builder.setPositiveButton("Close") { dialog, which -> dialog.dismiss() }

                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(true)
                alertDialog.show()


                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun saveToDB(noteObj: Note) {
        var status = dbHelper.saveData(noteObj)
        Toast.makeText(this, "Data saved successfully $status", Toast.LENGTH_SHORT).show()

    }

    fun readFromDB() {
        notes.clear()
        notes.addAll(dbHelper.readData())
        rvMain.adapter!!.notifyDataSetChanged()

    }
}
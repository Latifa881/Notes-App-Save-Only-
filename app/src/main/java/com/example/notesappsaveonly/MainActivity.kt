package com.example.notesappsaveonly

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.dialogue_view_add.view.*

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

        gridView.adapter = GridViewAdapter(notes, this@MainActivity,this)

        rvMain.adapter = RecyclerViewAdapter(notes,this,this)
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
                var note = ""
                var colorText = "Red"
                val builder = AlertDialog.Builder(this)
                val dialogView = LayoutInflater.from(this).inflate(R.layout.dialogue_view_add, null)
                builder.setView(dialogView)
                val alertDialog: AlertDialog = builder.create()

                dialogView.colorsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
                        when (checkedId) {
                            R.id.rbRed -> {
                                colorText = "Red"
                                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_red)
                            }
                            R.id.rbBlue -> {
                                colorText = "Blue"
                                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_blue)
                            }
                            R.id.rbGreen -> {
                                colorText = "Green"
                                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_green)
                            }
                            R.id.rbYellow -> {
                                colorText = "Yellow"
                                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_yellow)
                            }

                        }

                    }
                dialogView.btAddNote.setOnClickListener {

                    note=dialogView.etNote.text.toString()
                    if ( note.isNotEmpty()) {
                        saveToDB(Note( 0,note, colorText))
                        readFromDB()
                        alertDialog.dismiss()
                        dialogView.etNote.setText("")
                        dialogView.rbRed.isChecked = true
                        dialogView.rbBlue.isChecked = false
                        dialogView.rbGreen.isChecked = false
                        dialogView.rbYellow.isChecked = false

                    }
                }

                alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

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
        Toast.makeText(this, "Data saved successfully at [$status]", Toast.LENGTH_SHORT).show()

    }

    fun readFromDB() {
        notes.clear()
        notes.addAll(dbHelper.readData())
        rvMain.adapter!!.notifyDataSetChanged()

    }
}
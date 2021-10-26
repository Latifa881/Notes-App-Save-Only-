package com.example.notesappsaveonly

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import kotlinx.android.synthetic.main.dialogue_view_add.view.*
import kotlinx.android.synthetic.main.dialogue_view_add.view.alertLayout
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbBlue
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbGreen
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbRed
import kotlinx.android.synthetic.main.dialogue_view_add.view.rbYellow
import kotlinx.android.synthetic.main.dialogue_view_update_delete.view.*
import kotlinx.android.synthetic.main.item_row.view.*


class GridViewAdapter(val notes: ArrayList<Note>,

                      val context: Context,val mainActivity:MainActivity
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null

    private lateinit var tvNote: TextView
    private lateinit var linearLayout:LinearLayout
    private lateinit var ivOptions:ImageView


    override fun getCount(): Int {
        return notes.size
    }
    override fun getItem(position: Int): Any? {
        return notes[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        val id = notes[position].id
        val note = notes[position].note
        val color=notes[position].color


        if (layoutInflater==null)
        {
            layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        }
        if (convertView == null) {
            convertView = layoutInflater!!.inflate(R.layout.item_row, null)
        }

        if (convertView != null) {

            tvNote=convertView.findViewById(R.id.tvNote)
            linearLayout=convertView.findViewById(R.id.linearLayout)
            ivOptions=convertView.findViewById(R.id.ivOptions)
            when (color) {
                "Red" -> linearLayout.setBackgroundResource(R.drawable.round_layout_red)
                "Blue" -> linearLayout.setBackgroundResource(R.drawable.round_layout_blue)
                "Green" -> linearLayout.setBackgroundResource(R.drawable.round_layout_green)
                "Yellow" -> linearLayout.setBackgroundResource(R.drawable.round_layout_yellow)
            }

            ivOptions.setOnClickListener {
                val dbHelper = DBHelper(context)
                var colorText=color
                val builder = AlertDialog.Builder(context)
                val dialogView = LayoutInflater.from(context).inflate(R.layout.dialogue_view_update_delete, null)
                //Set the background color
                changeBackGroundColor(color, dialogView)
                builder.setView(dialogView)
                val alertDialog: AlertDialog = builder.create()
                dialogView.etYourNote.setText(note)
                dialogView.colorsRadioGroupUpdateDelete.setOnCheckedChangeListener { group, checkedId ->
                    colorText=  changeBackGroundColor(checkedId, dialogView)

                }
                dialogView.btUpdateNote.setOnClickListener {
                    val updatedNote = dialogView.etYourNote.text.toString()
                    if (updatedNote.isNotEmpty()) {
                        val rowNum=  dbHelper.updateNote(Note(id,updatedNote, colorText))
                        Toast.makeText(context,"Updated successfully. $rowNum row(s) updated",
                            Toast.LENGTH_SHORT).show()
                        alertDialog.dismiss()
                        mainActivity.readFromDB()

                    } else {
                        Toast.makeText(context,"You need to enter a note!", Toast.LENGTH_SHORT).show()
                    }

                }
                dialogView.btDeleteNote.setOnClickListener {
                    val deleteBuilder = AlertDialog.Builder(context)
                    deleteBuilder.setTitle("Delete note")
                    deleteBuilder.setMessage("Are you sure you want to delete this note?")
                    deleteBuilder.setIcon(android.R.drawable.ic_dialog_alert)

                    deleteBuilder.setPositiveButton("Delete"){dialogInterface, which ->
                        dbHelper.deleteNote(Note(id,note,color))
                        alertDialog.dismiss()
                        mainActivity.readFromDB()
                        dialogInterface.dismiss()
                    }
                    //performing cancel action
                    deleteBuilder.setNeutralButton("Cancel"){dialogInterface , which ->
                        dialogInterface.dismiss()
                    }
                    deleteBuilder.setCancelable(true)
                    deleteBuilder.show()
                }

                alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.setCancelable(true)
                alertDialog.show()
            }

            tvNote.text=note


        }
        return convertView
    }
    private fun changeBackGroundColor(colorText: String, dialogView: View) {
        when (colorText) {
            "Red" -> {
                dialogView.rbRed.isChecked = true
                dialogView.rbBlue.isChecked = false
                dialogView.rbGreen.isChecked = false
                dialogView.rbYellow.isChecked = false
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_red)
            }
            "Blue" -> {
                dialogView.rbRed.isChecked = false
                dialogView.rbBlue.isChecked = true
                dialogView.rbGreen.isChecked = false
                dialogView.rbYellow.isChecked = false

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_blue)
            }
            "Green" -> {
                dialogView.rbRed.isChecked = false
                dialogView.rbBlue.isChecked = false
                dialogView.rbGreen.isChecked = true
                dialogView.rbYellow.isChecked = false
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_green)
            }
            "Yellow" -> {
                dialogView.rbRed.isChecked = false
                dialogView.rbBlue.isChecked = false
                dialogView.rbGreen.isChecked = false
                dialogView.rbYellow.isChecked = true
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_yellow)
            }

        }

    }
    private fun changeBackGroundColor(checkedId: Int, dialogView: View): String {
        var colorText = "Red"
        when (checkedId) {
            R.id.rbRed -> {
                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_red)
                colorText = "Red"
            }
            R.id.rbBlue -> {

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_blue)
                colorText = "Blue"
            }
            R.id.rbGreen -> {

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_green)
                colorText = "Green"
            }
            R.id.rbYellow -> {

                dialogView.alertLayout.setBackgroundResource(R.drawable.round_layout_yellow)
                colorText = "Yellow"
            }

        }
        return colorText
    }
}
package com.example.notesappsaveonly

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout

import android.widget.TextView
import kotlinx.android.synthetic.main.item_row.view.*


class GridViewAdapter(val notes: ArrayList<Note>,

                      val context: Context
) : BaseAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private lateinit var tvTitle: TextView
    private lateinit var tvNote: TextView
    private lateinit var linearLayout:LinearLayout

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
        val title = notes[position].title
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
            tvTitle=convertView.findViewById(R.id.tvTitle)
            tvNote=convertView.findViewById(R.id.tvNote)
            linearLayout=convertView.findViewById(R.id.linearLayout)
            when(color){
                "Red"-> linearLayout.setBackgroundResource(R.drawable.round_layout_red)
                "Blue"-> linearLayout.setBackgroundResource(R.drawable.round_layout_blue)
                "Green"-> linearLayout.setBackgroundResource(R.drawable.round_layout_green)
                "Yellow"-> linearLayout.setBackgroundResource(R.drawable.round_layout_yellow)
            }
            tvTitle.text = title
            tvNote.text=note


        }
        return convertView
    }
}
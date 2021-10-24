package com.example.notesappsaveonly

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class RecyclerViewAdapter (private val notes:ArrayList<Note>): RecyclerView.Adapter<RecyclerViewAdapter.ItemViewHolder>(){
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val text=notes[position]
        holder.itemView.apply {
            tvTitle.text=text.title
            tvNote.text=text.note
            when(text.color){
                "Red"-> linearLayout.setBackgroundResource(R.drawable.round_layout_red)
                "Blue"-> linearLayout.setBackgroundResource(R.drawable.round_layout_blue)
                "Green"-> linearLayout.setBackgroundResource(R.drawable.round_layout_green)
                "Yellow"-> linearLayout.setBackgroundResource(R.drawable.round_layout_yellow)
            }

        }
    }

    override fun getItemCount()=notes.size
}

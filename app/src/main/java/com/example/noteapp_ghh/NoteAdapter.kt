package com.example.noteapp_ghh

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp_ghh.roomModel.Note
import com.example.noteapp_ghh.databinding.RowBinding

class NoteAdapter(var notes: List<Note>, var activity: MainActivity): RecyclerView.Adapter<NoteAdapter.Holder>() {
    class Holder( val Binding: RowBinding): RecyclerView.ViewHolder(Binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
       return Holder(RowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       val item = notes[position]
        holder.Binding.apply {
            tvNote.text = item.note
        }
        holder.Binding.cv.setOnClickListener {
            activity.toDetails(true,item)
        }

    }

    override fun getItemCount() = notes.size

    fun update(newList: List<Note>){
        this.notes = newList
        notifyDataSetChanged()
    }

}
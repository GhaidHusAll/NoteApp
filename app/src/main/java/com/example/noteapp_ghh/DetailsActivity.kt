package com.example.noteapp_ghh

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp_ghh.databinding.ActivityDetailsBinding
import com.example.noteapp_ghh.roomModel.Note
import com.example.noteapp_ghh.roomModel.NoteDatabase
import com.example.noteapp_ghh.viewModel.NoteViewModel
import kotlinx.coroutines.*

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private val noteDao by lazy { NoteDatabase.getDatabase(this).dao() }
    private val vm by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }

    private var theNote: Note? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val isEdit =  intent.getBooleanExtra("isEdit",false)
        if (isEdit){
            theNote = Note(intent.getLongExtra("pk",0), intent.getStringExtra("note").toString(), intent.getStringExtra("id").toString())
            binding.llAdd.visibility = View.GONE
            binding.tvNote.setText( intent.getStringExtra("note").toString())
        }else{
            binding.llEdit.visibility = View.GONE
        }

        binding.apply {
            btnAdd.setOnClickListener {
                if (tvNote.text.isNotEmpty()){
                    vm.addNoteFirebase(Note(0,tvNote.text.toString(),""))
                    toMain()
                        Toast.makeText(this@DetailsActivity, "Note Added Successfully online", Toast.LENGTH_LONG).show()
                        tvNote.text.clear()
                }else{
                    Toast.makeText(this@DetailsActivity, "Please fill the field", Toast.LENGTH_LONG).show()
                }
            }
            btnDelete.setOnClickListener {
                if ( theNote != null){
                    deleteAlert( theNote!!)
                }else{
                    Toast.makeText(this@DetailsActivity, "something went wrong", Toast.LENGTH_LONG).show()
                }
            }
            btnEdit.setOnClickListener {
                if ( theNote != null){
                    vm.updateNoteFirebase(Note( theNote!!.pk,tvNote.text.toString(), intent.getStringExtra("id").toString()))
                    toMain()
                    Toast.makeText(this@DetailsActivity, "Note Updated Successfully", Toast.LENGTH_LONG).show()
                    tvNote.text.clear()
                }else{
                    Toast.makeText(this@DetailsActivity, "something went wrong", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun toMain(){
        val toDetails = Intent(this,MainActivity::class.java)
        startActivity(toDetails)
    }
    private fun deleteAlert(note: Note){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Note")
        builder.setMessage("Are You sure you want to delete this note ${note.note}")
        builder.setPositiveButton("Delete") { _, _ ->
            vm.deleteNoteFirebase(note)
            binding.tvNote.text.clear()
            toMain()
            Toast.makeText(this@DetailsActivity, "the Note deleted successfully ", Toast.LENGTH_LONG).show()
        }
        builder.setNegativeButton("Close") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
}



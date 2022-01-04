package com.example.noteapp_ghh


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp_ghh.roomModel.Note
import com.example.noteapp_ghh.databinding.ActivityMainBinding
import com.example.noteapp_ghh.roomModel.NoteDatabase
import com.example.noteapp_ghh.viewModel.NoteViewModel
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteList: ArrayList<Note>
    private lateinit var myAdapter: NoteAdapter
    private val vm by lazy { ViewModelProvider(this).get(NoteViewModel::class.java) }
    private val noteDao by lazy { NoteDatabase.getDatabase(this).dao() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        noteList = arrayListOf()
        setAdapter(noteList)

        vm.getNotesFirebase().observe(this,{
           notes -> myAdapter.update(notes)
        })

    }
    private fun setAdapter(list: ArrayList<Note>){
        myAdapter = NoteAdapter(list,this)
        binding.apply {
            mainRV.adapter = myAdapter
            mainRV.layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    fun toDetails(isEdit: Boolean, note: Note?){
        val toDetails = Intent(this@MainActivity,DetailsActivity::class.java)
        if (isEdit){
            toDetails.putExtra("note",note?.note)
            toDetails.putExtra("pk",note?.pk)
            toDetails.putExtra("id",note?.id)
            toDetails.putExtra("isEdit",true)
        }
        startActivity(toDetails)
    }


}
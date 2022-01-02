package com.example.noteapp_ghh.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp_ghh.roomModel.Note
import com.example.noteapp_ghh.roomModel.NoteDao
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class NoteViewModel: ViewModel() {

     var notes: MutableLiveData<List<Note>>  = MutableLiveData()

    init {
        notes.postValue(listOf(Note(0,"")))
    }

    fun getNotes(noteDao:NoteDao):LiveData<List<Note>>{
        CoroutineScope(IO).launch {
            val data = async {
                noteDao.getNote()
            }.await()
            if (data.isNotEmpty()) {
                notes.postValue(data)
            } else {
                Log.e("MAIN", "Unable to get data",)
            }
        }
        return notes
    }
    fun updateNote(note:Note,noteDao:NoteDao){
        CoroutineScope(IO).launch {
                noteDao.updateNote(note)
                notes.postValue(noteDao.getNote())
        }
    }
    fun deleteNote(note:Note,noteDao:NoteDao){
        CoroutineScope(IO).launch {
            noteDao.deleteNote(note)
            notes.postValue(noteDao.getNote())
        }
    }
    fun addNote(note:Note,noteDao:NoteDao){
        CoroutineScope(IO).launch {
            noteDao.addNote(note)
            notes.postValue(noteDao.getNote())
        }
    }

}
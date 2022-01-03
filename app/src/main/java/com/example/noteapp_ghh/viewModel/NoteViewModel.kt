package com.example.noteapp_ghh.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.noteapp_ghh.roomModel.Note
import com.example.noteapp_ghh.roomModel.NoteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import com.google.firebase.firestore.QueryDocumentSnapshot

import com.google.firebase.firestore.QuerySnapshot

import com.google.android.gms.tasks.Task

import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnCompleteListener





class NoteViewModel: ViewModel() {

     var notes: MutableLiveData<List<Note>>  = MutableLiveData()
    var db = Firebase.firestore

    init {
        notes.postValue(listOf(Note(0,"","")))
    }

    fun getNotes(noteDao:NoteDao):LiveData<List<Note>>{
        CoroutineScope(IO).launch {
            val data = async {
                noteDao.getNote()
            }.await()
            if (data.isNotEmpty()) {
                notes.postValue(data)
            } else {
                Log.e("MAIN", "Unable to get data")
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
    fun addNoteFirebase(noteInput: Note){
        // Create a new user with a first and last name
        // Create a new user with a first and last name
        val note: MutableMap<String, Any> = HashMap()
        note["note"] = noteInput.note
        note["pk"] = UUID.randomUUID().mostSignificantBits

// Add a new document with a generated ID

// Add a new document with a generated ID
        db.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "TAG VM",
                    "-----------------------DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e -> Log.w("TAG VM", "-------------------Error adding document", e) }
    }
    fun getNotesFirebase():LiveData<List<Note>>{
        db.collection("notes")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data : MutableList<Note> = mutableListOf()
                        for (document in task.result!!) {
                        Log.d("TAG VM", document.id + " => " + document.data["note"] + document.data["pk"])
                           data.add(Note(document.data["pk"].toString().toLong(), document.data["note"].toString(),document.id))
                    }
                    notes.postValue(data)
                } else {
                    Log.w("TAG VM", "Error getting documents.", task.exception)
                }
            }
        return notes
    }
    fun updateNoteFirebase(note: Note){
        db.collection("notes").document(note.id).update("note", note.note)
            .addOnSuccessListener { _ ->
                Log.d(
                    "TAG VM",
                    "-----------------------DocumentSnapshot edit with ID: ${note.id} "
                ) }
            .addOnFailureListener {  e -> Log.w("TAG VM", "-------------------Error edit document", e) }
    }
    fun deleteNoteFirebase(note: Note){
        db.collection("notes").document(note.id).delete()
            .addOnSuccessListener { _ ->
                Log.d(
                    "TAG VM",
                    "-----------------------DocumentSnapshot edit with ID: ${note.id} "
                ) }
            .addOnFailureListener {  e -> Log.w("TAG VM", "-------------------Error edit document", e) }
    }

}
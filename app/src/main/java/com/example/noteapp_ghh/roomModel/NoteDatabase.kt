package com.example.noteapp_ghh.roomModel

import android.content.Context
import androidx.room.*


@Database(entities = [Note::class],version = 1,exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun dao(): NoteDao

    companion object{
        //visible to other threads
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getDatabase(context: Context): NoteDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){ // protection on multi threads
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "Notes"
                ).fallbackToDestructiveMigration() // destroy old DB if ver changes
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
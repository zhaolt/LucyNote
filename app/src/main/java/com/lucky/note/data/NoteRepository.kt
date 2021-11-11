package com.lucky.note.data

import android.content.Context
import com.lucky.note.base.SingletonHolder

/**
 * @Created by Walter on 2021/11/8
 */
class NoteRepository private constructor(var local: NoteDataSource): NoteDataSource {

    override fun getAllNotes(context: Context) = local.getAllNotes(context)

    override fun saveNote(context: Context, note: Note) {
        local.saveNote(context, note)
    }

    companion object : SingletonHolder<NoteRepository, NoteDataSource>(::NoteRepository) {
        private const val TAG = "NoteRepository"
    }


}
package com.lucky.note.data

import android.content.Context
import io.reactivex.rxjava3.core.Flowable

/**
 * @Created by Walter on 2021/11/8
 */
interface NoteDataSource {

    fun getAllNotes(context: Context): Flowable<MutableList<Note>?>

    fun saveNote(context: Context, note: Note)

}
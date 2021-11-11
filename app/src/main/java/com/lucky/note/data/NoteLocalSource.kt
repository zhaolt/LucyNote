package com.lucky.note.data

import android.content.Context
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Created by Walter on 2021/11/8
 */
class NoteLocalSource private constructor(): NoteDataSource {

    override fun getAllNotes(context: Context): Flowable<MutableList<Note>?> {
        return Flowable.just(NoteDaoManager.instance.queryAll(context))
            .subscribeOn(Schedulers.io())
    }

    override fun saveNote(context: Context, note: Note) {
        NoteDaoManager.instance.saveData(context, note)
    }

    companion object {
        @JvmStatic
        val instance: NoteLocalSource by lazy { NoteLocalSource() }
    }

}
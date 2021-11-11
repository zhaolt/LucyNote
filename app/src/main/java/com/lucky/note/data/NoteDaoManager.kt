package com.lucky.note.data

import android.content.Context
import com.lucky.note.greendao.NoteDao
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * @Created by Walter on 2021/11/8
 */
class NoteDaoManager private constructor() {

    fun insertData(context: Context, note: Note) {
        DbManager.getInstance(context).getDaoSession(context)?.noteDao?.insert(note)
    }

    fun saveData(context: Context, note: Note) {
        DbManager.getInstance(context).getDaoSession(context)?.noteDao?.save(note)
    }

    fun deleteData(context: Context, note: Note) {
        DbManager.getInstance(context).getDaoSession(context)?.noteDao?.delete(note)
    }

    fun deleteDataByKey(context: Context, id: Long) {
        DbManager.getInstance(context).getDaoSession(context)?.noteDao?.deleteByKey(id)
    }

    fun updateData(context: Context, note: Note) {
        DbManager.getInstance(context).getDaoSession(context)?.noteDao?.update(note)
    }

    fun queryAll(context: Context): MutableList<Note>? {
        val builder = DbManager.getInstance(context).getDaoSession(context)?.noteDao?.queryBuilder()
        return builder?.build()?.list()
    }

    fun queryById(context: Context, id: Long): MutableList<Note>? {
        val builder = DbManager.getInstance(context).getDaoSession(context)?.noteDao?.queryBuilder()
        return builder?.where(NoteDao.Properties.Id.eq(id))?.list()
    }

    companion object {
        @JvmStatic
        val instance: NoteDaoManager by lazy { NoteDaoManager() }
    }

}
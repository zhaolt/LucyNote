package com.lucky.note.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import com.lucky.note.base.SingletonHolder
import com.lucky.note.greendao.DaoMaster
import com.lucky.note.greendao.DaoSession

/**
 * @Created by Walter on 2021/11/5
 */
class DbManager private constructor(context: Context) {

    private var devOpenHelper: DaoMaster.DevOpenHelper? = null
    private var daoMaster: DaoMaster? = null
    private var daoSession: DaoSession? = null

    init {
        devOpenHelper = DaoMaster.DevOpenHelper(context, DB_NAME)
        getDaoMaster(context)
        getDaoSession(context)
    }

    fun getWritableDatabase(context: Context): SQLiteDatabase? {
        if (null == devOpenHelper)
            getInstance(context)
        return devOpenHelper?.writableDatabase
    }

    fun getDaoMaster(context: Context): DaoMaster? {
        if (null == daoMaster) {
            synchronized(DbManager::class.java) {
                if (null == daoMaster)
                    daoMaster = DaoMaster(getWritableDatabase(context))
            }
        }
        return daoMaster
    }

    fun getDaoSession(context: Context): DaoSession? {
        if (null == daoSession)
            synchronized(DbManager::class.java) {
                daoSession = getDaoMaster(context)?.newSession()
            }
        return daoSession
    }

    companion object : SingletonHolder<DbManager, Context>(::DbManager) {
        private const val DB_NAME = "LuckyNotes.db"
    }

}
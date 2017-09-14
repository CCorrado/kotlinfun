package com.ccorrads.kotlinfun.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.field.DataPersisterManager
import com.j256.ormlite.support.ConnectionSource
import com.ccorrads.kotlinfun.BuildConfig
import com.ccorrads.kotlinfun.serialization.DateTimePersister

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 *
 * Constructor for db w/o encryption (dev)
 *
 * @param context -- Application context
 */
class DatabaseHelper(context: Context?, databaseName: String?)
    : OrmLiteSqliteOpenHelper(context, databaseName, null, BuildConfig.databaseVersion) {

    init {
        try {
            DataPersisterManager.registerDataPersisters(DateTimePersister.singleton)
        } catch (e: Exception) {
            Log.e(TAG, "onCreate registering DateTimePersister", e)
        }
    }

    @Synchronized
    private fun resetDatabase() {
        com.ccorrads.kotlinfun.database.DatabaseTableUtil.dropTables(connectionSource, TAG)
        com.ccorrads.kotlinfun.database.DatabaseTableUtil.setUpTables(connectionSource, TAG)
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    override fun onCreate(db: SQLiteDatabase, connectionSource: ConnectionSource) {
        Log.i(TAG, "onCreate")
        com.ccorrads.kotlinfun.database.DatabaseTableUtil.setUpTables(connectionSource, TAG)
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVersion: Int, newVersion: Int) {
        resetDatabase()
    }

    companion object {

        private val TAG = DatabaseHelper::class.java.simpleName
    }
}
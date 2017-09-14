package com.ccorrads.kotlinfun.database

import android.util.Log
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.ccorrads.kotlinfun.models.MealPrep
import java.sql.SQLException


/**
 * Function to keep the table setup for the app in one place.
 */
object DatabaseTableUtil {

    /**
     * List of all database model classes
     */
    private val tableClasses = arrayOf<Class<*>>(MealPrep::class.java)

    /**
     * Simple function to build tables for app.
     */
    fun setUpTables(connectionSource: ConnectionSource, tag: String) {
        try {
            for (cls in com.ccorrads.kotlinfun.database.DatabaseTableUtil.tableClasses) {
                TableUtils.createTable(connectionSource, cls)
            }
        } catch (e: SQLException) {
            Log.e(tag, e.message, e)
        }
    }

    /**
     * Drops all tables in the database
     */
    fun dropTables(connectionSource: ConnectionSource, tag: String) {
        try {
            for (cls in com.ccorrads.kotlinfun.database.DatabaseTableUtil.tableClasses) {
                TableUtils.clearTable(connectionSource, cls)
            }
        } catch (e: SQLException) {
            Log.e(tag, e.message, e)
        }
    }
}
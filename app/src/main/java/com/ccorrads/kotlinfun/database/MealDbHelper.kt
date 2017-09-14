package com.ccorrads.kotlinfun.database

import android.util.Log
import com.j256.ormlite.dao.Dao
import com.ccorrads.kotlinfun.models.MealPrep
import java.sql.SQLException

class MealDbHelper(private val databaseHelper: DatabaseHelper) {

    companion object {
        val ID_COL: String = "id"
        val NEED_SYNC_COL: String = "needSync"
    }

    @Synchronized
    fun getMealPreps(): List<MealPrep> {
        val mealPreps = ArrayList<MealPrep>()
        try {
            val mealPrepDao = databaseHelper.getDao(MealPrep::class.java) as Dao
            mealPreps.addAll(mealPrepDao.queryForAll())
        } catch (e: SQLException) {
            Log.e(this.javaClass.simpleName, e.message, e)
        }
        return mealPreps
    }

    @Synchronized
    fun getMealPrepsForSync(): List<MealPrep> {
        val mealPreps = ArrayList<MealPrep>()
        try {
            val mealPrepDao = databaseHelper.getDao(MealPrep::class.java) as Dao
            val mealPrepQueryBuilder = mealPrepDao.queryBuilder()
            mealPrepQueryBuilder.where().eq(NEED_SYNC_COL, true)
            mealPreps.addAll(mealPrepDao.queryForAll())
        } catch (e: SQLException) {
            Log.e(this.javaClass.simpleName, e.message, e)
        }
        return mealPreps
    }

    @Synchronized
    fun getMealPrepForId(id: Int?): MealPrep? {
        var mealPrep: MealPrep? = null
        try {
            val mealPrepDao = databaseHelper.getDao(MealPrep::class.java) as Dao
            val mealPrepQueryBuilder = mealPrepDao.queryBuilder()
            mealPrepQueryBuilder.where().eq(ID_COL, id)
            mealPrep = mealPrepQueryBuilder.queryForFirst()
        } catch (e: SQLException) {
            Log.e(this.javaClass.simpleName, e.message, e)
        }
        return mealPrep
    }

    @Synchronized
    fun saveMealPrep(mealPrep: MealPrep) {
        try {
            val mealPrepDao = databaseHelper.getDao(MealPrep::class.java) as Dao
            mealPrepDao.createOrUpdate(mealPrep)
        } catch (e: SQLException) {
            Log.e(this.javaClass.simpleName, e.message, e)
        }
    }
}
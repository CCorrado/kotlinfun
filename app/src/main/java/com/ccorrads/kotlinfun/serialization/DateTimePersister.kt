package com.ccorrads.kotlinfun.serialization

import android.text.TextUtils
import android.util.Log
import com.j256.ormlite.field.FieldType
import com.j256.ormlite.field.SqlType
import com.j256.ormlite.field.types.DateStringType
import com.j256.ormlite.support.DatabaseResults
import org.joda.time.DateTime
import java.lang.reflect.Field
import java.sql.SQLException

class DateTimePersister private constructor() : DateStringType(SqlType.STRING, arrayOfNulls(0)) {

    override fun javaToSqlArg(fieldType: FieldType?, javaObject: Any): Any? {
        val dateTime = javaObject as DateTime
        return dateTime.toString()
    }

    override fun sqlArgToJava(fieldType: FieldType?, sqlArg: Any, columnPos: Int): Any? {
        return parseFromString(sqlArg as String)
    }

    override fun parseDefaultString(fieldType: FieldType, defaultStr: String): Any? {
        return parseFromString(defaultStr)
    }

    @Throws(SQLException::class)
    override fun resultToSqlArg(fieldType: FieldType?, results: DatabaseResults, columnPos: Int): Any {
        return results.getString(columnPos)
    }

    private fun parseFromString(str: String): DateTime? {
        if (TextUtils.isEmpty(str)) {
            return null
        }

        try {
            return DateTime.parse(str)
        } catch (e: Exception) {
            Log.e(TAG, e.message, e)
        }

        return null
    }

    override fun isValidForField(field: Field): Boolean {
        return field.type == DateTime::class.java
    }

    companion object {

        private val TAG = DateTimePersister::class.java.simpleName

        val singleton = DateTimePersister()
    }
}
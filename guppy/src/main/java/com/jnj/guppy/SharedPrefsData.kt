package com.jnj.guppy

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.jnj.guppy.models.GuppyData

class SharedPrefsData {

    private val GUPPY_DATA_STRING_KEY = "GUPPY_DATA_STRING_KEY"
    private val TAG = SharedPrefsData::class.java.simpleName

    private fun updateGuppyData(context: Context, guppyData: List<GuppyData>) {
        val type = object : TypeToken<List<GuppyData>>() {}.type
        getSharedPrefs(context).edit().putString(GUPPY_DATA_STRING_KEY, getGson().toJson(guppyData, type)).apply()
    }

    fun updateGuppyData(context: Context, guppyData: GuppyData) {
        var data = getGuppyData(context)
        data += guppyData
        updateGuppyData(context, data)
    }

    fun getGuppyData(context: Context): List<GuppyData> {
        val json = getSharedPrefs(context).getString(GUPPY_DATA_STRING_KEY, "")
        var data: List<GuppyData> = mutableListOf()
        if (json == "") {
            return data
        }
        try {
            val type = object : TypeToken<List<GuppyData>>() {}.type
            val result = getGson().fromJson<List<GuppyData>>(json, type)
            if (result != null) {
                data += result
                return data
            }
        } catch (error: Exception) {
            Log.d(TAG, "Error parsing json: " + error.message, error)
            val obj = getGson().fromJson(json, GuppyData::class.java)
            if (obj != null) {
                data += obj
            }
        }
        return data
    }

    private fun getSharedPrefs(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    private fun getGson(): Gson {
        val gsonBuilder = GsonBuilder()
        return gsonBuilder.create()
    }

    @Synchronized
    fun clearGuppyData(context: Context) {
        updateGuppyData(context, ArrayList())
    }

}
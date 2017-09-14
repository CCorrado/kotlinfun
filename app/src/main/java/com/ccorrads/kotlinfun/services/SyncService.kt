package com.ccorrads.kotlinfun.services

import android.app.IntentService
import android.content.Intent
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.models.MealPrep
import com.ccorrads.kotlinfun.network.MealPrepBackendService

class SyncService : IntentService(SyncService::class.java.simpleName) {

    override fun onHandleIntent(intent: Intent?) {
        val mealsToSync = MainApp.component.getMealDbHelper().getMealPrepsForSync()
        if (!mealsToSync.isEmpty()) {
            sendListOfMeals(mealsToSync)
        }
    }

    private fun sendListOfMeals(meals: List<MealPrep>) {
        val service = MealPrepBackendService()
        for (meal in meals) {
            service.sendMealToServer(meal)
        }
    }
}
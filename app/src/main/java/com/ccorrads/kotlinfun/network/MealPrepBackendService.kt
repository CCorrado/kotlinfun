package com.ccorrads.kotlinfun.network

import android.util.Log
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.events.AbstractServerEvent
import com.ccorrads.kotlinfun.events.MealsAvailableEvent
import com.ccorrads.kotlinfun.models.MealPrep
import com.ccorrads.kotlinfun.models.ParentModel
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import java.util.*

class MealPrepBackendService {

    companion object {
        val TAG: String = MealPrepBackendService::class.java.simpleName
    }

    fun sendMealToServer(mealPrep: MealPrep) {
        MainApp.component.getBackendService().sendMeals(Collections.singletonList(mealPrep))
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        mealPrep.needSync = false
                        MainApp.component.getMealDbHelper().saveMealPrep(mealPrep)
                        EventBus.getDefault().post(MealsAvailableEvent(AbstractServerEvent.CALL_SUCCESSFUL))
                    } else {
                        EventBus.getDefault().post(MealsAvailableEvent(AbstractServerEvent.CALL_FAILED))
                    }
                }, { err ->
                    Log.e(TAG, err.message, err)
                    EventBus.getDefault().post(MealsAvailableEvent(AbstractServerEvent.CALL_FAILED))
                })
    }

    fun getMealsFromServer() {
        MainApp.component.getBackendService().getMeals()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe({ response ->
                    if (response.isSuccessful) {
                        saveMeals(meals = response.body() as? List<ParentModel>)
                        EventBus.getDefault().post(MealsAvailableEvent(AbstractServerEvent.CALL_SUCCESSFUL))
                    } else {
                        EventBus.getDefault().post(MealsAvailableEvent(AbstractServerEvent.CALL_FAILED))
                    }
                }, { err ->
                    Log.e(TAG, err.message, err)
                    EventBus.getDefault().post(MealsAvailableEvent(AbstractServerEvent.CALL_FAILED))
                })
    }

    private fun saveMeals(meals: List<ParentModel>?) {
        if (meals == null) {
            return
        }
        for (meal: ParentModel in meals) {
            meal.mealPrep.shouldSync = false
            MainApp.component.getMealDbHelper().saveMealPrep(meal.mealPrep)
        }
    }
}
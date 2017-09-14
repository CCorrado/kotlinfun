package com.ccorrads.kotlinfun.network

import com.ccorrads.kotlinfun.models.MealPrep
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface BackendService {

    @POST("api/v1/meals.json")
    fun sendMeals(@Body mealPrep: List<MealPrep>): Observable<Response<ResponseBody>>

    @GET("api/v1/meals.json")
    fun getMeals(): Observable<Response<ResponseBody>>
}
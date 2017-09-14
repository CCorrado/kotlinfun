package com.ccorrads.kotlinfun.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.R
import com.ccorrads.kotlinfun.adapters.viewholders.MealPrepViewHolder
import com.ccorrads.kotlinfun.models.MealPrep
import com.ccorrads.kotlinfun.utils.DateUtil
import kotlinx.android.synthetic.main.card_view_meal_prep.view.*

open class MealPrepRecyclerAdapter(private var mealList: List<MealPrep>?) : RecyclerView.Adapter<MealPrepViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MealPrepViewHolder {
        val itemView = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_view_meal_prep, viewGroup, false)
        return MealPrepViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MealPrepViewHolder, position: Int) {
        val mealPrep: MealPrep? = mealList?.get(position)
        holder.itemView.card_title.text = DateUtil.formatDateAtTime(MainApp.application.applicationContext, mealPrep?.dateTime)
        holder.itemView.card_ingredient_one.text = mealPrep?.ingredientOne
        holder.itemView.card_ingredient_two.text = mealPrep?.ingredientTwo
        holder.itemView.card_ingredient_three.text = mealPrep?.ingredientThree
        holder.itemView.card_ingredient_four.text = mealPrep?.ingredientFour

        if (mealPrep != null && mealPrep.needSync) {
            holder.itemView.card_sync_icon.visibility = View.VISIBLE
        } else {
            holder.itemView.card_sync_icon.visibility = View.GONE
        }
    }

    fun test(): Int? = if (mealList == null) 0 else mealList?.size

    override fun getItemCount(): Int {
        val meals: List<MealPrep> = mealList ?: return 0
        return meals.size
    }

    fun updateMealList(items: List<MealPrep>?) {
        this.mealList = items
        notifyDataSetChanged()
    }
}
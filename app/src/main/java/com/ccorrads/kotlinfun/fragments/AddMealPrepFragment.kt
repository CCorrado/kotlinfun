package com.ccorrads.kotlinfun.fragments

import android.os.Bundle
import android.view.View
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.R
import com.ccorrads.kotlinfun.models.MealPrep
import kotlinx.android.synthetic.main.fragment_add_meal.*
import org.joda.time.DateTime

class AddMealPrepFragment : BaseFragment() {

    override fun getLayoutResourceId(): Int = R.layout.fragment_add_meal

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        save_button.setOnClickListener { onClickSave() }
    }

    private fun onClickSave() {
        val mealPrep = MealPrep(DateTime.now())
        mealPrep.ingredientOne = edit_text_ingredient_one.text.toString()
        mealPrep.ingredientTwo = edit_text_ingredient_two.text.toString()
        mealPrep.ingredientThree = edit_text_ingredient_three.text.toString()
        mealPrep.ingredientFour = edit_text_ingredient_four.text.toString()
        mealPrep.needSync = true
        mealPrep.id = MainApp.component.getMealDbHelper().getMealPreps().size + 1

        MainApp.component.getMealDbHelper().saveMealPrep(mealPrep)

        //TODO enable this for synchronous calls.
        /*val syncServiceIntent = Intent(context, SyncService::class.java)
        context.startService(syncServiceIntent)*/
        activity?.onBackPressed()
    }
}

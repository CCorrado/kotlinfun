package com.ccorrads.kotlinfun.fragments

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.R
import com.ccorrads.kotlinfun.adapters.MealPrepRecyclerAdapter
import com.ccorrads.kotlinfun.events.AbstractServerEvent
import com.ccorrads.kotlinfun.events.MealsAvailableEvent
import com.ccorrads.kotlinfun.events.NetworkChangedEvent
import com.ccorrads.kotlinfun.network.MealPrepBackendService
import com.ccorrads.kotlinfun.utils.NetworkHelperUtil
import kotlinx.android.synthetic.main.fragment_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : BaseFragment() {

    private lateinit var adapter: MealPrepRecyclerAdapter

    override fun getLayoutResourceId(): Int = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView(fragment_recycler)
        fab.setOnClickListener {
            BaseFragment.replaceFragmentInActivity(R.id.fragment_container,
                    activity as? AppCompatActivity, AddMealPrepFragment(), AddMealPrepFragment::class.java.simpleName)
        }
    }

    private fun setUpRecyclerView(recyclerView: RecyclerView) {
        val layoutManager: RecyclerView.LayoutManager

        recyclerView.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = layoutManager

        adapter = MealPrepRecyclerAdapter(MainApp.component.getMealDbHelper().getMealPreps())
        recyclerView.adapter = adapter
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveItems(event: MealsAvailableEvent) {
        if (event.callStatus == (AbstractServerEvent.CALL_SUCCESSFUL)) {
            adapter.updateMealList(MainApp.component.getMealDbHelper().getMealPreps())
        } else {
            Snackbar.make(fragment_recycler, getString(R.string.failed_fetch), Snackbar.LENGTH_SHORT).show()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetworkChangedEvent(event: NetworkChangedEvent) {
        val snackbar: Snackbar = Snackbar.make(fragment_recycler, getString(R.string.offline), Snackbar.LENGTH_INDEFINITE)
        if (!NetworkHelperUtil.isOnline()) {
            snackbar.show()
        } else {
            snackbar.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        val service = MealPrepBackendService()
        service.getMealsFromServer()
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }
}

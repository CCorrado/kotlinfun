package com.ccorrads.kotlinfun.activities

import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.v7.app.AppCompatActivity
import com.ccorrads.kotlinfun.MainApp
import com.ccorrads.kotlinfun.R
import com.ccorrads.kotlinfun.fragments.BaseFragment
import com.ccorrads.kotlinfun.fragments.MainActivityFragment
import com.ccorrads.kotlinfun.network.BackendService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        var backend: BackendService = MainApp.component.getBackendService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        BaseFragment.replaceFragmentNotInBackstack(R.id.fragment_container, this,
                MainActivityFragment(), MainActivityFragment::class.java.simpleName)
    }

    @VisibleForTesting
    fun getBackend(): BackendService {
        return com.ccorrads.kotlinfun.activities.MainActivity.Companion.backend
    }

}

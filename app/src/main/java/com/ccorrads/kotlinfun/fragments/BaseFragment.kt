package com.ccorrads.kotlinfun.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment : Fragment() {

    /**
     * the layout resource id for this fragment.
     *
     * @return int layout resource
     */
    protected abstract fun getLayoutResourceId(): Int

    /**
     * covers off common fragment initialization tasks such as creating the content view & binding the sub-views
     * sub classes should implement [.onViewCreated]
     */
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(getLayoutResourceId(), container, false)
        setHasOptionsMenu(true)
        retainInstance = true
        return view
    }

    companion object {

        /**
         * Use this method when you are replacing existing fragments in an activity.
         *
         * @param containerId   -- the id of the current view container
         * @param activity      -- the activity hosting the current view
         * @param fragment      -- the fragment to replace the current view with.
         * @param transactionId -- the simple name of the new fragment. [class.getSimpleName()][Fragment]
         */
        fun replaceFragmentInActivity(containerId: Int, activity: AppCompatActivity?, fragment: Fragment, transactionId: String) {
            activity?.supportFragmentManager?.beginTransaction()
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.replace(containerId, fragment, transactionId)
                    ?.addToBackStack(transactionId)
                    ?.commit()
        }

        /**
         * Use this method when you are starting a new activity with the first fragment.
         *
         * @param containerId   -- the id of the current view container
         * @param activity      -- the activity hosting the current view
         * @param fragment      -- the fragment to replace the current view with.
         * @param transactionId -- the simple name of the new fragment. [class.getSimpleName()][Fragment]
         */
        fun replaceFragmentNotInBackstack(containerId: Int, activity: AppCompatActivity, fragment: Fragment, transactionId: String) {
            activity.supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(containerId, fragment, transactionId)
                    .commit()
        }
    }
}
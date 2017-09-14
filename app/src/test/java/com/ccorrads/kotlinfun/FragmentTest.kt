package com.ccorrads.kotlinfun

import com.ccorrads.kotlinfun.fragments.MainActivityFragment
import junit.framework.TestCase
import kotlinx.android.synthetic.main.fragment_main.*

class FragmentTest : TestCase() {

    var fragment = MainActivityFragment()

    var activity = com.ccorrads.kotlinfun.activities.MainActivity()

    override fun setUp() {
        super.setUp()
    }

    override fun tearDown() {
        super.tearDown()
    }

    fun testFragmentFab() {
        assert(fragment.isAdded && activity.fab.isActivated)
    }

    fun testDaggerModule() {
        assertNotNull(activity.getBackend())
    }
}
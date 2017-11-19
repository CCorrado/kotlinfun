package com.jnj.guppy.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.jnj.guppy.BuildConfig
import com.jnj.guppy.R
import com.jnj.guppy.SharedPrefsData
import com.jnj.guppy.adapters.GuppyRecyclerAdapter

class GuppyDialogFragment : DialogFragment() {
    private lateinit var adapter: GuppyRecyclerAdapter
    private val sharedPrefs = SharedPrefsData()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)

        val view = activity?.layoutInflater?.inflate(R.layout.guppy_dialog, null)
        dialog.setContentView(view)

        // needs to be called after you set the content view
        dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        if (view != null) {
            onViewCreated(view, savedInstanceState)

            dialog.findViewById<TextView>(R.id.guppy_version).text = getString(R.string.guppy_version_string,
                    BuildConfig.VERSION_NAME + "+" + BuildConfig.VERSION_CODE)

            dialog.findViewById<ImageView>(R.id.clear_all_guppies).setOnClickListener {
                if (context != null) {
                    sharedPrefs.clearGuppyData(context!!)
                }
                this.adapter.updateData(ArrayList())
            }

            configureRecyclerView(dialog.findViewById(R.id.guppy_recycler_view))
        }
        return dialog
    }

    private fun configureRecyclerView(guppyRv: RecyclerView) {
        val sharedPrefsData = SharedPrefsData()
        if (context != null) {
            val data = sharedPrefsData.getGuppyData(context!!)
            guppyRv.layoutManager = LinearLayoutManager(activity)
            this.adapter = GuppyRecyclerAdapter(activity as AppCompatActivity, data)
            guppyRv.adapter = this.adapter
        }
    }
}
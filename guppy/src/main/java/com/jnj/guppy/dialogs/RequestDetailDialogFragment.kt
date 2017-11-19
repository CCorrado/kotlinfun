package com.jnj.guppy.dialogs

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.jnj.guppy.R
import com.jnj.guppy.models.GuppyData
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class RequestDetailDialogFragment : DialogFragment() {

    private val TAG = RequestDetailDialogFragment::class.java.simpleName

    private lateinit var guppyData: GuppyData

    fun newInstance(data: GuppyData): RequestDetailDialogFragment {
        val dialogFragment = RequestDetailDialogFragment()
        dialogFragment.guppyData = data
        return dialogFragment
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(activity)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)

        if (activity != null) {
            val view = activity!!.layoutInflater.inflate(R.layout.request_detail_dialog, null)
            dialog.setContentView(view)

            // needs to be called after you set the content view
            dialog.window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

            dialog.findViewById<TextView>(R.id.request_url).text = guppyData.host
            dialog.findViewById<TextView>(R.id.request_body).text = guppyData.requestBody

            dialog.findViewById<TextView>(R.id.request_headers).text = TextUtils.join("\n",
                    guppyData.requestHeaders)

            dialog.findViewById<TextView>(R.id.request_type).text = guppyData.requestType
            dialog.findViewById<TextView>(R.id.response_result).text = guppyData.responseResult
            dialog.findViewById<TextView>(R.id.response_body).text = formatJson(guppyData.responseBody)

            onViewCreated(view, savedInstanceState)
        }
        return dialog
    }

    private fun formatJson(json: String?): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        if (!TextUtils.isEmpty(json)) {
            try {
                val jsonObj = JSONObject(json)
                return gson.toJson(jsonObj)
            } catch (err: JSONException) {
                Log.e(TAG, err.message, err)
            }
            try {
                val jsonArr = JSONArray(json)
                return gson.toJson(jsonArr)
            } catch (err: JSONException) {
                Log.e(TAG, err.message, err)
            }
        }
        return gson.toJson(json)
    }
}
package com.jnj.guppy.adapters

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.jnj.guppy.R
import com.jnj.guppy.dialogs.RequestDetailDialogFragment
import com.jnj.guppy.models.GuppyData

class GuppyRecyclerAdapter(private val activity: AppCompatActivity, var data: List<GuppyData>) : RecyclerView.Adapter<GuppyViewHolder>() {

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: GuppyViewHolder, position: Int) {
        holder.itemView?.findViewById<TextView>(R.id.request_type)?.text = data[position].requestType
        holder.itemView?.findViewById<TextView>(R.id.request_host_url)?.text = data[position].host
        holder.itemView?.setOnClickListener {
            val detailFragment = RequestDetailDialogFragment().newInstance(data[position])
            detailFragment.show(activity.supportFragmentManager, "RequestDetailDialogFragment")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuppyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.card_view_guppy_item,
                parent, false)
        return GuppyViewHolder(itemView, data)
    }

    fun updateData(data: List<GuppyData>) {
        this.data = data
        notifyDataSetChanged()
    }
}
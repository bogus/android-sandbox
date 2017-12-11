package net.bogus.services.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import net.bogus.services.R
import net.bogus.services.model.Color

/**
 * Created by burak on 12/10/17.
 */
class RVAdapter(val strings:List<String>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(strings[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.rv_rows, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return strings.size
    }

    class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

        fun bind(value:String) {
            val textView = itemView.findViewById<TextView>(R.id.color_name)
            textView.text = value
        }

    }
}
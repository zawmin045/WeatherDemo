package com.example.weatherdemo.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherdemo.R
import com.example.weatherdemo.model.MyHourly

class MyHourlyAdapter(context: Context, hrLists: List<MyHourly>): RecyclerView.Adapter<MyHourlyAdapter.MyHourlyViewHolder>() {

    var context: Context = context
    var hrLists: List<MyHourly> = hrLists

    inner class MyHourlyViewHolder(itemView: View) : RecyclerView.ViewHolder(
        itemView
    ) {
        val txtHr = itemView.findViewById<TextView>(R.id.txtHr)
        val txtTmp = itemView.findViewById<TextView>(R.id.txtHrTmp)
        val rain = itemView.findViewById<TextView>(R.id.txtHrRain)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyHourlyAdapter.MyHourlyViewHolder {
        val itemView = View.inflate(parent.context,R.layout.hourly_item, null)
        return MyHourlyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyHourlyAdapter.MyHourlyViewHolder,
        position: Int
    ) {
        val hr = hrLists[position]
        holder.txtHr.text = hr.time.toString()
        holder.txtTmp.text = hr.tmp.toString()
        holder.rain.text = hr.rain.toString()+"%"
    }

    override fun getItemCount(): Int {
        return hrLists.size
    }


}
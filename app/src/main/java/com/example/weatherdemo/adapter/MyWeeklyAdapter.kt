package com.example.weatherdemo.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherdemo.R
import com.example.weatherdemo.model.MyHourly
import com.example.weatherdemo.model.MyWeekly
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.get
import kotlin.toString

class MyWeeklyAdapter (context: Context, wkList: List<MyWeekly>): RecyclerView.Adapter<MyWeeklyAdapter.MyWeeklyViewHolder>() {
    var context: Context = context
    var wkList: List<MyWeekly> = wkList
    inner class MyWeeklyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var txttime = itemView.findViewById<TextView>(R.id.txtDate)
        var txtMaxTmp = itemView.findViewById<TextView>(R.id.txtMaxTmp)
        var txtMinTmp = itemView.findViewById<TextView>(R.id.txtMinTmp)
        var txtRain = itemView.findViewById<TextView>(R.id.txtRain)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyWeeklyAdapter.MyWeeklyViewHolder {
        val itemView = View.inflate(parent.context,R.layout.weekly_item, null)
        return MyWeeklyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyWeeklyAdapter.MyWeeklyViewHolder,
        position: Int
    ) {
        val wkItem = wkList[position]
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val currentDate = dateFormat.format(Date())

        val dayText = if (wkItem.time == currentDate) {
            "Today"
        } else {
            val date = dateFormat.parse(wkItem.time)
            dayFormat.format(date ?: Date())
        }

        holder.txttime.text = dayText
        holder.txtMaxTmp.text = wkItem.tmpMax.toString() + "°C"
        holder.txtMinTmp.text = wkItem.tmpMin.toString() + "°C"
        holder.txtRain.text = wkItem.rainSum.toString() + "%"
    }

    override fun getItemCount(): Int {
        return  wkList.size
    }
}
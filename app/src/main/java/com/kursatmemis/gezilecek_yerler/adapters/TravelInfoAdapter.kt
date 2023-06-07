package com.kursatmemis.gezilecek_yerler.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gezilecek_yerler.R
import com.kursatmemis.gezilecek_yerler.models.TravelInfo

class TravelInfoAdapter(context: Context, val travelInfos: List<TravelInfo>) :
    ArrayAdapter<TravelInfo>(context, R.layout.list_view_item, travelInfos) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = LayoutInflater.from(context)
        val viewHolder: ViewHolder
        val listViewItem: View?

        if (convertView == null) {
            listViewItem = layoutInflater.inflate(R.layout.list_view_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.titleTextView = listViewItem.findViewById<TextView>(R.id.titleTextView)
            viewHolder.cityTextView = listViewItem.findViewById<TextView>(R.id.cityTextView)
            viewHolder.noteTextView = listViewItem.findViewById<TextView>(R.id.noteTextView)
            listViewItem.tag = viewHolder
        } else {
            listViewItem = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val travelInfo = travelInfos[position]
        viewHolder.titleTextView.text = travelInfo.title
        viewHolder.cityTextView.text = travelInfo.city
        viewHolder.noteTextView.text = travelInfo.note

        return listViewItem!!
    }

    class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var cityTextView: TextView
        lateinit var noteTextView: TextView
    }
}
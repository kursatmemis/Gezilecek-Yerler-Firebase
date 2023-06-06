package com.kursatmemis.gezilecek_yerler.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.gezilecek_yerler.R
import com.kursatmemis.gezilecek_yerler.models.Info

class InfoAdapter(context: Context, val infos: List<Info>) :
    ArrayAdapter<Info>(context, R.layout.list_view_item, infos) {
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

        val info = infos[position]
        viewHolder.titleTextView.text = info.title
        viewHolder.cityTextView.text = info.city
        viewHolder.noteTextView.text = info.note

        return listViewItem!!
    }

    class ViewHolder {
        lateinit var titleTextView: TextView
        lateinit var cityTextView: TextView
        lateinit var noteTextView: TextView
    }
}
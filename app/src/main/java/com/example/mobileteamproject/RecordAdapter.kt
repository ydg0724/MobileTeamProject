package com.example.mobileteamproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class RecordAdapter(context: Context, records: MutableList<String>) :
    ArrayAdapter<String>(context, 0, records) {

    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val mRecords: MutableList<String> = records

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_record, parent, false)
            holder = ViewHolder()
            holder.recordTextView = convertView.findViewById(R.id.recordTextView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val record = mRecords[position]
        holder.recordTextView.text = record

        return convertView!!
    }

    fun addRecord(record: String) {
        mRecords.add(record)
        notifyDataSetChanged()
    }

    private class ViewHolder {
        lateinit var recordTextView: TextView
    }
}
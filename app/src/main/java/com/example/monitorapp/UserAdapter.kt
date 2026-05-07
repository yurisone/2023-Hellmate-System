package com.example.monitorapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class UserAdapter(val context: Context, val UserList: ArrayList<User>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.work_status, null)

        val name = view.findViewById<TextView>(R.id.value_name)
        val namevalue = view.findViewById<TextView>(R.id.value)
        val shape = view.findViewById<ImageView>(R.id.st_shape)

        val user = UserList[position]

        name.text = user.name
        namevalue.text = user.name_value
        shape.setImageResource(user.shape_status)

        return view
    }

    override fun getCount(): Int {
        return UserList.size
    }

    override fun getItem(position: Int): Any {
        return UserList[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

}
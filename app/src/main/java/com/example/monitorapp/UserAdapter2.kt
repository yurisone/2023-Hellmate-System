package com.example.monitorapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class UserAdapter2 (val context: Context, val UserList2: ArrayList<User2>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.worker_list, null)

        val name = view.findViewById<TextView>(R.id.name)
        val shape = view.findViewById<ImageView>(R.id.st_shape)

        val user = UserList2[position]

        name.text = user.name
        shape.setImageResource(user.shape_status)

        return view
    }

    override fun getCount(): Int {
        return UserList2.size
    }

    override fun getItem(position: Int): Any {
        return UserList2[position]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

}
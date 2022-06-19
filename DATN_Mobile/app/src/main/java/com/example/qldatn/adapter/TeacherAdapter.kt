package com.example.qldatn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qldatn.R
import com.example.qldatn.models.Teacher

internal class TeacherAdapter(private var itemsList: ArrayList<Teacher>,var context:Context) :
    RecyclerView.Adapter<TeacherAdapter.MyViewHolder>(),View.OnClickListener {
    internal inner class MyViewHolder(view: View,listener: onItemClicklistener) : RecyclerView.ViewHolder(view) {
        var itemTextView: TextView = view.findViewById(R.id.tv_itemTeacher_name)
        var img_itemTeacher_avt: ImageView = view.findViewById(R.id.img_itemTeacher_avt)
        init{
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var myListener:onItemClicklistener

    interface onItemClicklistener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClicklistener){
        myListener=listener
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemteacher, parent, false)
        return MyViewHolder(itemView,myListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.itemTextView.text = item.teacher_name
        item.avt?.let {
            if(!item.avt.isEmpty()){
                Glide.with(context).load(item.avt).into(holder.img_itemTeacher_avt)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onClick(p0: View?) {

    }
}
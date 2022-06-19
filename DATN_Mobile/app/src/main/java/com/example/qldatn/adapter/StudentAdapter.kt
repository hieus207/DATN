package com.example.qldatn.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.R
import com.example.qldatn.models.Proposal
import com.example.qldatn.models.Teacher
//                *********
internal class StudentAdapter(private var itemsList: ArrayList<Proposal>) :
    RecyclerView.Adapter<StudentAdapter.MyViewHolder>(),View.OnClickListener {
    internal inner class MyViewHolder(view: View,listener: onItemClicklistener) : RecyclerView.ViewHolder(view) {

        var tv_itemProposal_teacherName: TextView = view.findViewById(R.id.tv_itemProposal_teacherName)
        var tv_itemProposal_studentName: TextView = view.findViewById(R.id.tv_itemProposal_studentName)
        var tv_itemProposal_projectName: TextView = view.findViewById(R.id.tv_itemProposal_projectName)
        var ll_itemProposal_bg: LinearLayout = view.findViewById(R.id.ll_itemProposal_bg)
        init{
            view.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    private lateinit var myListener:onItemClicklistener
    lateinit var ct: Context

    interface onItemClicklistener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClicklistener){
        myListener=listener
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
//                *********
            .inflate(R.layout.itemproposal, parent, false)
        ct = parent.context
        return MyViewHolder(itemView,myListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tv_itemProposal_teacherName.text = "Giảng viên:" + item.teacher_name
        holder.tv_itemProposal_studentName.text = "Sinh viên: " + item.student_name
        holder.tv_itemProposal_projectName.text = "Dự án: " + item.project_name
        var bg_color = R.color._bg_small_blue
        holder.ll_itemProposal_bg.setBackgroundColor(ContextCompat.getColor(ct,bg_color))
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onClick(p0: View?) {

    }
}
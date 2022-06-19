package com.example.qldatn.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.R
import com.example.qldatn.api.response.ResHomeInfo
import com.example.qldatn.models.Teacher


internal class HomeInfoAdapter(private var itemsList: ArrayList<ResHomeInfo>) :
    RecyclerView.Adapter<HomeInfoAdapter.MyViewHolder>(), View.OnClickListener {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_ithome_stdName:TextView = view.findViewById(R.id.tv_ithome_stdName)
        var tv_ithome_prjName:TextView = view.findViewById(R.id.tv_ithome_prjName)
        var tv_ithome_reportDate:TextView = view.findViewById(R.id.tv_ithome_reportDate)
        var tv_ithome_reportContent:TextView = view.findViewById(R.id.tv_ithome_reportContent)
    }

    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_home_project, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tv_ithome_stdName.text = item.student_name
        holder.tv_ithome_prjName.text = item.project_name
        holder.tv_ithome_reportDate.text = item.reportDate
        holder.tv_ithome_reportContent.text = item.reportContent
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onClick(p0: View?) {

    }
}
package com.example.qldatn.adapter
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.R
import com.example.qldatn.models.Schedule

internal class ScheduleAdapter(private var itemsList: ArrayList<Schedule>) :
    RecyclerView.Adapter<ScheduleAdapter.MyViewHolder>(),View.OnClickListener {
    internal inner class MyViewHolder(view: View,listener: onItemClicklistener) : RecyclerView.ViewHolder(view) {
        var tv_itemCalen_date: TextView = view.findViewById(R.id.tv_itemCalen_date)
        var tv_itemCalen_content: TextView = view.findViewById(R.id.tv_itemCalen_content)
        var ll_itemCalen_bg:ConstraintLayout = view.findViewById(R.id.ll_itemCalen_bg)
        var btn_edit:ImageButton = view.findViewById(R.id.btn_itemCalen_edit)
        var btn_delete:ImageButton = view.findViewById(R.id.btn_itemCalen_del)
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
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClicklistener){
        myListener=listener
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.itemdate, parent, false)
        ct = parent.context
        return MyViewHolder(itemView,myListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = itemsList[position]
        holder.tv_itemCalen_date.text = item.reportDate
        holder.tv_itemCalen_content.text = item.reportContent
        holder.btn_edit.setOnClickListener {
            myListener.onEditClick(position)
        }

        holder.btn_delete.setOnClickListener {
            myListener.onDeleteClick(position)
        }
        var bg_color = R.color._bg_grey
        if(item.status=="1")
            bg_color = R.color._bg_yellow
        else
            if(item.status=="2"){
                holder.btn_edit.isVisible = false
                holder.btn_delete.isVisible = false
                bg_color = R.color._bg_small_blue
            }
        else
            if(item.status=="3")
                bg_color = R.color._bg_red

        holder.ll_itemCalen_bg.setBackgroundColor(ContextCompat.getColor(ct,bg_color))
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onClick(p0: View?) {

    }
}
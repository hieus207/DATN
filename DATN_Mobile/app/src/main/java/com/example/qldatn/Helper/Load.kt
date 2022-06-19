package com.example.qldatn.Helper

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Handler
import com.example.qldatn.MyApplication
import com.example.qldatn.R
import java.util.*
import kotlin.concurrent.schedule

class Load {
    companion object dialog{
        var alert:AlertDialog? = null
        fun show(context: Context,activity: Activity){
            val inflater = activity!!.layoutInflater;
            val dialogView = inflater.inflate(R.layout.dialog_progress_bar, null)
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setView(dialogView)
            dialogBuilder.setCancelable(false)
            alert = dialogBuilder.create()
            alert!!.show()
        }

        fun hide(){
            if(alert!=null)
                alert!!.hide()

        }




    }
}
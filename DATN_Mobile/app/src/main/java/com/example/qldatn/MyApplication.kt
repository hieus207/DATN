package com.example.qldatn

import android.app.Activity
import android.app.Application
import android.content.Context

class MyApplication: Application() {
    companion object {
        var id:Int=-1
        var accessToken:String=""
        var _context: Context? = null
        var inPhase:Boolean=false
    }

    override fun onCreate() {
        super.onCreate()
        _context=baseContext
        // initialization code here
    }
}
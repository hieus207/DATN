package com.example.qldatn.models

import java.io.Serializable

class Project(val id: Int,val name:String,val quest:String, val solution:String, val tech:String, val func:String, val result:String, val platforms:String, val student_id:Int, val phase_id:Int,val link:String? ) :Serializable {
}
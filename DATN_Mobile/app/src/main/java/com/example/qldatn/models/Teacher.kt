package com.example.qldatn.models

import java.io.Serializable

class Teacher(
              val teacher_id:Int,
              val teacher_name:String,
              val email:String,
              val birthday:String,
              val phone:String,
              val topic:String,
              val platforms:String,
              val quota:Int,
              val available_quota:Int,
              val major:String,
              val avt:String,
              val major_id:Int) : Serializable {
}
package com.example.qldatn.models

class Schedule(val id:Int,
               val student_id:Int,
               val teacher_id:Int,
               val phase_id:Int,
               val reportDate:String,
               val reportContent:String,
               val status:String,
               val refuseContent:String,
               val student_name:String,
              ) {
}
package com.example.qldatn.api.response

import com.example.qldatn.models.Project
import com.example.qldatn.models.Schedule
import com.example.qldatn.models.Teacher

class ResHomeProject(val project: Project,val teacher: Teacher,val schedule: Schedule,val phase:Boolean?) {
}
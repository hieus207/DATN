package com.example.qldatn.api

import com.example.qldatn.api.response.*
import com.example.qldatn.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/account/login")
    fun login(@Field("username") username:String,
              @Field("password") password:String):Call<ResLogin>

    @FormUrlEncoded
    @POST("api/account/changePwd")
    fun changePwd(
              @Field("id") id:Int,
              @Field("oldPwd") oldPwd:String,
              @Field("newPwd") newPwd:String,
              @Field("token") token: String
    ):Call<Boolean>

    @FormUrlEncoded
    @POST("api/project/getProject")
    fun getProject(@Field("id") id:Int, @Field("token") token:String):Call<ResProject>

    @GET("api/getHomeProject")
    fun getHomeProject(@Query("student_id") student_id: Int):Call<ResHomeProject>

    @GET("api/getHomeInfo")
    fun getHomeInfo(@Query("teacher_id") teacher_id: Int):Call<ArrayList<ResHomeInfo>>

    @GET("api/getPhaseTeacher")
    fun getPhaseTeacher(@Query("teacher_id") teacher_id: Int):Call<Int>

    @GET("api/getPhase")
    fun getPhase():Call<Phase>

    @GET("api/getStage")
    fun getStage():Call<Int>

    @FormUrlEncoded
    @POST("api/submit")
    fun submitProject(
        @Field("student_id") student_id: Int,
        @Field("link") link:String
    ):Call<Boolean>

    @FormUrlEncoded
    @POST("api/project")
    fun createProject(
        @Field("name") name:String,
        @Field("quest") quest:String,
        @Field("solution") solution:String,
        @Field("tech") tech:String,
        @Field("func") func:String,
        @Field("result") result:String,
        @Field("student_id") student_id:Int,
        @Field("platforms[]") platforms:ArrayList<Int>,
        @Field("token") token:String):Call<Boolean>

    @FormUrlEncoded
    @PUT("api/project/{id}")
    fun updateProject(
        @Path("id") id:Int,
        @Field("name") name:String,
        @Field("quest") quest:String,
        @Field("solution") solution:String,
        @Field("tech") tech:String,
        @Field("func") func:String,
        @Field("result") result:String,
        @Field("student_id") student_id:Int,
        @Field("platforms[]") platforms:ArrayList<Int>,
        @Field("token") token:String):Call<Boolean>

    @FormUrlEncoded
    @POST("api/proposal")
    fun applyProject(
        @Field("student_id") student_id:Int,
        @Field("teacher_id") teacher_id:Int,
        @Field("project_id") project_id:Int,
        @Field("token") token:String
    ):Call<Boolean>

    @FormUrlEncoded
    @POST("api/proposal/accept")
    fun acceptProposal(
        @Field("student_id") student_id:Int,
        @Field("teacher_id") teacher_id:Int,
        @Field("project_id") project_id:Int,
        @Field("type") type:String,
        @Field("token") token:String
    ):Call<Boolean>


    @GET("api/proposal")
    fun getProposal(@Query("id") id:Int,@Query("type") type:String,@Query("status") status:Int):Call<ArrayList<Proposal>>

    @GET("api/getStudentReview")
    fun getStudentReview(@Query("teacher_id") id:Int):Call<ArrayList<Proposal>>

    @GET("api/getTeacherCouncil")
    fun getTeacherCouncil(@Query("teacher_id") id:Int):Call<ArrayList<Proposal>>

    @GET("api/student/{id}")
    fun getStudent(
        @Path("id") id:Int
    ):Call<Student>

    @Multipart
    @POST("api/student/{id}?_method=PUT")
    fun updateStudent(
        @Path("id") id:Int,
        @Part("student_name") student_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("cv") cv: RequestBody,
        @Part("major_id") major_id: RequestBody,
        @Part avt:MultipartBody.Part,
        @Part("token") token: RequestBody):Call<Boolean>




    @FormUrlEncoded
    @POST("api/schedule")
    fun createSchedule(
        @Field("student_id") student_id:Int,
        @Field("reportDate") reportDate:String,
        @Field("reportContent") reportContent:String,
        @Field("token") token:String
    ):Call<Boolean>


    @FormUrlEncoded
    @POST("api/schedule/proposeSchedule")
    fun proposeSchedule(
        @Field("student_id") student_id:Int,
        @Field("token") token:String
    ):Call<Boolean>

    @FormUrlEncoded
    @PUT("api/schedule/{id}")
    fun updateSchedule(
        @Path("id") id:Int,
        @Field("reportDate") name:String,
        @Field("reportContent") reportContent:String,
        @Field("token") token:String):Call<Boolean>

    @FormUrlEncoded
    @PUT("api/schedule/teacherUpdate/{id}")
    fun updateTeacherSchedule(
        @Path("id") id:Int,
        @Field("status") status: Int,
        @Field("refuseContent") refuseContent:String,
        @Field("token") token:String):Call<Boolean>

    @FormUrlEncoded
    @POST("api/schedule/acceptAllSchedule")
    fun acceptAllSchedule(
        @Field("student_id") student_id:Int,
        @Field("teacher_id") teacher_id:Int,
        @Field("refuseContent") refuseContent:String,
        @Field("token") token:String):Call<Boolean>

    @FormUrlEncoded
    @POST("api/schedule/refuseAllSchedule")
    fun refuseAllSchedule(
        @Field("student_id") student_id:Int,
        @Field("teacher_id") teacher_id:Int,
        @Field("refuseContent") refuseContent:String,
        @Field("token") token:String):Call<Boolean>


    @GET("api/teacherSchedule")
    fun getTeacherSchedule(@Query("teacher_id") teacher_id:Int):Call<ArrayList<Schedule>>

    @GET("api/getPhaseTime")
    fun getPhaseTime():Call<Phase>

    @GET("api/schedule")
    fun getSchedule(@Query("student_id") student_id:Int,@Query("type") type:String):Call<ArrayList<Schedule>>

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "api/schedule/{id}", hasBody = true)
    fun deleteSchedule(
        @Path("id") id:Int,
        @Field("token") token:String
    ):Call<Boolean>



    @Multipart
    @POST("api/teacher/{id}?_method=PUT")
    fun updateTeacher(
        @Path("id") id:Int,
        @Part("teacher_name") teacher_name:RequestBody,
        @Part("email") email:RequestBody,
        @Part("birthday") birthday:RequestBody,
        @Part("phone") phone:RequestBody,
        @Part("topic") topic:RequestBody,
        @Part avt:MultipartBody.Part,
        @Part("major_id") major_id:RequestBody,
        @Part("platforms[]") platforms:ArrayList<RequestBody>,
        @Part("token") token:RequestBody):Call<Boolean>

    @GET("api/teacher/{id}")
    fun getTeacher(
        @Path("id") id:Int
    ):Call<Teacher>

    @GET("api/applyProject")
    fun confirmApply(@Query("id") id:Int):Call<ResApplyProject>

    @GET("api/major")
    fun getAllMajor():Call<ArrayList<Major>>

    @GET("api/teacher")
    fun getAllTeacher():Call<ArrayList<Teacher>>

    @GET("api/platform")
    fun getAllPlatform():Call<ArrayList<Platform>>

    companion object {

        var BASE_URL = "http://192.168.1.167:3000/"

        fun create() : ApiService {

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiService::class.java)

        }
    }
}
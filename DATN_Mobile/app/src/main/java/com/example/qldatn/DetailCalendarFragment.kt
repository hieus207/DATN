package com.example.qldatn

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.Helper.Message
import com.example.qldatn.adapter.ScheduleAdapter
import com.example.qldatn.adapter.TeacherAdapter
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Phase
import com.example.qldatn.models.Schedule
import com.example.qldatn.models.Teacher
import com.google.android.material.datepicker.MaterialDatePicker.Builder.datePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DetailCalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    var listSchedule = ArrayList<Schedule>()
    lateinit var rcv_dtcalendar:RecyclerView
    private lateinit var scheduleAdapter: ScheduleAdapter
    lateinit var btn_dtcalendar_add:ImageButton
    lateinit var btn_calendar_apply:ImageButton
    lateinit var btn_dtcalendar_back:ImageView
    lateinit var phase:Phase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_detail_calendar, container, false)
        rcv_dtcalendar = v.findViewById(R.id.rcv_dtcalendar)
        btn_dtcalendar_add = v.findViewById(R.id.btn_dtcalendar_add);
        btn_calendar_apply = v.findViewById(R.id.btn_calendar_apply)
        btn_dtcalendar_back = v.findViewById(R.id.btn_dtcalendar_back)

        btn_dtcalendar_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btn_dtcalendar_add.setOnClickListener {
            showCreatDialog()
        }
        btn_calendar_apply.setOnClickListener {
            showApplyDialog()
        }
        initSchedule();
        getPhaseTime();
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailCalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getPhaseTime(){
        ApiService.create().getPhaseTime().enqueue(
            object : Callback<Phase>{
                override fun onResponse(call: Call<Phase>, response: Response<Phase>) {
                    if(response.isSuccessful){
                        phase = response.body()!!
                    }
                }
                override fun onFailure(call: Call<Phase>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
    fun initSchedule(){
        ApiService.create().getSchedule(MyApplication.id,"all").enqueue(
            object :Callback<ArrayList<Schedule>>{
                override fun onResponse(call: Call<ArrayList<Schedule>>,response: Response<ArrayList<Schedule>>) {
                    if(response.isSuccessful){
                        listSchedule = response.body()!!
                        scheduleAdapter= ScheduleAdapter(listSchedule)
                        val layoutManager = LinearLayoutManager(context)
                        rcv_dtcalendar.layoutManager = layoutManager
                        rcv_dtcalendar.adapter = scheduleAdapter
                        scheduleAdapter.setOnItemClickListener(
                            object : ScheduleAdapter.onItemClicklistener{
                                override fun onItemClick(position: Int) {
                                    if(listSchedule[position].status=="3"){
                                        val dialogBuilder = AlertDialog.Builder(context)
                                        dialogBuilder.setMessage(listSchedule[position].refuseContent)
                                        dialogBuilder.setCancelable(true)
                                        dialogBuilder.setNegativeButton("Đồng ý", DialogInterface.OnClickListener {
                                                dialog, id -> dialog.cancel()
                                        })
                                        val alert = dialogBuilder.create()
                                        alert.setTitle("Lý do từ chối")
                                        alert.show()
                                    }
                                }

                                override fun onEditClick(position: Int) {
                                    showEditDialog(listSchedule[position].id,listSchedule[position].reportDate,listSchedule[position].reportContent)
                                }

                                override fun onDeleteClick(position: Int) {
                                    showDeleteDialog(listSchedule[position].id)
                                }
                            }
                        )
                        scheduleAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Schedule>>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )

    }

    fun showCreatDialog(){
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.datepicker, null)
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        val datepicker:DatePicker =  dialogView.findViewById(R.id.dpick_calendar)
        val edt_calender_content:EditText = dialogView.findViewById(R.id.edt_calender_content)
        val startDate = if(SimpleDateFormat("yyyy-MM-dd").parse(phase.startDate).time>System.currentTimeMillis()) SimpleDateFormat("yyyy-MM-dd").parse(phase.startDate).time else System.currentTimeMillis()
        val endDate = SimpleDateFormat("yyyy-MM-dd").parse(phase.endDate).time
        datepicker.minDate = startDate
        datepicker.maxDate = endDate

        dialogBuilder.setPositiveButton("OK"){_,_->
            val day: Int = datepicker.getDayOfMonth()
            val month: Int = datepicker.getMonth()+1
            val year: Int = datepicker.getYear()
            val selectedDate = "${year}-${if(month>9) month else "0"+month}-${if(day>9) day else "0"+day}"
            createSchedule(selectedDate,edt_calender_content.text.toString())

        }
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()

        alert.show()
    }

    fun createSchedule(date:String,content:String){
        ApiService.create().createSchedule(
            MyApplication.id,
            date,
            content,
            MyApplication.accessToken
        ).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        if(response.body()!!){
                            Toast.makeText(context,"Tạo lịch báo cáo thành công",Toast.LENGTH_SHORT).show()
                            initSchedule()
                        }

                        else
                            Toast.makeText(context,"Tạo lịch báo cáo thất bại, không được để nội dung rỗng",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun showEditDialog(schedule_id:Int,date:String, content:String){
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.datepicker, null)
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        val datepicker:DatePicker =  dialogView.findViewById(R.id.dpick_calendar)
        val edt_calender_content:EditText = dialogView.findViewById(R.id.edt_calender_content)
        datepicker.init(date.slice(0..3).toInt(),date.slice(5..6).toInt()-1,date.slice(8..9).toInt(),null)
        edt_calender_content.setText(content)
        val startDate = if(SimpleDateFormat("yyyy-MM-dd").parse(phase.startDate).time>System.currentTimeMillis()) SimpleDateFormat("yyyy-MM-dd").parse(phase.startDate).time else System.currentTimeMillis()
        val endDate = SimpleDateFormat("yyyy-MM-dd").parse(phase.endDate).time
        datepicker.minDate = startDate
        datepicker.maxDate = endDate

        dialogBuilder.setPositiveButton("OK"){_,_->
            val day: Int = datepicker.getDayOfMonth()
            val month: Int = datepicker.getMonth()+1
            val year: Int = datepicker.getYear()
            val selectedDate = "${year}-${if(month>9) month else "0"+month}-${if(day>9) day else "0"+day}"
            updateSchedule(schedule_id,selectedDate,edt_calender_content.text.toString())

        }
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()

        alert.show()
    }

    fun updateSchedule(id:Int,date:String,content:String){
        ApiService.create().updateSchedule(
            id,
            date,
            content,
            MyApplication.accessToken
        ).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        if(response.body()!!){
                            Message.msg("Cập nhật lịch báo cáo thành công")
                            initSchedule()
                        }else
                            Message.msg("Cập nhật lịch báo cáo thất bại, không được để nội dung rỗng")
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun showDeleteDialog(id:Int){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Bạn có chắc muốn xoá ngày báo cáo này?")
        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton("Chắc chắn"){_,_->
            deleteSchedule(id);
        }
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()
        alert.setTitle("Xác nhận xoá")
        alert.show()
    }

    fun deleteSchedule(id:Int){
        ApiService.create().deleteSchedule(id,MyApplication.accessToken).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        if(response.body()!!){
                            Message.msg("Xoá lịch báo cáo thành công")
                            initSchedule()
                        }else
                            Toast.makeText(context,"Xoá lịch báo cáo thất bại",Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun showApplyDialog(){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Bạn có chắc muốn đề xuất tất cả lịch báo cáo?")
        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton("Chắc chắn"){_,_->
            applySchedule()
        }
        dialogBuilder.setNegativeButton("Huỷ", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()
        alert.setTitle("Xác nhận")
        alert.show()
    }

    fun applySchedule(){
        ApiService.create().proposeSchedule(MyApplication.id,MyApplication.accessToken).enqueue(
            object :Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        if(response.body()!!){
                            Message.msg("Đề xuất lịch báo cáo thành công")
                            initSchedule()
                        }else
                            Message.msg("Đề xuất lịch báo cáo thất bại")
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
}
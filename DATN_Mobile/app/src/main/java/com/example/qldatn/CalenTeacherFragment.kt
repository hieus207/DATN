package com.example.qldatn

import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import com.example.qldatn.api.ApiService
import com.example.qldatn.calendarHelper.CurrentDayDecorator
import com.example.qldatn.models.Schedule
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalenTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalenTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var btn_calen_teacher_view:ImageButton
    lateinit var cdv_calendar_teacher: MaterialCalendarView
    lateinit var tv_calen_teacher_content:TextView
    var listSchedule = ArrayList<Schedule>()
    var listDay = ArrayList<CalendarDay>()
    var listContent = ArrayList<String>()

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
        val v = inflater.inflate(R.layout.fragment_calen_teacher, container, false)

        btn_calen_teacher_view = v.findViewById(R.id.btn_calen_teacher_view)
        cdv_calendar_teacher = v.findViewById(R.id.cdv_calendar_teacher)
        tv_calen_teacher_content = v.findViewById(R.id.tv_calen_teacher_content)
        if(!MyApplication.inPhase)
            btn_calen_teacher_view.isInvisible = true
        getCalendar()
        btn_calen_teacher_view.setOnClickListener {
            findNavController().navigate(R.id.action_calenTeacherFragment_to_listCalenTeacherFragment)
        }

        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalenTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getCalendar(){
        ApiService.create().getTeacherSchedule(MyApplication.id).enqueue(
            object : Callback<ArrayList<Schedule>> {
                override fun onResponse(call: Call<ArrayList<Schedule>>, response: Response<ArrayList<Schedule>>) {
                    if(response.isSuccessful) {
                        listSchedule = response.body()!!
                        initCalendar()
                    }
                }
                override fun onFailure(call: Call<ArrayList<Schedule>>, t: Throwable) {
                    Toast.makeText(context,"Lỗi không thể tải được dữ liệu lịch, kiểm tra đường truyền mạng!",
                        Toast.LENGTH_SHORT).show()
                }
            }
        )

    }
    fun initCalendar(){
        for(schedule in listSchedule){
            if(schedule.status=="2"){
                var year = schedule.reportDate.slice(0..3).toInt()
                var month = schedule.reportDate.slice(5..6).toInt()
                var day = schedule.reportDate.slice(8..9).toInt()
                var mydate= CalendarDay.from(year,month, day)
//                CẦN FIX TRÙNG CHỖ NÀY
                listDay.add(mydate)
                Log.e("date",mydate.toString())
//                TẠO RA LỆCH INDEX
                listContent.add(schedule.reportContent)
                cdv_calendar_teacher.addDecorators(CurrentDayDecorator(requireActivity(), mydate))
                val calendar = java.util.Calendar.getInstance()
                val today = CalendarDay.from(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(
                    Calendar.DAY_OF_MONTH))
                cdv_calendar_teacher.setSelectedDate(today)
                checkForSet(today)
            }
        }

        cdv_calendar_teacher.setOnDateChangedListener { widget, date, selected ->
            tv_calen_teacher_content.text = "Không có nhiệm vụ cần hoàn thành trong hôm nay"
            checkForSet(date)
//            var oldDate = CalendarDay.from(2000,2,2)
//            for(schedule in listSchedule){
//                if(schedule.status=="2"){
//                    var year = schedule.reportDate.slice(0..3).toInt()
//                    var month = schedule.reportDate.slice(5..6).toInt()
//                    var day = schedule.reportDate.slice(8..9).toInt()
//                    var mydate= CalendarDay.from(year,month, day)
//                    if(date.equals(mydate)){
//                        if(mydate.equals(oldDate)){
//                            tv_calen_teacher_content.text = tv_calen_teacher_content.text.toString() + "- Sinh viên: "+schedule.student_name + "\n    "+schedule.reportContent + "\n"
//                        }else{
//                            oldDate = mydate
//                            tv_calen_teacher_content.text = "- Sinh viên: "+schedule.student_name + "\n    "+schedule.reportContent + "\n"
//                        }
//                    }
//                }
//            }

        }
    }

    fun checkForSet(selectDate:CalendarDay){
        var oldDate = CalendarDay.from(2000,2,2)
        for(schedule in listSchedule){
            if(schedule.status=="2"){
                var year = schedule.reportDate.slice(0..3).toInt()
                var month = schedule.reportDate.slice(5..6).toInt()
                var day = schedule.reportDate.slice(8..9).toInt()
                var mydate= CalendarDay.from(year,month, day)
                if(selectDate.equals(mydate)){
                    if(mydate.equals(oldDate)){
                        tv_calen_teacher_content.text = tv_calen_teacher_content.text.toString() + "- Sinh viên: "+schedule.student_name + "\n    "+schedule.reportContent + "\n"
                    }else{
                        oldDate = mydate
                        tv_calen_teacher_content.text = "- Sinh viên: "+schedule.student_name + "\n    "+schedule.reportContent + "\n"
                    }
                }
            }
        }
    }
}
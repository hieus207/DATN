package com.example.qldatn


import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qldatn.api.ApiService
import com.example.qldatn.calendarHelper.CurrentDayDecorator
import com.example.qldatn.models.Schedule
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var btn_calendar_add:ImageView
    lateinit var cdv_calendar:MaterialCalendarView
    lateinit var tv_calendar_content:TextView

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
        var v:View = inflater.inflate(R.layout.fragment_calendar, container, false)

        tv_calendar_content = v.findViewById(R.id.tv_calendar_content)
        btn_calendar_add = v.findViewById(R.id.btn_calendar_add);
        cdv_calendar = v.findViewById(R.id.cdv_calendar)
        getCalendar()
        if(!MyApplication.inPhase){
            btn_calendar_add.isInvisible = true
        }


        btn_calendar_add.setOnClickListener {
            findNavController().navigate(R.id.action_calendarFragment_to_detailCalendarFragment)
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getCalendar(){
        ApiService.create().getSchedule(MyApplication.id,"all").enqueue(
            object :Callback<ArrayList<Schedule>>{
                override fun onResponse(call: Call<ArrayList<Schedule>>,response: Response<ArrayList<Schedule>> ) {
                    if(response.isSuccessful) {
                        listSchedule = response.body()!!
                        initCalendar()
                    }
                }
                override fun onFailure(call: Call<ArrayList<Schedule>>, t: Throwable) {
                    Toast.makeText(context,"Lỗi không thể tải được dữ liệu lịch, kiểm tra đường truyền mạng!",Toast.LENGTH_SHORT).show()
                }
            }
        )

    }

    fun initCalendar(){
        for(schedule in listSchedule){
            if(schedule.status=="1"||schedule.status=="2"){
                val year = schedule.reportDate.slice(0..3).toInt()
                val month = schedule.reportDate.slice(5..6).toInt()
                val day = schedule.reportDate.slice(8..9).toInt()
                val mydate= CalendarDay.from(year,month, day)
                listDay.add(mydate)
                listContent.add(schedule.reportContent)
                cdv_calendar.addDecorators(CurrentDayDecorator(requireActivity(), mydate))
                val calendar = java.util.Calendar.getInstance()
                val today = CalendarDay.from(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH))
                cdv_calendar.setSelectedDate(today)
                checkForSet(today)
            }
        }

        cdv_calendar.setOnDateChangedListener { widget, date, selected ->
            checkForSet(date)
        }
    }

    fun checkForSet(selectDate:CalendarDay){
        var idDate = listDay.indexOf(selectDate)
        if(idDate>=0){
            tv_calendar_content.text = listContent[idDate]
        }
        else
            tv_calendar_content.text = "Không có nhiệm vụ cần hoàn thành trong hôm nay"
    }
}


package com.example.qldatn

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import com.example.qldatn.Helper.Message
import com.example.qldatn.api.ApiService
import com.example.qldatn.api.response.ResHomeProject
import com.example.qldatn.api.response.ResStage
import com.example.qldatn.models.Phase
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var bottom_menu: BottomNavigationView
    lateinit var tv_home_projectName:TextView
    lateinit var tv_home_teacherName:TextView
    lateinit var tv_home_reportDate:TextView
    lateinit var tv_home_reportContent:TextView
    lateinit var tv_home_mon:TextView
    lateinit var tv_home_tue:TextView
    lateinit var tv_home_wed:TextView
    lateinit var tv_home_thu:TextView
    lateinit var tv_home_fri:TextView
    lateinit var tv_home_sat:TextView
    lateinit var tv_home_sun:TextView
    lateinit var cv_home_mon:CardView
    lateinit var cv_home_tue:CardView
    lateinit var cv_home_wed:CardView
    lateinit var cv_home_thu:CardView
    lateinit var cv_home_fri:CardView
    lateinit var cv_home_sat:CardView
    lateinit var cv_home_sun:CardView
    lateinit var pgb_home:ProgressBar
    lateinit var tv_home_startDate:TextView
    lateinit var tv_home_endDate:TextView
    val calendar = Calendar.getInstance()

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
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        bottom_menu= requireActivity()?.findViewById(R.id.bottom_menu)!!
        bottom_menu.isInvisible=false;
        tv_home_mon = v.findViewById(R.id.tv_home_mon)
        tv_home_tue = v.findViewById(R.id.tv_home_tue)
        tv_home_wed = v.findViewById(R.id.tv_home_wed)
        tv_home_thu = v.findViewById(R.id.tv_home_thu)
        tv_home_fri = v.findViewById(R.id.tv_home_fri)
        tv_home_sat = v.findViewById(R.id.tv_home_sat)
        tv_home_sun = v.findViewById(R.id.tv_home_sun)
        cv_home_mon = v.findViewById(R.id.cv_home_mon)
        cv_home_tue = v.findViewById(R.id.cv_home_tue)
        cv_home_wed = v.findViewById(R.id.cv_home_wed)
        cv_home_thu = v.findViewById(R.id.cv_home_thu)
        cv_home_fri = v.findViewById(R.id.cv_home_fri)
        cv_home_sat = v.findViewById(R.id.cv_home_sat)
        cv_home_sun = v.findViewById(R.id.cv_home_sun)
        tv_home_projectName = v.findViewById(R.id.tv_home_projectName)
        tv_home_teacherName = v.findViewById(R.id.tv_home_teacherName)
        tv_home_reportDate = v.findViewById(R.id.tv_home_reportDate)
        tv_home_reportContent = v.findViewById(R.id.tv_home_reportContent)
        pgb_home = v.findViewById(R.id.pgb_home)
        tv_home_startDate = v.findViewById(R.id.tv_home_startDate)
        tv_home_endDate = v.findViewById(R.id.tv_home_endDate)

        initProgessBar()
        fillDayOfWeek()
        initCardData()

        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun fillDayOfWeek(){

        var dayInWeek = 0;
        if(calendar.get(Calendar.DAY_OF_WEEK)==1){
            dayInWeek=8
        }else
            dayInWeek=calendar.get(Calendar.DAY_OF_WEEK)
        var distance = 6-(8-dayInWeek)

        calendar.add(Calendar.DATE, - distance);
        cv_home_mon.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_tue.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_wed.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_thu.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_fri.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_sat.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_sun.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))

        for (i in 2..8){
            when (i)
            {
                2 -> {
                    tv_home_mon.text = calendar.get(Calendar.DATE).toString()
                }
                3 -> {
                    tv_home_tue.text = calendar.get(Calendar.DATE).toString()
                }
                4 -> {
                    tv_home_wed.text = calendar.get(Calendar.DATE).toString()
                }
                5 -> {
                    tv_home_thu.text = calendar.get(Calendar.DATE).toString()
                }
                6 -> {
                    tv_home_fri.text = calendar.get(Calendar.DATE).toString()
                }
                7 -> {
                    tv_home_sat.text = calendar.get(Calendar.DATE).toString()
                }
                8 -> {
                    tv_home_sun.text = calendar.get(Calendar.DATE).toString()
                }
            }
            calendar.add(Calendar.DATE,1)
        }

        when(2+distance){
            2 ->{
                cv_home_mon.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            3 ->{
                cv_home_tue.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            4 ->{
                cv_home_wed.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            5 ->{
                cv_home_thu.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            6 ->{
                cv_home_fri.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            7 ->{
                cv_home_sat.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            8 ->{
                cv_home_sun.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
        }
    }

    fun initCardData(){
        ApiService.create().getHomeProject(MyApplication.id).enqueue(
            object :Callback<ResHomeProject>{
                override fun onResponse(
                    call: Call<ResHomeProject>,
                    response: Response<ResHomeProject>
                ) {
                    if(response.isSuccessful){
                        if(!response.body()?.phase!!){
                            tv_home_projectName.text = response.body()?.project?.name
                            tv_home_teacherName.text = response.body()?.teacher?.teacher_name
                            tv_home_reportDate.text = "Bạn đang không trong đợt làm đồ án nào!"
                            tv_home_reportContent.text = response.body()?.schedule?.reportContent
                            MyApplication.inPhase=false
                        }
                        else{
                            tv_home_projectName.text = response.body()?.project?.name
                            tv_home_teacherName.text = response.body()?.teacher?.teacher_name
                            tv_home_reportDate.text = response.body()?.schedule?.reportDate
                            tv_home_reportContent.text = response.body()?.schedule?.reportContent

                            if(tv_home_projectName.text.isEmpty()){
                                tv_home_projectName.text = "Bạn chưa tạo đề tài nào!"
                            }
                            MyApplication.inPhase=true
                        }
                    }
                    else
                        Message.faildMsg()
                }

                override fun onFailure(call: Call<ResHomeProject>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun calcDistance(startD:String,endD:String):Long{
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val start = startD
        val end = endD
        val startDate = formatter.parse(start)
        val endDate = formatter.parse(end)
        return TimeUnit.MILLISECONDS.toDays(endDate.time-startDate.time)
    }

    fun initProgessBar(){
        ApiService.create().getPhase().enqueue(
            object : Callback<Phase>{
                override fun onResponse(call: Call<Phase>, response: Response<Phase>) {
                    response.body()?.let {
                        val days: Long = calcDistance(response.body()!!.startDate,response.body()!!.endDate)
                        pgb_home.max = (days*100).toInt()

                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        var formatter2 = SimpleDateFormat("dd-MM-yyyy")
                        var stDate = formatter.parse(response.body()!!.startDate).time
                        var edDate = formatter.parse(response.body()!!.endDate).time
                        tv_home_startDate.text = formatter2.format(Date(stDate)).toString()
                        tv_home_endDate.text = formatter2.format(Date(edDate)).toString()

                        var currentDate = formatter.format(object : Date(){})
                        val currentProgress = (calcDistance(response.body()!!.startDate,currentDate)*100).toInt()
                        ObjectAnimator.ofInt(pgb_home,"progress",currentProgress).setDuration(2000).start()
                        Log.e("tt",(days*100).toString())
                        Log.e("cr",(calcDistance(response.body()!!.startDate,currentDate)*100).toString())
                    }

                }

                override fun onFailure(call: Call<Phase>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
}
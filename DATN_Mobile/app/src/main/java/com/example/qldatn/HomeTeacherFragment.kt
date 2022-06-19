package com.example.qldatn

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.Helper.CirclePagerIndicatorDecoration
import com.example.qldatn.Helper.Message
import com.example.qldatn.adapter.HomeInfoAdapter
import com.example.qldatn.api.ApiService
import com.example.qldatn.api.response.ResHomeInfo
import com.example.qldatn.api.response.ResStage
import com.example.qldatn.models.Phase
import com.example.qldatn.models.Stage
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var bottom_teacher_menu: BottomNavigationView
    lateinit var rcv_home_teacher: RecyclerView
    lateinit var tv_home_teacher_mon:TextView
    lateinit var tv_home_teacher_tue:TextView
    lateinit var tv_home_teacher_wed:TextView
    lateinit var tv_home_teacher_thu:TextView
    lateinit var tv_home_teacher_fri:TextView
    lateinit var tv_home_teacher_sat:TextView
    lateinit var tv_home_teacher_sun:TextView
    lateinit var cv_home_teacher_mon:CardView
    lateinit var cv_home_teacher_tue:CardView
    lateinit var cv_home_teacher_wed:CardView
    lateinit var cv_home_teacher_thu:CardView
    lateinit var cv_home_teacher_fri:CardView
    lateinit var cv_home_teacher_sat:CardView
    lateinit var cv_home_teacher_sun: CardView
    lateinit var pgb_home_teacher: ProgressBar
    lateinit var tv_home_teacher_startDate: TextView
    lateinit var tv_home_teacher_endDate: TextView
    val calendar = Calendar.getInstance()


    private lateinit var homeInfoAdapter: HomeInfoAdapter
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
        val v = inflater.inflate(R.layout.fragment_home_teacher, container, false)
        bottom_teacher_menu = requireActivity()?.findViewById(R.id.bottom_teacher_menu)!!
        bottom_teacher_menu.isInvisible = false;
        rcv_home_teacher = v.findViewById(R.id.rcv_home_teacher)
        tv_home_teacher_mon = v.findViewById(R.id.tv_home_teacher_mon)
        tv_home_teacher_tue = v.findViewById(R.id.tv_home_teacher_tue)
        tv_home_teacher_wed = v.findViewById(R.id.tv_home_teacher_wed)
        tv_home_teacher_thu = v.findViewById(R.id.tv_home_teacher_thu)
        tv_home_teacher_fri = v.findViewById(R.id.tv_home_teacher_fri)
        tv_home_teacher_sat = v.findViewById(R.id.tv_home_teacher_sat)
        tv_home_teacher_sun = v.findViewById(R.id.tv_home_teacher_sun)
        cv_home_teacher_mon = v.findViewById(R.id.cv_home_teacher_mon)
        cv_home_teacher_tue = v.findViewById(R.id.cv_home_teacher_tue)
        cv_home_teacher_wed = v.findViewById(R.id.cv_home_teacher_wed)
        cv_home_teacher_thu = v.findViewById(R.id.cv_home_teacher_thu)
        cv_home_teacher_fri = v.findViewById(R.id.cv_home_teacher_fri)
        cv_home_teacher_sat = v.findViewById(R.id.cv_home_teacher_sat)
        cv_home_teacher_sun = v.findViewById(R.id.cv_home_teacher_sun)
        pgb_home_teacher = v.findViewById(R.id.pgb_home_teacher)
        tv_home_teacher_startDate = v.findViewById(R.id.tv_home_teacher_startDate)
        tv_home_teacher_endDate = v.findViewById(R.id.tv_home_teacher_endDate)



        initProgessBar()
        fillDayOfWeek()
        initData()

        return v;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeTeacherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    fun initData(){
        checkTeacherInPhase();
        ApiService.create().getHomeInfo(MyApplication.id).enqueue(
            object : Callback<ArrayList<ResHomeInfo>>{
                override fun onResponse(
                    call: Call<ArrayList<ResHomeInfo>>,
                    response: Response<ArrayList<ResHomeInfo>>
                ) {
                    if(response.isSuccessful){
                        response.body()?.let {
                            if(response.body()?.size==0){
                                var arrlist = ArrayList<ResHomeInfo>(1)
                                var not:ResHomeInfo = ResHomeInfo("Bạn đang chưa nhận hướng dẫn sinh viên","","","")
                                arrlist.add(not)
                                homeInfoAdapter = HomeInfoAdapter(arrlist)
                            }else{
                                homeInfoAdapter = HomeInfoAdapter(response.body()!!)
                            }
                            val layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
                            rcv_home_teacher.layoutManager = layoutManager
                            rcv_home_teacher.adapter = homeInfoAdapter
                            rcv_home_teacher.addItemDecoration(CirclePagerIndicatorDecoration())
                            homeInfoAdapter.notifyDataSetChanged()
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<ResHomeInfo>>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun checkTeacherInPhase(){
        ApiService.create().getPhaseTeacher(MyApplication.id).enqueue(
            object : Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if(response.body()!=-1){
                        MyApplication.inPhase = true;
                    }
                    else
                        MyApplication.inPhase = false;
                }
                override fun onFailure(call: Call<Int>, t: Throwable) {
                    MyApplication.inPhase = false;
                }
            }
        )
    }
    fun fillDayOfWeek(){

        var dayInWeek = 0;
        if(calendar.get(Calendar.DAY_OF_WEEK)==1){
            dayInWeek=8
        }else
            dayInWeek=calendar.get(Calendar.DAY_OF_WEEK)
        var distance = 6-(8-dayInWeek)
        calendar.add(Calendar.DATE, - distance);
        cv_home_teacher_mon.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_teacher_tue.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_teacher_wed.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_teacher_thu.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_teacher_fri.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_teacher_sat.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))
        cv_home_teacher_sun.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_calen_blue))

        for (i in 2..8){
            when (i)
            {
                2 -> {
                    tv_home_teacher_mon.text = calendar.get(Calendar.DATE).toString()
                }
                3 -> {
                    tv_home_teacher_tue.text = calendar.get(Calendar.DATE).toString()
                }
                4 -> {
                    tv_home_teacher_wed.text = calendar.get(Calendar.DATE).toString()
                }
                5 -> {
                    tv_home_teacher_thu.text = calendar.get(Calendar.DATE).toString()
                }
                6 -> {
                    tv_home_teacher_fri.text = calendar.get(Calendar.DATE).toString()
                }
                7 -> {
                    tv_home_teacher_sat.text = calendar.get(Calendar.DATE).toString()
                }
                8 -> {
                    tv_home_teacher_sun.text = calendar.get(Calendar.DATE).toString()
                }
            }
            calendar.add(Calendar.DATE,1)
        }

        when(2+distance){
            2 ->{
                cv_home_teacher_mon.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            3 ->{
                cv_home_teacher_tue.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            4 ->{
                cv_home_teacher_wed.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            5 ->{
                cv_home_teacher_thu.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            6 ->{
                cv_home_teacher_fri.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            7 ->{
                cv_home_teacher_sat.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
            8 ->{
                cv_home_teacher_sun.setCardBackgroundColor(ContextCompat.getColor(requireContext(),R.color._bg_yellow))
            }
        }
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
                        val days: Long =
                            calcDistance(response.body()!!.startDate, response.body()!!.endDate)

                        pgb_home_teacher.max = (days * 100).toInt()

                        val formatter = SimpleDateFormat("yyyy-MM-dd")
                        var formatter2 = SimpleDateFormat("dd-MM-yyyy")
                        var stDate = formatter.parse(response.body()!!.startDate).time
                        var edDate = formatter.parse(response.body()!!.endDate).time
                        tv_home_teacher_startDate.text = formatter2.format(Date(stDate)).toString()
                        tv_home_teacher_endDate.text = formatter2.format(Date(edDate)).toString()

                        var currentDate = formatter.format(object : Date() {})
                        val currentProgress =
                            (calcDistance(response.body()!!.startDate, currentDate) * 100).toInt()
                        ObjectAnimator.ofInt(pgb_home_teacher, "progress", currentProgress)
                            .setDuration(2000).start()
                        Log.e("tt", (days * 100).toString())
                        Log.e(
                            "cr",
                            (calcDistance(
                                response.body()!!.startDate,
                                currentDate
                            ) * 100).toString()
                        )
                    }

                }

                override fun onFailure(call: Call<Phase>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
}
package com.example.qldatn

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.adapter.ScheduleAdapter
import com.example.qldatn.adapter.ScheduleTeacherAdapter
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailCalenTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailCalenTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rcv_detail_calen_teacher:RecyclerView
    var listSchedule = ArrayList<Schedule>()
    private lateinit var scheduleTeacherAdapter: ScheduleTeacherAdapter
    var student_id:Int = -1
    lateinit var btn_detail_calen_teacher_accept:ImageView
    lateinit var btn_detail_calen_teacher_cancel:ImageView
    lateinit var btn_detail_calen_teacher_back:ImageView
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
        arguments?.let {
            student_id = it.getInt("student_id")
        }
        val v = inflater.inflate(R.layout.fragment_detail_calen_teacher, container, false)
        btn_detail_calen_teacher_accept = v.findViewById(R.id.btn_detail_calen_teacher_accept)
        btn_detail_calen_teacher_cancel = v.findViewById(R.id.btn_detail_calen_teacher_cancel)
        btn_detail_calen_teacher_back = v.findViewById(R.id.btn_detail_calen_teacher_back)

        btn_detail_calen_teacher_accept.setOnClickListener {
            acceptAllSchedule()
        }
        btn_detail_calen_teacher_cancel.setOnClickListener {
            showRefuseAllSchedule()
        }
        btn_detail_calen_teacher_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        rcv_detail_calen_teacher = v.findViewById(R.id.rcv_detail_calen_teacher)
        initSchedule();
        return v;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailCalenTeacherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailCalenTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initSchedule(){
        if(student_id>0)
        ApiService.create().getSchedule(student_id,"pending").enqueue(
            object : Callback<ArrayList<Schedule>> {
                override fun onResponse(call: Call<ArrayList<Schedule>>, response: Response<ArrayList<Schedule>>) {
                    if(response.isSuccessful){
                        listSchedule = response.body()!!
                        scheduleTeacherAdapter= ScheduleTeacherAdapter(listSchedule)
                        val layoutManager = LinearLayoutManager(context)
                        rcv_detail_calen_teacher.layoutManager = layoutManager
                        rcv_detail_calen_teacher.adapter = scheduleTeacherAdapter
                        scheduleTeacherAdapter.setOnItemClickListener(
                            object : ScheduleTeacherAdapter.onItemClicklistener{
                                override fun onItemClick(position: Int) {
                                    Log.e("Click ",position.toString())
                                }

                                override fun onDeleteClick(position: Int) {
                                    showRefuseDialog(listSchedule[position].id);
                                }
                            }
                        )
                        scheduleTeacherAdapter.notifyDataSetChanged()
                        if(listSchedule.size==0){
                            btn_detail_calen_teacher_cancel.isInvisible=true
                            btn_detail_calen_teacher_accept.isInvisible=true
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Schedule>>, t: Throwable) {
                    Toast.makeText(context,"Tạo lịch báo cáo thất bại kiểm tra mạng", Toast.LENGTH_SHORT).show()
                }
            }
        )
        else
            Toast.makeText(context,"Tạo lịch báo cáo thất bại vui lòng đăng nhập lại",Toast.LENGTH_SHORT).show()
    }


    fun showRefuseDialog(id:Int){
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.dialog_refuse_schedule, null)
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        val edt_refuseSchedule_content: EditText = dialogView.findViewById(R.id.edt_refuseSchedule_content)


        dialogBuilder.setPositiveButton("Đồng ý"){_,_->
            refuseSchedule(id,edt_refuseSchedule_content.text.toString())

        }
        dialogBuilder.setNegativeButton("Huỷ bỏ", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()

        alert.show()
    }

    fun showRefuseAllSchedule(){
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.dialog_refuse_schedule, null)
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setTitle("Bạn có chắc muốn từ chối tất cả đề xuất của sinh viên?")
        val edt_refuseSchedule_content: EditText = dialogView.findViewById(R.id.edt_refuseSchedule_content)
        dialogBuilder.setPositiveButton("Đồng ý"){_,_->
            refuseAllSchedule(edt_refuseSchedule_content.text.toString())

        }
        dialogBuilder.setNegativeButton("Huỷ bỏ", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()

        alert.show()
    }
    fun refuseSchedule(id:Int,refuseContent:String){
        ApiService.create().updateTeacherSchedule(id,3,refuseContent,MyApplication.accessToken).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if(response.body()!!){
                            Toast.makeText(context,"Từ chối đề xuất thành công",Toast.LENGTH_SHORT).show()
                            initSchedule()
                        }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Từ chối đề xuất thất bại, kiểm tra đường truyền mạng!",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun acceptAllSchedule(){
        ApiService.create().acceptAllSchedule(student_id,MyApplication.id,"",MyApplication.accessToken).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if(response.body()!!){
                            Toast.makeText(context,"Chấp nhận tất cả đề xuất thành công",Toast.LENGTH_SHORT).show()
                            initSchedule()
                        }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Thực thi thất bại, kiểm tra đường truyền mạng!",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun refuseAllSchedule(refuseContent: String){
        ApiService.create().refuseAllSchedule(student_id,MyApplication.id,refuseContent,MyApplication.accessToken).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if(response.body()!!){
                            Toast.makeText(context,"Từ chối tất cả đề xuất thành công",Toast.LENGTH_SHORT).show()
                            initSchedule()
                        }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Thực thi thất bại, kiểm tra đường truyền mạng!",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
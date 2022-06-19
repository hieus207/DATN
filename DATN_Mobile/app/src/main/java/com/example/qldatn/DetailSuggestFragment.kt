package com.example.qldatn

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import com.example.qldatn.api.ApiService
import com.example.qldatn.api.response.ResProject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailSuggestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailSuggestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var tv_dtsuggest_teacher_projectname:TextView
    lateinit var tv_dtsuggest_teacher_studentname:TextView
    lateinit var tv_dtsuggest_teacher_quest:TextView
    lateinit var tv_dtsuggest_teacher_solution:TextView
    lateinit var tv_dtsuggest_teacher_platform:TextView
    lateinit var tv_dtsuggest_teacher_tech:TextView
    lateinit var tv_dtsuggest_teacher_func:TextView
    lateinit var tv_dtsuggest_teacher_result:TextView
    lateinit var tv_dtsuggest_teacher_title:TextView
    lateinit var tv_dtsuggest_teacher_link:TextView
    lateinit var btn_dtsuggest_teacher_accept:ImageView
    lateinit var btn_dtsuggest_teacher_cancel:ImageView
    lateinit var btn_dtsuggest_teacher_back:ImageView
    var project_id:Int =-1
    var student_id:Int =-1
    lateinit var student_name:String
    lateinit var from:String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            from = it.getString("from").toString()
            project_id = it.getInt("project_id")
            student_id = it.getInt("student_id")
            student_name = it.getString("student_name").toString()
        }
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_detail_suggest, container, false)
        tv_dtsuggest_teacher_title = v.findViewById(R.id.tv_dtsuggest_teacher_title)
        tv_dtsuggest_teacher_link = v.findViewById(R.id.tv_dtsuggest_teacher_link)
        tv_dtsuggest_teacher_projectname = v.findViewById(R.id.tv_dtsuggest_teacher_projectname)
        tv_dtsuggest_teacher_studentname = v.findViewById(R.id.tv_dtsuggest_teacher_studentname)
        tv_dtsuggest_teacher_quest = v.findViewById(R.id.tv_dtsuggest_teacher_quest)
        tv_dtsuggest_teacher_solution = v.findViewById(R.id.tv_dtsuggest_teacher_solution)
        tv_dtsuggest_teacher_platform = v.findViewById(R.id.tv_dtsuggest_teacher_platform)
        tv_dtsuggest_teacher_tech = v.findViewById(R.id.tv_dtsuggest_teacher_tech)
        tv_dtsuggest_teacher_func = v.findViewById(R.id.tv_dtsuggest_teacher_func)
        tv_dtsuggest_teacher_result = v.findViewById(R.id.tv_dtsuggest_teacher_result)
        btn_dtsuggest_teacher_accept = v.findViewById(R.id.btn_dtsuggest_teacher_accept)
        btn_dtsuggest_teacher_cancel = v.findViewById(R.id.btn_dtsuggest_teacher_cancel)
        btn_dtsuggest_teacher_back = v.findViewById(R.id.btn_dtsuggest_teacher_back)

        if(from.equals("projectFragment")){
            tv_dtsuggest_teacher_title.text = "Đề tài sinh viên"
            btn_dtsuggest_teacher_accept.isInvisible=true
            btn_dtsuggest_teacher_cancel.isInvisible=true

        }

        tv_dtsuggest_teacher_studentname.setOnClickListener {
            val bundle = Bundle();
            bundle.putInt("student_id",student_id)
            Log.e("student_id",student_id.toString())
            findNavController().navigate(R.id.action_detailSuggestFragment_to_infoStudentFragment,bundle)
        }
        btn_dtsuggest_teacher_accept.setOnClickListener {
            AcceptProposal()
        }
        btn_dtsuggest_teacher_cancel.setOnClickListener {
            showConfirmDialog()
        }
        btn_dtsuggest_teacher_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        initData();
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailSuggestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initData(){
        ApiService.create().getProject(student_id,MyApplication.accessToken).enqueue(
            object : Callback<ResProject> {
                override fun onResponse(call: Call<ResProject>, response: Response<ResProject>) {
                    if(response.isSuccessful){
                        val resProject: ResProject? = response!!.body()
                        if(resProject!!.status){
//                            Truyen data
                            val project = response!!.body()!!.data;
                            tv_dtsuggest_teacher_link.text = if(project!!.link.toString()?.isNotEmpty()) "Đường dẫn: "+ project!!.link.toString() else ""
                            Log.e("link",project!!.link.toString())
                            tv_dtsuggest_teacher_projectname.text = project!!.name
                            tv_dtsuggest_teacher_studentname.text = student_name
                            tv_dtsuggest_teacher_quest.text = project!!.name
                            tv_dtsuggest_teacher_solution.text = project!!.solution
                            tv_dtsuggest_teacher_platform.text = project!!.platforms
                            tv_dtsuggest_teacher_tech.text = project!!.tech
                            tv_dtsuggest_teacher_func.text = project!!.func
                            tv_dtsuggest_teacher_result.text = project!!.result
                        }
                    }
                }

                override fun onFailure(call: Call<ResProject>, t: Throwable) {
                    Toast.makeText(context,"Lấy dữ liệu thất bại",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun AcceptProposal(){
        ApiService.create().acceptProposal(
            student_id,
            MyApplication.id,
            project_id,
            "accept",
            MyApplication.accessToken
        ).enqueue(
            object :Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if (response.body()!!){
                            Toast.makeText(context,"Chấp nhận đề xuất thành công",Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_detailSuggestFragment_to_suggestTeacherFragment)
                        }


                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Thực thi thất bại kiểm tra đường truyền",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun showConfirmDialog(){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Bạn có chắc muốn từ chối đề xuất này không?")
        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton("Chắc chắn"){_,_->
            RefuseProposal()
        }
        dialogBuilder.setNegativeButton("Huỷ bỏ", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()
        alert.setTitle("Xác nhận")
        alert.show()
    }

    fun RefuseProposal(){
        ApiService.create().acceptProposal(
            student_id,
            MyApplication.id,
            project_id,
            "refuse",
            MyApplication.accessToken
        ).enqueue(
            object :Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if (response.body()!!){
                            Toast.makeText(context,"Từ chối đề xuất thành công",Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_detailSuggestFragment_to_suggestTeacherFragment)
                        }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Thực thi thất bại kiểm tra đường truyền",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
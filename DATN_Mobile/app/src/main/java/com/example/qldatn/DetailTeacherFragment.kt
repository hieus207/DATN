package com.example.qldatn

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.qldatn.api.ApiService
import com.example.qldatn.api.response.ResApplyProject
import com.example.qldatn.api.response.ResProject
import com.example.qldatn.models.Teacher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DetailTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    var project_id:Int = 0
    lateinit var teacher: Teacher
    lateinit var tv_shteacher_name:TextView
    lateinit var tv_shteacher_quota:TextView
    lateinit var tv_shteacher_major:TextView
    lateinit var tv_shteacher_topic:TextView
    lateinit var tv_shteacher_platform:TextView
    lateinit var btn_shteacher_apply:ImageButton
    lateinit var btn_shteacher_back:ImageView
    lateinit var img_shteacher_avt:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_detail_teacher, container, false)
        tv_shteacher_name = v.findViewById(R.id.tv_shteacher_name)
        tv_shteacher_quota = v.findViewById(R.id.tv_shteacher_quota)
        tv_shteacher_major = v.findViewById(R.id.tv_shteacher_major)
        tv_shteacher_topic = v.findViewById(R.id.tv_shteacher_topic)
        tv_shteacher_platform = v.findViewById(R.id.tv_shteacher_platform)
        btn_shteacher_apply = v.findViewById(R.id.btn_shteacher_apply)
        btn_shteacher_back = v.findViewById(R.id.btn_shteacher_back)
        img_shteacher_avt = v.findViewById(R.id.img_shteacher_avt)

        arguments?.let {
            teacher = it.getSerializable("teacher") as Teacher
        }
        btn_shteacher_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        btn_shteacher_apply.setOnClickListener {
            confirmProject();
        }
        setData()
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun setData(){
        teacher.avt?.let {
            if(!it.isEmpty()){
                Glide.with(requireContext()).load(teacher.avt).into(img_shteacher_avt)
            }
        }
        tv_shteacher_name.text = teacher.teacher_name
        tv_shteacher_quota.text = "Đã nhận ${teacher.quota-teacher.available_quota}/${teacher.quota} sinh viên"
        tv_shteacher_major.text = teacher.major
        tv_shteacher_topic.text = teacher.topic
        tv_shteacher_platform.text = teacher.platforms!!.dropLast(1).replace(",","\n");

    }

    fun confirmProject(){
        ApiService.create().confirmApply(MyApplication.id).enqueue(
            object : Callback<ResApplyProject>{
                override fun onResponse(call: Call<ResApplyProject>,response: Response<ResApplyProject>) {
                    if(response.isSuccessful){
                        project_id=response!!.body()!!.project_id
                        showApplyDialog(response!!.body()!!.project_name)
                    }
                }
                override fun onFailure(call: Call<ResApplyProject>, t: Throwable) {
                    Toast.makeText(context,"Kiểm tra lại đề tài đã tạo và đường truyền mạng!",Toast.LENGTH_LONG).show()
                    Log.e("fail",t.toString())
                }
            }
        )
    }
    fun showApplyDialog(project_name:String){
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Bạn muốn đề xuất đề tài: \n  - "+project_name +"\n Với giảng viên: \n  - "+ teacher.teacher_name)
        dialogBuilder.setCancelable(false)
        dialogBuilder.setPositiveButton("OK"){_,_->
            applyProject();
        }
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()
        alert.setTitle("Xác nhận đề xuất")
        alert.show()
    }

    fun applyProject(){
        ApiService.create().applyProject(
            MyApplication.id,
            teacher.teacher_id,
            project_id,
            MyApplication.accessToken
        ).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        if(response.body()!!){
                            Toast.makeText(context,"Tạo đề xuất thành công",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(context,"Tạo đề xuất thất bại: kiểm tra đề xuất đã tạo bị trùng và hạn ngạch",Toast.LENGTH_LONG).show()
                        }
                    }

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Tạo đề xuất thất bại đường truyền mạng! ",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

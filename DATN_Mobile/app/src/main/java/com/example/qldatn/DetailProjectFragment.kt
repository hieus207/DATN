package com.example.qldatn

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.qldatn.Helper.Message
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailProjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var project: Project? = null
    private var param2: String? = null
    lateinit var tv_shproject_linksm:TextView
    lateinit var tv_shproject_name:TextView
    lateinit var tv_shproject_quest:TextView
    lateinit var tv_shproject_solution:TextView
    lateinit var tv_shproject_platform:TextView
    lateinit var tv_shproject_tech:TextView
    lateinit var tv_shproject_func:TextView
    lateinit var tv_shproject_result:TextView
    lateinit var btn_shproject_edit:ImageButton
    lateinit var btn_shproject_submit:ImageButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (savedInstanceState != null) {
            findNavController().navigate(R.id.action_detailProjectFragment_to_projectFragment)
        }
        val v = inflater.inflate(R.layout.fragment_detail_project, container, false)
        tv_shproject_linksm = v.findViewById(R.id.tv_shproject_linksm)
        tv_shproject_name = v.findViewById(R.id.tv_shproject_name)
        tv_shproject_quest =  v.findViewById(R.id.tv_shproject_quest)
        tv_shproject_solution =  v.findViewById(R.id.tv_shproject_solution)
        tv_shproject_platform =  v.findViewById(R.id.tv_shproject_platform)
        tv_shproject_tech =  v.findViewById(R.id.tv_shproject_tech)
        tv_shproject_func =  v.findViewById(R.id.tv_shproject_func)
        tv_shproject_result =  v.findViewById(R.id.tv_shproject_result)
        btn_shproject_edit = v.findViewById(R.id.btn_shproject_edit)
        btn_shproject_submit = v.findViewById(R.id.btn_shproject_submit)
        btn_shproject_submit.isInvisible = true
        btn_shproject_edit.isInvisible = true
        arguments?.let {
            project = it.getSerializable("project") as Project?

            tv_shproject_linksm.text = if(project!!.link.toString()?.isNotEmpty()) "Đường dẫn: "+ project!!.link.toString() else ""
            Log.e("link",project!!.link.toString()?.isEmpty().toString())
            tv_shproject_name.text = project!!.name
            tv_shproject_quest.text = project!!.quest
            tv_shproject_solution.text = project!!.solution
            tv_shproject_platform.text =project!!.platforms.replace(",","\n").trim()
            tv_shproject_tech.text = project!!.tech
            tv_shproject_func.text = project!!.func
            tv_shproject_result.text = project!!.result
        }
        btn_shproject_edit.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("project",project)
            findNavController().navigate(R.id.action_detailProjectFragment_to_editProjectFragment,bundle)
        }
        checkStage()
        return v
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailProjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun checkStage(){
        ApiService.create().getStage().enqueue(
            object : Callback<Int>{
                override fun onResponse(call: Call<Int>, response: Response<Int>) {
                    if(response.body()!=-1){
                        if(response.body()!! == 4){
                            btn_shproject_submit.isVisible = true
                            btn_shproject_submit.setOnClickListener {
                                showSubmitDialog();
                            }
                        }
                        if(response.body()!! < 3){
                            btn_shproject_edit.isVisible = true
                        }
                    }

                }

                override fun onFailure(call: Call<Int>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun showSubmitDialog(){
        val inflater = requireActivity().layoutInflater;
        val dialogView = inflater.inflate(R.layout.dialog_submit, null)
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setView(dialogView)
        dialogBuilder.setCancelable(false)
        val edt_submit_project: EditText = dialogView.findViewById(R.id.edt_submit_project)


        dialogBuilder.setPositiveButton("Đồng ý"){_,_->
//            goi api
            submitPorject(edt_submit_project.text.toString())

        }
        dialogBuilder.setNegativeButton("Huỷ bỏ", DialogInterface.OnClickListener {
                dialog, id -> dialog.cancel()
        })
        val alert = dialogBuilder.create()

        alert.show()
    }

    fun submitPorject(link:String){
        ApiService.create().submitProject(
            MyApplication.id,
            link
        ).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.body()!!){
                        Message.msg("Nộp đồ án thành công!")
                        findNavController().navigate(R.id.action_detailProjectFragment_to_projectFragment)
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
}
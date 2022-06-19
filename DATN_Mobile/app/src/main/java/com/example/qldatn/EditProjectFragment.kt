package com.example.qldatn

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.qldatn.Helper.Message
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Platform
import com.example.qldatn.models.Project
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cv_edtproject_platforms: CardView
    lateinit var tv_edtproject_inpplatform: TextView
    lateinit var btn_edtproject_add: ImageView
    lateinit var btn_edtproject_cancel: ImageView
    lateinit var edt_edtproject_inpname: EditText
    lateinit var edt_edtproject_inpquest: EditText
    lateinit var edt_edtproject_inpsolution: EditText
    lateinit var edt_edtproject_inptechs: EditText
    lateinit var edt_edtproject_inpfuncs: EditText
    lateinit var edt_edtproject_inpresult: EditText
    lateinit var btn_edtproject_back: ImageView

    lateinit var selectedPlatform:BooleanArray
    private var project: Project? = null
    var platformList = ArrayList<Int>()
    var platformArray = arrayOf<String>("Web","Android","Ios")
    var platformId = arrayOf<Int>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v=inflater.inflate(R.layout.fragment_edit_project, container, false)
        cv_edtproject_platforms = v.findViewById(R.id.cv_edtproject_platforms)
        tv_edtproject_inpplatform = v.findViewById(R.id.tv_edtproject_inpplatform)
        btn_edtproject_add = v.findViewById(R.id.btn_edtproject_add)
        edt_edtproject_inpname = v.findViewById(R.id.edt_edtproject_inpname)
        edt_edtproject_inpquest = v.findViewById(R.id.edt_edtproject_inpquest)
        edt_edtproject_inpsolution = v.findViewById(R.id.edt_edtproject_inpsolution)
        edt_edtproject_inptechs = v.findViewById(R.id.edt_edtproject_inptechs)
        edt_edtproject_inpfuncs = v.findViewById(R.id.edt_edtproject_inpfuncs)
        edt_edtproject_inpresult = v.findViewById(R.id.edt_edtproject_inpresult)
        btn_edtproject_back = v.findViewById(R.id.btn_edtproject_back)
        btn_edtproject_cancel = v.findViewById(R.id.btn_edtproject_cancel)

        arguments?.let {
            project = it.getSerializable("project") as Project?
            edt_edtproject_inpname.setText(project!!.name);
            edt_edtproject_inpquest.setText(project!!.quest)
            edt_edtproject_inpsolution.setText(project!!.solution)
            edt_edtproject_inptechs.setText(project!!.tech)
            edt_edtproject_inpfuncs.setText(project!!.func)
            edt_edtproject_inpresult.setText(project!!.result)
        }
        initPlatform();

        btn_edtproject_cancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btn_edtproject_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btn_edtproject_add.setOnClickListener {
            updateProject()
        }
        
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun initPlatform(){
        ApiService.create().getAllPlatform().enqueue(
            object : Callback<ArrayList<Platform>> {
                override fun onResponse(
                    call: Call<ArrayList<Platform>>,
                    response: Response<ArrayList<Platform>>
                ) {
                    if(response.isSuccessful){
                        platformId = response.body()!!.map { it.id }.toTypedArray()
                        platformArray= response.body()!!.map { it.name }.toTypedArray()
                        cv_edtproject_platforms.setOnClickListener {
                            showPlatformDialog()
                        }
                        val selected = project!!.platforms.dropLast(1).split(",")
                        selectedPlatform= BooleanArray(platformArray.size){false}

                        for (item in selected){
                            var index = platformArray.indexOf(item.trim())
                            if(index>=0){
                                selectedPlatform[index]=true;
                                tv_edtproject_inpplatform.text = "${tv_edtproject_inpplatform.text} ${platformArray[index]},"
                            }
                        }
                        if(tv_edtproject_inpplatform.text!="")
                            tv_edtproject_inpplatform.text=tv_edtproject_inpplatform.text.dropLast(1)
                    }
                }
                override fun onFailure(call: Call<ArrayList<Platform>>, t: Throwable) {
                }
            }
        )
    }
    fun showPlatformDialog(){
        lateinit var dialog: AlertDialog
        var builder = AlertDialog.Builder(context)
        builder.setTitle("Select Platform")
        builder.setCancelable(false)
        builder.setMultiChoiceItems(platformArray,selectedPlatform,{dialog,which,isChecked->
            selectedPlatform[which]=isChecked
        })
        platformList.clear()
        builder.setPositiveButton("OK"){_,_ ->
            tv_edtproject_inpplatform.text = ""
            for (i in 0 until platformArray.size){
                val checked = selectedPlatform[i]
                if(checked){
                    platformList.add(platformId[i])
                    tv_edtproject_inpplatform.text = "${tv_edtproject_inpplatform.text} ${platformArray[i]},"
                }
            }

            if(tv_edtproject_inpplatform.text!="")
                tv_edtproject_inpplatform.text=tv_edtproject_inpplatform.text.dropLast(1)
        }
        dialog = builder.create()
        dialog.show()
    }

    fun updateProject(){
        if(checkData())
        ApiService.create().updateProject(
            project!!.id,
            edt_edtproject_inpname.text.toString(),
            edt_edtproject_inpquest.text.toString(),
            edt_edtproject_inpsolution.text.toString(),
            edt_edtproject_inptechs.text.toString(),
            edt_edtproject_inpfuncs.text.toString(),
            edt_edtproject_inpresult.text.toString(),
            MyApplication.id,
            platformList,
            MyApplication.accessToken
        ).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){
                        Toast.makeText(context,"Cập nhật đề tài thành công", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_editProjectFragment_to_projectFragment)
                    }
                }
                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Cập nhật đề tài thất bại", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    fun checkData():Boolean{
        var name = edt_edtproject_inpname.text.toString()
        var quest = edt_edtproject_inpquest.text.toString()
        var solution = edt_edtproject_inpsolution.text.toString()
        var techs = edt_edtproject_inptechs.text.toString()
        var funcs = edt_edtproject_inpfuncs.text.toString()
        var result = edt_edtproject_inpresult.text.toString()

        if(name.length<10)
        {
            Message.msg("Tên đề tài tối thiểu 10 ký tự")
            return false
        }
        if(quest.length<10)
        {
            Message.msg("Đặt vấn đề tối thiểu 10 ký tự")
            return false
        }
        if(solution.length<10)
        {
            Message.msg("Giải pháp tối thiểu 10 ký tự")
            return false
        }
        if(techs.length<10)
        {
            Message.msg("Công nghệ sử dụng tối thiểu 10 ký tự")
            return false
        }
        if(funcs.length<10)
        {
            Message.msg("Chức năng chính thiểu 10 ký tự")
            return false
        }
        if(result.length<10)
        {
            Message.msg("kết quả mong muốn tối thiểu 10 ký tự")
            return false
        }
        return true
    }
}
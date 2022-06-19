package com.example.qldatn

import android.app.AlertDialog
import android.os.Bundle
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CreateProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateProjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cv_crtproject_platforms:CardView
    lateinit var tv_crtproject_inpplatform:TextView

    lateinit var btn_crtproject_add:ImageView
    lateinit var btn_crtproject_back:ImageView
    lateinit var edt_crtproject_inpname:EditText
    lateinit var edt_crtproject_inpquest:EditText
    lateinit var edt_crtproject_inpsolution:EditText
    lateinit var edt_crtproject_inptechs:EditText
    lateinit var edt_crtproject_inpfuncs:EditText
    lateinit var edt_crtproject_inpresult:EditText

    lateinit var selectedPlatform:BooleanArray
    var platformList = ArrayList<Int>()
    var platformId = arrayOf<Int>()
    var platformArray = arrayOf<String>("Web","Android","Ios")


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
        val v = inflater.inflate(R.layout.fragment_create_project, container, false)

        cv_crtproject_platforms = v.findViewById(R.id.cv_crtproject_platforms)
        tv_crtproject_inpplatform = v.findViewById(R.id.tv_crtproject_inpplatform)
        btn_crtproject_add = v.findViewById(R.id.btn_crtproject_add)
        btn_crtproject_back = v.findViewById(R.id.btn_crtproject_back)
        edt_crtproject_inpname = v.findViewById(R.id.edt_crtproject_inpname)
        edt_crtproject_inpquest = v.findViewById(R.id.edt_crtproject_inpquest)
        edt_crtproject_inpsolution = v.findViewById(R.id.edt_crtproject_inpsolution)
        edt_crtproject_inptechs = v.findViewById(R.id.edt_crtproject_inptechs)
        edt_crtproject_inpfuncs = v.findViewById(R.id.edt_crtproject_inpfuncs)
        edt_crtproject_inpresult = v.findViewById(R.id.edt_crtproject_inpresult)

        initPlatform();
        btn_crtproject_add.setOnClickListener {
            createProject();
        }
        btn_crtproject_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CreateProjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initPlatform(){
        ApiService.create().getAllPlatform().enqueue(
            object : Callback<ArrayList<Platform>>{
                override fun onResponse(
                    call: Call<ArrayList<Platform>>,
                    response: Response<ArrayList<Platform>>
                ) {
                    if(response.isSuccessful){
                        platformId = response.body()!!.map { it.id }.toTypedArray()
                        platformArray= response.body()!!.map { it.name }.toTypedArray()
                        cv_crtproject_platforms.setOnClickListener {
                            showPlatformDialog()
                        }
                        selectedPlatform= BooleanArray(platformArray.size){false}
                    }

                }

                override fun onFailure(call: Call<ArrayList<Platform>>, t: Throwable) {

                }
            }
        )
    }
    fun showPlatformDialog(){
        lateinit var dialog:AlertDialog
        var builder = AlertDialog.Builder(context)
        builder.setTitle("Select Platform")
        builder.setCancelable(false)
        builder.setMultiChoiceItems(platformArray,selectedPlatform,{dialog,which,isChecked->
            selectedPlatform[which]=isChecked
        })
        platformList.clear()
        builder.setPositiveButton("OK"){_,_ ->
            tv_crtproject_inpplatform.text = ""
            for (i in 0 until platformArray.size){
                val checked = selectedPlatform[i]
                if(checked){
                    platformList.add(platformId[i])
                    tv_crtproject_inpplatform.text = "${tv_crtproject_inpplatform.text} ${platformArray[i]},"
                }
            }

            if(tv_crtproject_inpplatform.text!="")
                tv_crtproject_inpplatform.text=tv_crtproject_inpplatform.text.dropLast(1)
        }
        dialog = builder.create()
        dialog.show()
    }

    fun createProject(){
        if(checkData())
            ApiService.create().createProject(
                edt_crtproject_inpname.text.toString(),
                edt_crtproject_inpquest.text.toString(),
                edt_crtproject_inpsolution.text.toString(),
                edt_crtproject_inptechs.text.toString(),
                edt_crtproject_inpfuncs.text.toString(),
                edt_crtproject_inpresult.text.toString(),
                MyApplication.id,
                platformList,
                MyApplication.accessToken
            ).enqueue(
                object : Callback<Boolean>{
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        if(response.isSuccessful){
                            Message.msg("Tạo dự án thành công")
                            findNavController().navigate(R.id.action_createProjectFragment_to_projectFragment)
                        }
                    }
                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        Message.faildMsg()
                    }
                }
            )
    }
    fun checkData():Boolean{
        var name = edt_crtproject_inpname.text.toString()
        var quest = edt_crtproject_inpquest.text.toString()
        var solution = edt_crtproject_inpsolution.text.toString()
        var techs = edt_crtproject_inptechs.text.toString()
        var funcs = edt_crtproject_inpfuncs.text.toString()
        var result = edt_crtproject_inpresult.text.toString()

        if(name.length<10)
        {
           Message.msg("Tên dự án tối thiểu 10 ký tự")
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



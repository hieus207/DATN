package com.example.qldatn

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.Helper.Load
import com.example.qldatn.Helper.Message
import com.example.qldatn.adapter.TeacherAdapter
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Major
import com.example.qldatn.models.Platform
import com.example.qldatn.models.Teacher
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TeacherFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var param1: String? = null
    private var param2: String? = null
    var major_id:Int = -1
    var platform_id:Int = -1
    var listTeacher = ArrayList<Teacher>()

    lateinit var rcv_teacher:RecyclerView
    lateinit var edt_teacher_search:EditText
    lateinit var btn_teacher_search:ImageButton
    lateinit var spinner_subject:Spinner
    lateinit var spinner_platform:Spinner
    private lateinit var teacherAdapter: TeacherAdapter

    var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")
    lateinit var listMajor:ArrayList<Major>
    var list_of_items_2 = arrayOf("Item 1", "Item 2", "Item 3")
    lateinit var listPlatform:ArrayList<Platform>

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
        val v = inflater.inflate(R.layout.fragment_teacher, container, false)
        rcv_teacher=v.findViewById(R.id.rcv_teacher)
        edt_teacher_search = v.findViewById(R.id.edt_teacher_search)
        btn_teacher_search = v.findViewById(R.id.btn_teacher_search)
        spinner_subject = v.findViewById(R.id.spinner_subject)
        spinner_platform = v.findViewById(R.id.spinner_platform)
        btn_teacher_search.setOnClickListener {
            search()
        }
        if(MyApplication.inPhase){
            initMajor()
            initPlatform()
            getAllTeacher()
        }
        else
            Message.msg("Bạn không ở trong đợt làm đồ án!")
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initMajor(){
        ApiService.create().getAllMajor().enqueue(
            object :Callback<ArrayList<Major>>{
                override fun onResponse(call: Call<ArrayList<Major>>, response: Response<ArrayList<Major>>) {
                    if(response.isSuccessful){
                        listMajor= response!!.body()!!
                        listMajor.add(Major(-1,"Not select"))
                        list_of_items = listMajor.map { it.name.toString() }.toTypedArray()
                        spinner_subject.setOnItemSelectedListener(this@TeacherFragment)
                        val majorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list_of_items)
                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner_subject!!.setAdapter(majorAdapter)
                        spinner_subject!!.setSelection(majorAdapter.getPosition("Not select"))
                    }
                }
                override fun onFailure(call: Call<ArrayList<Major>>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun initPlatform(){
        ApiService.create().getAllPlatform().enqueue(
            object :Callback<ArrayList<Platform>>{
                override fun onResponse(call: Call<ArrayList<Platform>>,response: Response<ArrayList<Platform>>) {
                    if(response.isSuccessful){
                        Log.e("response",response.body()!!.size.toString())
                        listPlatform= response!!.body()!!
                        listPlatform.add(Platform(-1,"Not select"))
                        list_of_items_2 = listPlatform.map { it.name }.toTypedArray()
                        spinner_platform.setOnItemSelectedListener(
                            object : AdapterView.OnItemSelectedListener{
                                override fun onItemSelected(
                                    p0: AdapterView<*>?,
                                    p1: View?,
                                    p2: Int,
                                    p3: Long
                                ) {
                                    platform_id=listPlatform[p2].id
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    TODO("Not yet implemented")
                                }
                            }
                        )
                        if(list_of_items_2.size>0){
                            val platformAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list_of_items_2)
                            platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner_platform!!.setAdapter(platformAdapter)
                            spinner_platform!!.setSelection(platformAdapter.getPosition("Not select"))
                        }

                    }
                }
                override fun onFailure(call: Call<ArrayList<Platform>>, t: Throwable) {
                    Message.faildMsg()
                }

            }
        )
    }

    fun getAllTeacher(){
        Load.show(requireContext(),requireActivity())
        ApiService.create().getAllTeacher().enqueue(
            object :Callback<ArrayList<Teacher>>{
                override fun onResponse(call: Call<ArrayList<Teacher>>,response: Response<ArrayList<Teacher>>) {
                    Load.hide()
                    if(response.isSuccessful){
                        listTeacher=response!!.body()!!
                        teacherAdapter = TeacherAdapter(listTeacher,requireContext())
                        val layoutManager = GridLayoutManager(context,2)
                        rcv_teacher.layoutManager = layoutManager
                        rcv_teacher.adapter = teacherAdapter
                        teacherAdapter.setOnItemClickListener(object :TeacherAdapter.onItemClicklistener{
                            override fun onItemClick(position: Int) {
                                val bundle = Bundle();
                                bundle.putSerializable("teacher",listTeacher.get(position))
                                findNavController().navigate(R.id.action_teacherFragment_to_detailTeacherFragment,bundle)
                            }
                        })
                        teacherAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<ArrayList<Teacher>>, t: Throwable) {
                    Load.hide()
                    Message.faildMsg()
                }
            }
        )
    }

    fun search(){
        var platform = ""
        if(platform_id!=-1)
            platform = listPlatform.find { it.id==platform_id
            }!!.name
        var listTeacherSearch = listTeacher.filter { key ->
            var isResult = true
           if(!edt_teacher_search.text.toString().isEmpty())
               if(!key.teacher_name.contains(edt_teacher_search.text.toString(),ignoreCase = true))
                   isResult = false

           if(major_id!=-1&&isResult)
               if(key.major_id!=major_id)
                   isResult = false

           if(platform_id!=-1&&isResult)
               if(!key.platforms.contains(platform))
                   isResult = false
            isResult
            }
        teacherAdapter = TeacherAdapter(listTeacherSearch as ArrayList<Teacher>,requireContext())
        val layoutManager = GridLayoutManager(context,2)
        rcv_teacher.layoutManager = layoutManager
        rcv_teacher.adapter = teacherAdapter
        teacherAdapter.setOnItemClickListener(object :TeacherAdapter.onItemClicklistener{
            override fun onItemClick(position: Int) {
                val bundle = Bundle();
                bundle.putSerializable("teacher",listTeacherSearch.get(position))
                findNavController().navigate(R.id.action_teacherFragment_to_detailTeacherFragment,bundle)
            }
        })
        teacherAdapter.notifyDataSetChanged()
    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        major_id = listMajor[p2].id;
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}
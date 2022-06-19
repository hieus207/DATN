package com.example.qldatn

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.qldatn.Helper.Message
import com.example.qldatn.Helper.RealPathUtil
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Major
import com.example.qldatn.models.Student
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class InfoStudentFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")
    var major_id:Int = -1
    var student_id = -1
    private val pickImage = 100
    lateinit var spinner:Spinner
    lateinit var student: Student
    lateinit var edt_shstudent_fullname:EditText
    lateinit var edt_shstudent_cv:EditText
    lateinit var edt_shstudent_email:EditText
    lateinit var edt_shstudent_phone:EditText
    lateinit var img_shstudent_avt:ImageView
    lateinit var btn_shstudent_save:ImageView
    lateinit var btn_shstudent_cancel:ImageView
    lateinit var btn_shstudent_back:ImageView
    lateinit var btn_shstudent_edit:ImageButton
    lateinit var listMajor:ArrayList<Major>
    private var imageUri: Uri? = null
    private var file:File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_info_student, container, false)
        arguments?.let {
            student_id = it.getInt("student_id")
        }

        spinner = v.findViewById(R.id.spinner_sample)
        edt_shstudent_fullname = v.findViewById(R.id.edt_shstudent_fullname)
        edt_shstudent_cv = v.findViewById(R.id.edt_shstudent_cv)
        edt_shstudent_email = v.findViewById(R.id.edt_shstudent_email)
        edt_shstudent_phone = v.findViewById(R.id.edt_shstudent_phone)
        img_shstudent_avt = v.findViewById(R.id.img_shstudent_avt)
        btn_shstudent_save = v.findViewById(R.id.btn_shstudent_save)
        btn_shstudent_cancel = v.findViewById(R.id.btn_shstudent_cancel)
        btn_shstudent_back = v.findViewById(R.id.btn_shstudent_back)
        btn_shstudent_edit = v.findViewById(R.id.btn_shstudent_edit)

        initInfo()
        hideEdit()

        btn_shstudent_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        btn_shstudent_edit.setOnClickListener {
            showEdit()
        }
        btn_shstudent_cancel.setOnClickListener {
            hideEdit()
        }

        img_shstudent_avt.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        btn_shstudent_save.setOnClickListener {
            updateStudent()
            hideEdit()
        }

        if(student_id>0){
            btn_shstudent_edit.isInvisible = true;
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoStudentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initInfo(){
        val stid = if(student_id>0) student_id else MyApplication.id
        ApiService.create().getStudent(stid).enqueue(
            object :Callback<Student>{
                override fun onResponse(call: Call<Student>, response: Response<Student>) {
                    if(response.isSuccessful)
                    {
                        student = response!!.body()!!
                        initMajor()
                        setInfo(student)
                    }
                }

                override fun onFailure(call: Call<Student>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun initMajor(){
        ApiService.create().getAllMajor().enqueue(
            object :Callback<ArrayList<Major>>{
                override fun onResponse(call: Call<ArrayList<Major>>,response: Response<ArrayList<Major>>) {
                    if(response.isSuccessful){
                        listMajor= response!!.body()!!
                        list_of_items = listMajor.map { it.name.toString() }.toTypedArray()
                        spinner.setOnItemSelectedListener(this@InfoStudentFragment)
                        try{
                            var majorAdapter = ArrayAdapter(requireContext(), R.layout.simple_spinner, list_of_items)
//                        val majorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, list_of_items)
//                        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            majorAdapter.setDropDownViewResource(R.layout.checked_spinner_item)
                            spinner!!.setAdapter(majorAdapter)
                            spinner!!.setSelection(majorAdapter.getPosition(student.major))
                        }
                        catch(e: Exception){
                            Log.e("Loi","infostudent")
                        }
                    }
                }
                override fun onFailure(call: Call<ArrayList<Major>>, t: Throwable) {

                    Message.faildMsg()
                }
            }
        )
    }

    fun setInfo(std:Student){
        student.avt?.let {
            if(!it.isEmpty())
                Glide.with(requireContext()).load(student.avt).into(img_shstudent_avt)
        }
        edt_shstudent_fullname.setText(std.student_name)
        edt_shstudent_cv.setText(std.cv)
        edt_shstudent_email.setText(std.email)
        edt_shstudent_phone.setText(std.phone)
    }

    fun updateStudent(){
        var bodyFile = if (file==null)
            MultipartBody.Part.createFormData("avt","")
        else
            MultipartBody.Part.createFormData("avt",file!!.name, RequestBody.create(MediaType.parse("image/*"),file))

        if(major_id>-1&&checkData())
        ApiService.create().updateStudent(
            MyApplication.id,
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_shstudent_fullname.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_shstudent_email.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_shstudent_phone.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_shstudent_cv.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),major_id.toString()),
            bodyFile ,
            RequestBody.create(MediaType.parse("multipart/form-data"),MyApplication.accessToken)
        ).enqueue(
            object :Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful&&response.body()!!)
                        Message.msg("Cập nhật thông tin thành công")
                    else
                        Log.e("error","loi tren "+response.body().toString())
                        Message.faildMsg()
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.e("error","loi duoi "+t.toString())
                    Message.faildMsg()
                }
            }
        )
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        major_id = listMajor[p2].id;
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    fun checkData():Boolean{
        if(edt_shstudent_fullname.text.toString().length<10)
        {
            Message.msg("Họ và tên tối thiểu 10 ký tự")
            return false
        }
        if(edt_shstudent_email.text.toString().length<10)
        {
            Message.msg("Email tối thiểu 10 ký tự")
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            var realPath = RealPathUtil.getRealPath(requireContext(),imageUri!!)
            Log.e("uri", RealPathUtil.getRealPath(requireContext(),imageUri!!).toString())

            file = File(realPath)

//            var file:File = File(imageUri!!.path)
            img_shstudent_avt.setImageURI(imageUri)
        }
    }

    fun hideEdit(){
        btn_shstudent_save.isVisible = false;
        btn_shstudent_cancel.isVisible = false;
        btn_shstudent_edit.isVisible = true;
        edt_shstudent_fullname.isEnabled = false;
        spinner.isEnabled = false;
        edt_shstudent_cv.isEnabled = false;
        edt_shstudent_email.isEnabled = false;
        edt_shstudent_phone.isEnabled = false;
        img_shstudent_avt.isEnabled = false;
        initInfo()
    }

    fun showEdit(){
        btn_shstudent_save.isVisible = true;
        btn_shstudent_cancel.isVisible = true;
        btn_shstudent_edit.isVisible = false;
        edt_shstudent_fullname.isEnabled = true;
        spinner.isEnabled = true;
        edt_shstudent_cv.isEnabled = true;
        edt_shstudent_email.isEnabled = true;
        edt_shstudent_phone.isEnabled = true;
        img_shstudent_avt.isEnabled = true;
    }


}
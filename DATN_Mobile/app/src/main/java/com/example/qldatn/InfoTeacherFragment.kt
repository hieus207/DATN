package com.example.qldatn

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.qldatn.Helper.Message
import com.example.qldatn.Helper.RealPathUtil
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Major
import com.example.qldatn.models.Platform
import com.example.qldatn.models.Teacher
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

/**
 * A simple [Fragment] subclass.
 * Use the [InfoTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoTeacherFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val pickImage = 100
    private var imageUri: Uri? = null
    private var file:File? = null
    lateinit var edt_info_teacher_fullname:EditText
    lateinit var edt_info_teacher_birthday:EditText
    lateinit var edt_info_teacher_email:EditText
    lateinit var edt_info_teacher_phone:EditText
    lateinit var edt_info_teacher_direction:EditText
    lateinit var tv_info_teacher_platform:TextView
    lateinit var cv_info_teacher_platforms:CardView
    lateinit var teacher:Teacher
    lateinit var btn_info_teacher_save:ImageView
    lateinit var btn_info_teacher_cancel:ImageView
    lateinit var btn_info_teacher_back:ImageView
    lateinit var img_info_teacher_avt:ImageView
    lateinit var btn_info_teacher_edit:ImageButton
    lateinit var listMajor:ArrayList<Major>
    var list_of_items = arrayOf("Item 1", "Item 2", "Item 3")
    var major_id:Int = -1
    lateinit var spinner: Spinner

    lateinit var selectedPlatform:BooleanArray
    var platformList = ArrayList<RequestBody>()
    var platformArray = arrayOf<String>()
    var platformId = arrayOf<Int>()
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
        val v = inflater.inflate(R.layout.fragment_info_teacher, container, false)
        img_info_teacher_avt = v.findViewById(R.id.img_info_teacher_avt)
        edt_info_teacher_fullname = v.findViewById(R.id.edt_info_teacher_fullname)
        spinner = v.findViewById(R.id.spinner_info_teacher_major)
        edt_info_teacher_birthday = v.findViewById(R.id.edt_info_teacher_birthday)
        edt_info_teacher_email = v.findViewById(R.id.edt_info_teacher_email)
        edt_info_teacher_phone = v.findViewById(R.id.edt_info_teacher_phone)
        edt_info_teacher_direction = v.findViewById(R.id.edt_info_teacher_direction)
        tv_info_teacher_platform = v.findViewById(R.id.tv_info_teacher_platform)
        cv_info_teacher_platforms = v.findViewById(R.id.cv_info_teacher_platforms)
        btn_info_teacher_save = v.findViewById(R.id.btn_info_teacher_save)
        btn_info_teacher_cancel = v.findViewById(R.id.btn_info_teacher_cancel)
        btn_info_teacher_back = v.findViewById(R.id.btn_info_teacher_back)
        btn_info_teacher_edit = v.findViewById(R.id.btn_info_teacher_edit)

        initData();
        hideEdit();
        btn_info_teacher_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        btn_info_teacher_save.setOnClickListener {
            updateTeacher()

        }

        btn_info_teacher_cancel.setOnClickListener {
            hideEdit()
            initData()
        }
        img_info_teacher_avt.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

        btn_info_teacher_edit.setOnClickListener {
            showEdit()
        }
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initData(){
        ApiService.create().getTeacher(MyApplication.id).enqueue(
            object :Callback<Teacher>{
                override fun onResponse(call: Call<Teacher>, response: Response<Teacher>) {
                    if(response.isSuccessful){
                        teacher = response.body()!!
                        teacher.avt?.let {
                            if(!it.isEmpty())
                                Glide.with(requireContext()).load(teacher.avt).into(img_info_teacher_avt)
                        }

                        edt_info_teacher_fullname.setText(response.body()!!.teacher_name)
                        edt_info_teacher_birthday.setText(response.body()!!.birthday)
                        edt_info_teacher_email.setText(response.body()!!.email)
                        edt_info_teacher_phone.setText(response.body()!!.phone)
                        edt_info_teacher_direction.setText(response.body()!!.topic)
                        major_id = teacher.major_id
                        initPlatform();
                        initMajor();
                    }
                }

                override fun onFailure(call: Call<Teacher>, t: Throwable) {
                    Toast.makeText(context,"Không thể lấy dữ liệu kiểm tra đường truyền!",Toast.LENGTH_SHORT).show()
                }
            }
        )
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
                        cv_info_teacher_platforms.setOnClickListener {
                            showPlatformDialog()
                        }
                        selectedPlatform= BooleanArray(platformArray.size){false}
                        if(teacher.platforms!=null){
                            tv_info_teacher_platform.text=""
                            val selected = teacher.platforms.dropLast(1).split(",")
                            for (item in selected){
                                var index = platformArray.indexOf(item.trim())
                                if(index>=0){
                                    selectedPlatform[index]=true;
                                    tv_info_teacher_platform.text = "${tv_info_teacher_platform.text} ${platformArray[index]},"
                                }
                            }
                        }

                        if(tv_info_teacher_platform.text!="")
                            tv_info_teacher_platform.text=tv_info_teacher_platform.text.dropLast(1)
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
            tv_info_teacher_platform.text = ""
            for (i in 0 until platformArray.size){
                val checked = selectedPlatform[i]
                if(checked){
                    platformList.add(RequestBody.create(MediaType.parse("multipart/form-data"),platformId[i].toString()))
                    tv_info_teacher_platform.text = "${tv_info_teacher_platform.text} ${platformArray[i]},"
                }
            }

            if(tv_info_teacher_platform.text!="")
                tv_info_teacher_platform.text=tv_info_teacher_platform.text.dropLast(1)
        }
        dialog = builder.create()
        dialog.show()
    }

    fun initMajor(){
        ApiService.create().getAllMajor().enqueue(
            object :Callback<ArrayList<Major>>{
                override fun onResponse(call: Call<ArrayList<Major>>, response: Response<ArrayList<Major>>) {
                    if(response.isSuccessful){
                        listMajor= response!!.body()!!
                        Log.e("list major",listMajor.size.toString())
                        list_of_items = listMajor.map { it.name.toString() }.toTypedArray()
                        spinner.setOnItemSelectedListener(this@InfoTeacherFragment)
                        if(context!=null){
                            val majorAdapter = ArrayAdapter(requireContext(), R.layout.checked_spinner_item, list_of_items)
                            majorAdapter.setDropDownViewResource(R.layout.simple_spinner)
                            spinner!!.setAdapter(majorAdapter)
                            spinner!!.setSelection(majorAdapter.getPosition(teacher.major))
                        }

                    }
                }
                override fun onFailure(call: Call<ArrayList<Major>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

    }

    fun updateTeacher(){
        var bodyFile = if (file==null)
            MultipartBody.Part.createFormData("avt","")
        else
            MultipartBody.Part.createFormData("avt",file!!.name, RequestBody.create(MediaType.parse("image/*"),file))
        if(major_id>=0&&checkData())
        ApiService.create().updateTeacher(
            MyApplication.id,
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_info_teacher_fullname.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_info_teacher_email.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_info_teacher_birthday.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_info_teacher_phone.text.toString()),
            RequestBody.create(MediaType.parse("multipart/form-data"),edt_info_teacher_direction.text.toString()),
            bodyFile,
            RequestBody.create(MediaType.parse("multipart/form-data"),major_id.toString()),
            platformList,
            RequestBody.create(MediaType.parse("multipart/form-data"),MyApplication.accessToken)
        ).enqueue(
            object :Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if(response.body()!!){
                            Toast.makeText(context,"Cập nhật thành công",Toast.LENGTH_SHORT).show()
                            initData()
                            hideEdit()
                        }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Toast.makeText(context,"Cập nhật dữ liệu thất bại, kiểm tra đường truyền mạng",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    fun checkData():Boolean{
        if(edt_info_teacher_fullname.text.toString().length<10)
        {
            Message.msg("Họ và tên tối thiểu 10 ký tự")
            return false
        }
        if(edt_info_teacher_email.text.toString().length<10)
        {
            Message.msg("Email tối thiểu 10 ký tự")
            return false
        }
        return true
    }
    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        major_id = listMajor[p2].id;
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            var realPath = RealPathUtil.getRealPath(requireContext(),imageUri!!)
            Log.e("uri", RealPathUtil.getRealPath(requireContext(),imageUri!!).toString())

            file = File(realPath)

//            var file:File = File(imageUri!!.path)
            img_info_teacher_avt.setImageURI(imageUri)
        }
    }

    fun showEdit(){
        btn_info_teacher_edit.isVisible = false
        img_info_teacher_avt.isEnabled = true
        edt_info_teacher_fullname.isEnabled = true
        spinner.isEnabled = true
        edt_info_teacher_birthday.isEnabled = true
        edt_info_teacher_email.isEnabled = true
        edt_info_teacher_phone.isEnabled = true
        edt_info_teacher_direction.isEnabled = true
        tv_info_teacher_platform.isEnabled = true
        cv_info_teacher_platforms.isEnabled = true
        btn_info_teacher_save.isVisible = true
        btn_info_teacher_cancel.isVisible = true
    }

    fun hideEdit(){
        btn_info_teacher_edit.isVisible = true
        img_info_teacher_avt.isEnabled = false
        edt_info_teacher_fullname.isEnabled = false
        spinner.isEnabled = false
        edt_info_teacher_birthday.isEnabled = false
        edt_info_teacher_email.isEnabled = false
        edt_info_teacher_phone.isEnabled = false
        edt_info_teacher_direction.isEnabled = false
        tv_info_teacher_platform.isEnabled = false
        cv_info_teacher_platforms.isEnabled = false
        btn_info_teacher_save.isVisible = false
        btn_info_teacher_cancel.isVisible = false
    }

}
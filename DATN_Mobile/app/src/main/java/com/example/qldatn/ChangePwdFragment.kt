package com.example.qldatn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.example.qldatn.Helper.Message
import com.example.qldatn.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePwdFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePwdFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var edt_changepwd_currentpwd:EditText
    lateinit var edt_changepwd_newpwd:EditText
    lateinit var edt_changepwd_cfnewpwd:EditText
    lateinit var btn_changepwd_save:ImageView
    lateinit var btn_changepwd_cancel:ImageView
    lateinit var btn_changepwd_back:ImageView

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
        val v = inflater.inflate(R.layout.fragment_change_pwd, container, false)

        edt_changepwd_currentpwd = v.findViewById(R.id.edt_changepwd_currentpwd)
        edt_changepwd_newpwd = v.findViewById(R.id.edt_changepwd_newpwd)
        edt_changepwd_cfnewpwd = v.findViewById(R.id.edt_changepwd_cfnewpwd)
        btn_changepwd_save = v.findViewById(R.id.btn_changepwd_save)
        btn_changepwd_cancel = v.findViewById(R.id.btn_changepwd_cancel)
        btn_changepwd_back = v.findViewById(R.id.btn_changepwd_back)

        btn_changepwd_cancel.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btn_changepwd_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        btn_changepwd_save.setOnClickListener {
            changePassword()
        }
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ChangePwdFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun changePassword(){
        if(checkData())
        ApiService.create().changePwd(
            MyApplication.id,
            edt_changepwd_currentpwd.text.toString(),
            edt_changepwd_newpwd.text.toString(),
            MyApplication.accessToken
        ).enqueue(
            object : Callback<Boolean>{
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful)
                        if(response.body()!!)
                            Message.msg("Đổi mật khẩu thành công")
                        else
                            Message.msg("Đổi mật khẩu thất bại, kiểm tra lại mật khẩu cũ!")
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }

    fun checkData():Boolean{
        if(edt_changepwd_newpwd.text.toString().length<6){
            Message.msg("Mật khẩu mới cần tối thiểu 6 ký tự")
            return false
        }
        if(edt_changepwd_newpwd.text.toString()!=edt_changepwd_cfnewpwd.text.toString()){
            Message.msg("Xác nhận mật khẩu không trùng nhau")
            return false
        }
        return true
    }
}
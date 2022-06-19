package com.example.qldatn

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import com.example.qldatn.Helper.Load
import com.example.qldatn.Helper.Message
import com.example.qldatn.api.ApiService
import com.example.qldatn.api.response.ResLogin
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.security.MessageDigest

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    lateinit var layout_login_btn:CardView
    lateinit var edt_login_username:EditText
    lateinit var edt_login_password:EditText
    lateinit var bottom_menu: BottomNavigationView
    lateinit var bottom_teacher_menu: BottomNavigationView
    private var param1: String? = null
    private var param2: String? = null

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
        val v = inflater.inflate(R.layout.fragment_login, container, false)
        edt_login_username = v.findViewById(R.id.edt_login_username)
        edt_login_password = v.findViewById(R.id.edt_login_password)
        layout_login_btn = v.findViewById(R.id.layout_login_btn)
        bottom_menu= requireActivity()?.findViewById(R.id.bottom_menu)!!
        bottom_teacher_menu= requireActivity()?.findViewById(R.id.bottom_teacher_menu)!!
        layout_login_btn.setOnClickListener {
            Login();
        }
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun Login(){
        val username = edt_login_username.text.toString()
        val pass= edt_login_password.text.toString()

        if(checkData(username,pass)){
            Load.show(requireContext(),requireActivity())
            ApiService.create().login(username,pass.md5()).enqueue(object : Callback<ResLogin>{
                override fun onResponse(call: Call<ResLogin>, response: Response<ResLogin>) {
                    Load.hide()
                    if(response.isSuccessful){
                        var resLogin: ResLogin? =response.body()
                        if(resLogin!!.status){
                            MyApplication.id = resLogin!!.data!!.id!!
                            MyApplication.accessToken=resLogin!!.data!!.token!!
                            if(resLogin.data!!.role==0)
                                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                            else if(resLogin.data!!.role==1)
                                findNavController().navigate(R.id.action_loginFragment_to_homeTeacherFragment)
                        }
                        else{

                            Message.msg("Sai tên tài khoản hoặc mật khẩu")
                        }
                    }
                }
                override fun onFailure(call: Call<ResLogin>, t: Throwable) {
                    Load.hide()
                    Message.faildMsg()
                }
            })
        }
        else
            Message.msg("Độ dài tối thiểu cho username và pass là 6 ký tự")
    }

    fun checkData(username:String,pass:String):Boolean{
        if(username.length<6||pass.length<6)
            return false;
        return true;
    }
    fun String.md5(): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
    }

    override fun onResume() {
        super.onResume()
        bottom_menu.isInvisible = true;
        bottom_teacher_menu.isInvisible = true;
    }
}
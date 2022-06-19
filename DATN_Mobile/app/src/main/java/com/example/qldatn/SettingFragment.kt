package com.example.qldatn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cv_setting_info: CardView
    lateinit var cv_setting_logout: CardView
    lateinit var cv_setting_changepwd: CardView
    lateinit var cv_setting_mypropose: CardView

    lateinit var bottom_menu: BottomNavigationView

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
        val v = inflater.inflate(R.layout.fragment_setting, container, false)
        bottom_menu= requireActivity()?.findViewById(R.id.bottom_menu)!!
        cv_setting_info = v.findViewById(R.id.cv_setting_info)
        cv_setting_changepwd = v.findViewById(R.id.cv_setting_changepwd)
        cv_setting_mypropose = v.findViewById(R.id.cv_setting_mypropose)
        cv_setting_logout = v.findViewById(R.id.cv_setting_logout)
        cv_setting_info.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_infoStudentFragment)
        }

        cv_setting_mypropose.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_mySuggestFragment)
        }

        cv_setting_changepwd.setOnClickListener {
            findNavController().navigate(R.id.action_settingFragment_to_changePwdFragment)
        }
        cv_setting_logout.setOnClickListener {
            Logout();
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun Logout(){
        MyApplication.id=-1;
        MyApplication.accessToken=""
        bottom_menu.isInvisible=true;
        findNavController().navigate(R.id.action_settingFragment_to_loginFragment)
        super.onDestroy();
    }
}
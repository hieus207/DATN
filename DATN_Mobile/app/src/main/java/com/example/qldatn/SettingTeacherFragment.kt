package com.example.qldatn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cv_setting_teacher_info:CardView
    lateinit var cv_setting_teacher_changepwd:CardView
    lateinit var cv_setting_teacher_logout:CardView

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
        val v = inflater.inflate(R.layout.fragment_setting_teacher, container, false)
        cv_setting_teacher_info = v.findViewById(R.id.cv_setting_teacher_info)
        cv_setting_teacher_changepwd = v.findViewById(R.id.cv_setting_teacher_changepwd)
        cv_setting_teacher_logout = v.findViewById(R.id.cv_setting_teacher_logout)
        cv_setting_teacher_info.setOnClickListener {
            findNavController().navigate(R.id.action_settingTeacherFragment_to_infoTeacherFragment)
        }

        cv_setting_teacher_changepwd.setOnClickListener {
            findNavController().navigate(R.id.action_settingTeacherFragment_to_changePwdFragment)
        }
        cv_setting_teacher_logout.setOnClickListener {
            MyApplication.id=-1
            MyApplication.accessToken=""
            findNavController().navigate(R.id.action_settingTeacherFragment_to_loginFragment)
        }
        return v
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
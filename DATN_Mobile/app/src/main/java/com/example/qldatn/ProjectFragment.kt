package com.example.qldatn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.qldatn.Helper.Message
import com.example.qldatn.api.ApiService
import com.example.qldatn.api.response.ResProject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProjectFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProjectFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var cv_project_add:CardView

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
        val v = inflater.inflate(R.layout.fragment_project, container, false)
        cv_project_add = v.findViewById(R.id.cv_project_add);
        cv_project_add.isInvisible=true;
        cv_project_add.setOnClickListener {
            findNavController().navigate(R.id.action_projectFragment_to_createProjectFragment)
        }
        return  v;
    }

    override fun onResume() {
        super.onResume()
        checkProject();
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProjectFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun checkProject(){
        if(MyApplication.id>=0)
        ApiService.create().getProject(MyApplication.id,MyApplication.accessToken).enqueue(
            object : Callback<ResProject>{
                override fun onResponse(call: Call<ResProject>, response: Response<ResProject>) {
                    if(response.isSuccessful){
                        val resProject: ResProject? = response!!.body()
                        if(resProject!!.status){
//                            Truyen data
                            val project = response!!.body()!!.data;
                            val bundle = Bundle()
                            bundle.putSerializable("project",project)
                            findNavController().navigate(R.id.action_projectFragment_to_detailProjectFragment,bundle)
                        }
                        else{
                            cv_project_add.isInvisible=false;
                        }
                    }
                }

                override fun onFailure(call: Call<ResProject>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
}
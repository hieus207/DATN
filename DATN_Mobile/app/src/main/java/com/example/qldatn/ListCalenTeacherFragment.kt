package com.example.qldatn

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.adapter.StudentAdapter
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Proposal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListCalenTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListCalenTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rcv_listcalen_teacher: RecyclerView
    lateinit var btn_listcalen_teacher_back: ImageView
    var listProposal = ArrayList<Proposal>()
    private lateinit var studentAdapter: StudentAdapter

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
        val v = inflater.inflate(R.layout.fragment_list_calen_teacher, container, false)
        rcv_listcalen_teacher = v.findViewById(R.id.rcv_listcalen_teacher)
        btn_listcalen_teacher_back = v.findViewById(R.id.btn_listcalen_teacher_back)

        btn_listcalen_teacher_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        initProposal()
        return v;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListCalenTeacherFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListCalenTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun initProposal(){
        ApiService.create().getProposal(MyApplication.id,"teacher",1).enqueue(
            object : Callback<ArrayList<Proposal>> {
                override fun onResponse(call: Call<ArrayList<Proposal>>, response: Response<ArrayList<Proposal>>) {
                    if(response.isSuccessful){
                        listProposal = response.body()!!
                        studentAdapter= StudentAdapter(listProposal)
                        val layoutManager = LinearLayoutManager(context)
                        rcv_listcalen_teacher.layoutManager = layoutManager
                        rcv_listcalen_teacher.adapter = studentAdapter
                        studentAdapter.setOnItemClickListener(
                            object : StudentAdapter.onItemClicklistener{
                                override fun onItemClick(position: Int) {
                                    val bundle = Bundle()
                                    bundle.putInt("student_id",listProposal[position].student_id)
                                   findNavController().navigate(R.id.action_listCalenTeacherFragment_to_detailCalenTeacherFragment,bundle)
                                }
                            }
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<Proposal>>, t: Throwable) {
                    Toast.makeText(context,"Lấy dữ liệu đề xuất thất bại kiểm tra mạng", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
package com.example.qldatn

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.Helper.Message
import com.example.qldatn.adapter.ProposalAdapter
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
 * Use the [SuggestTeacherFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SuggestTeacherFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rcv_suggest_teacher:RecyclerView
    lateinit var cv_suggest_teacher_council:CardView
    lateinit var ibtn_suggest_teacher_council:ImageButton
    lateinit var ibtn_suggest_teacher_back:ImageButton
    lateinit var tv_suggest_teacher_title:TextView

    var listProposal = ArrayList<Proposal>()
    var listProposal2 = ArrayList<Proposal>()
    private lateinit var proposalAdapter: ProposalAdapter

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
        val v = inflater.inflate(R.layout.fragment_suggest_teacher, container, false)
        rcv_suggest_teacher = v.findViewById(R.id.rcv_suggest_teacher)
        cv_suggest_teacher_council = v.findViewById(R.id.cv_suggest_teacher_council)
        ibtn_suggest_teacher_council = v.findViewById(R.id.ibtn_suggest_teacher_council)
        ibtn_suggest_teacher_back = v.findViewById(R.id.ibtn_suggest_teacher_back)
        tv_suggest_teacher_title = v.findViewById(R.id.tv_suggest_teacher_title)
        cv_suggest_teacher_council.isInvisible=true
        ibtn_suggest_teacher_council.isInvisible=true
        ibtn_suggest_teacher_back.isInvisible=true
        checkHaveReview()
        checkCouncil()
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SuggestTeacherFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    fun checkHaveReview(){
        ApiService.create().getStudentReview(MyApplication.id).enqueue(
            object : Callback<ArrayList<Proposal>>{
                override fun onResponse(
                    call: Call<ArrayList<Proposal>>,
                    response: Response<ArrayList<Proposal>>
                ) {
                    if(response.isSuccessful){
                        listProposal = response.body()!!
                        if(listProposal.size>0){
                            proposalAdapter= ProposalAdapter(listProposal)
                            val layoutManager = LinearLayoutManager(context)
                            rcv_suggest_teacher.layoutManager = layoutManager
                            rcv_suggest_teacher.adapter = proposalAdapter
                            tv_suggest_teacher_title.text = "Danh sách sinh viên phản biện"
                            Message.msg("Bạn có sinh viên cần phản biện")
                            proposalAdapter.setOnItemClickListener(
                                object : ProposalAdapter.onItemClicklistener{
                                    override fun onItemClick(position: Int) {
                                        var bundle = Bundle()
                                        bundle.putInt("project_id",listProposal[position].project_id)
                                        bundle.putInt("student_id",listProposal[position].student_id)
                                        bundle.putString("student_name",listProposal[position].student_name)
                                        bundle.putString("from","projectFragment")

                                        findNavController().navigate(R.id.action_suggestTeacherFragment_to_detailSuggestFragment,bundle)
                                    }
                                }
                            )
                        }
                        else{
                            initProposal();
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Proposal>>, t: Throwable) {
                    Message.faildMsg()

                    initProposal()
                }
            }
        )
    }
    fun initProposal(){
        ApiService.create().getProposal(MyApplication.id,"teacher",0).enqueue(
            object : Callback<ArrayList<Proposal>> {
                override fun onResponse(call: Call<ArrayList<Proposal>>, response: Response<ArrayList<Proposal>>) {
                    if(response.isSuccessful){
                        listProposal = response.body()!!
                        proposalAdapter= ProposalAdapter(listProposal)
                        val layoutManager = LinearLayoutManager(context)
                        rcv_suggest_teacher.layoutManager = layoutManager
                        rcv_suggest_teacher.adapter = proposalAdapter
                        tv_suggest_teacher_title.text = "Danh sách đề xuất"
                        proposalAdapter.setOnItemClickListener(
                            object : ProposalAdapter.onItemClicklistener{
                                override fun onItemClick(position: Int) {
                                    var bundle = Bundle()
                                    bundle.putInt("project_id",listProposal[position].project_id)
                                    bundle.putInt("student_id",listProposal[position].student_id)
                                    bundle.putString("student_name",listProposal[position].student_name)
                                    bundle.putString("from","suggestFragment")
                                    findNavController().navigate(R.id.action_suggestTeacherFragment_to_detailSuggestFragment,bundle)
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

    fun checkCouncil(){
        ApiService.create().getTeacherCouncil(MyApplication.id).enqueue(
            object : Callback<ArrayList<Proposal>>{
                override fun onResponse(call: Call<ArrayList<Proposal>>,response: Response<ArrayList<Proposal>>
                ) {
                    response.body()?.let{
                        if(response.body()!!.size>0){
                            listProposal2 = response.body()!!
                            cv_suggest_teacher_council.isInvisible = false;
                            ibtn_suggest_teacher_council.isInvisible = false;
                            ibtn_suggest_teacher_council.setOnClickListener {
                                listProposal = listProposal2
                                proposalAdapter= ProposalAdapter(listProposal)
                                val layoutManager = LinearLayoutManager(context)
                                rcv_suggest_teacher.layoutManager = layoutManager
                                rcv_suggest_teacher.adapter = proposalAdapter
                                tv_suggest_teacher_title.text = "Danh sách sinh viên phản biện"

                                proposalAdapter.setOnItemClickListener(
                                    object : ProposalAdapter.onItemClicklistener{
                                        override fun onItemClick(position: Int) {
                                            var bundle = Bundle()
                                            bundle.putInt("project_id",listProposal[position].project_id)
                                            bundle.putInt("student_id",listProposal[position].student_id)
                                            bundle.putString("student_name",listProposal[position].student_name)
                                            bundle.putString("from","projectFragment")

                                            findNavController().navigate(R.id.action_suggestTeacherFragment_to_detailSuggestFragment,bundle)
                                        }
                                    }
                                )
                                cv_suggest_teacher_council.isInvisible=true
                                ibtn_suggest_teacher_council.isInvisible=true
                                ibtn_suggest_teacher_back.isVisible=true
                            }
                            ibtn_suggest_teacher_back.setOnClickListener {
                                ibtn_suggest_teacher_council.isInvisible=false
                                cv_suggest_teacher_council.isInvisible=false
                                ibtn_suggest_teacher_back.isVisible=false
                                checkHaveReview()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<ArrayList<Proposal>>, t: Throwable) {
                    Message.faildMsg()
                }
            }
        )
    }
}
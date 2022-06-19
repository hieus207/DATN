package com.example.qldatn

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.qldatn.adapter.ProposalAdapter
import com.example.qldatn.adapter.ScheduleAdapter
import com.example.qldatn.api.ApiService
import com.example.qldatn.models.Proposal
import com.example.qldatn.models.Schedule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MySuggestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MySuggestFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rcv_mysuggest:RecyclerView
    lateinit var btn_mysuggest_back:ImageView
    var listProposal = ArrayList<Proposal>()
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
        val v = inflater.inflate(R.layout.fragment_my_suggest, container, false)
        rcv_mysuggest = v.findViewById(R.id.rcv_mysuggest)
        btn_mysuggest_back = v.findViewById(R.id.btn_mysuggest_back)
        btn_mysuggest_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
        initProposal()
        return v;
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MySuggestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun initProposal(){
        ApiService.create().getProposal(
            MyApplication.id,
            "student",
            0
        ).enqueue(
            object :Callback<ArrayList<Proposal>>{
                override fun onResponse(call: Call<ArrayList<Proposal>>,response: Response<ArrayList<Proposal>> ) {
                    if(response.isSuccessful){
                        listProposal = response.body()!!
                        proposalAdapter= ProposalAdapter(listProposal)
                        val layoutManager = LinearLayoutManager(context)
                        rcv_mysuggest.layoutManager = layoutManager
                        rcv_mysuggest.adapter = proposalAdapter
                        proposalAdapter.setOnItemClickListener(
                            object : ProposalAdapter.onItemClicklistener{
                                override fun onItemClick(position: Int) {

                                }
                            }
                        )
                    }
                }

                override fun onFailure(call: Call<ArrayList<Proposal>>, t: Throwable) {
                    Toast.makeText(context,"Lấy dữ liệu đề xuất thất bại kiểm tra mạng",Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}
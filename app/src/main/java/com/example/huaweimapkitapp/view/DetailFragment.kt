package com.example.huaweimapkitapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.huaweimapkitapp.Connection
import com.example.huaweimapkitapp.adapter.ChargeAdapter
import com.example.huaweimapkitapp.databinding.FragmentDetailBinding




class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding
    private var recyclerViewAdapter: ChargeAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().setTitle("Location Details")
       
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        takeLocationInfo()
    }

    companion object {
        private const val TAG = "DetailFragment"
    }

    fun takeLocationInfo(){
        arguments?.let {
            val marker = DetailFragmentArgs.fromBundle(it)



            binding.locationTv.text = "${marker.marker.addressInfo!!.latitude} , ${marker.marker.addressInfo!!.longitude} "
            binding.addressTv.text= "${marker.marker.addressInfo!!.addressLine1} ${marker.marker.addressInfo!!.town}  ${marker.marker.addressInfo!!.country!!.title}"
            binding.mailTv.text ="${marker.marker.operatorInfo!!.webSiteURL}"

            recyclerViewAdapter = ChargeAdapter(marker.marker.connections as ArrayList<Connection>)
            binding.recyclerView.adapter = recyclerViewAdapter
        }
    }
}
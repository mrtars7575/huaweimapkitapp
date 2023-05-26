package com.example.huaweimapkitapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.huaweimapkitapp.databinding.FragmentDetailBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class DetailFragment : Fragment() {

    private lateinit var binding : FragmentDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       
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
        }
    }
}
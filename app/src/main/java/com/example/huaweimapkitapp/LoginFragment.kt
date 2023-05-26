package com.example.huaweimapkitapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.huaweimapkitapp.databinding.FragmentLoginBinding
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userInfo : String

    companion object{
        private const val TAG = "LoginFragement"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle("180101002")

        binding.huaweiIdAuthButton.setOnClickListener {
            print("viewas" + view)

            signInWithHuawei()
        }

    }

    fun signInWithHuawei(){
        val authParams : AccountAuthParams =  AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setAuthorizationCode().createParams()
        val service : AccountAuthService = AccountAuthManager.getService(requireActivity(), authParams)
        startActivityForResult(service.signInIntent, 8888)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Process the authorization result to obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 8888) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                val authAccount = authAccountTask.result
                Log.i(TAG, "serverAuthCode:" + authAccount.authorizationCode)
                if(authAccount.displayName!=null){
                    userInfo = authAccount.displayName
                }
                // go to search fragment
                navigateToSearchFragment()

            } else {
                // The sign-in failed.
                Log.e(TAG, "sign in failed:" + (authAccountTask.exception as ApiException).statusCode)

                Toast.makeText(requireContext(),(authAccountTask.exception as ApiException).statusCode,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToSearchFragment(){
        val action = LoginFragmentDirections.actionLoginFragmentToSearchFragment(userInfo)
        Navigation.findNavController(binding.huaweiIdAuthButton).navigate(action)
    }


}
package com.gregorchristiaens.karatelessons.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.gregorchristiaens.karatelessons.FireBaseOptions
import com.gregorchristiaens.karatelessons.R
import com.gregorchristiaens.karatelessons.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val logTag = "KLT.LoginFragment"
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        googleSignInOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(FireBaseOptions().serverClientId)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.google.setOnClickListener {
            googleSignIn()
        }
        binding.register.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.forgot.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        viewModel.toProfile.observe(viewLifecycleOwner) {
            if (it) Navigation.findNavController(binding.root)
                .navigate(R.id.action_loginFragment_to_profileFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val RC_SIGN_IN = 120
    }

    private lateinit var googleSignInOptions: GoogleSignInOptions
    private lateinit var googleSignInClient: GoogleSignInClient

    private fun googleSignIn() {
        val signInIntent = googleSignInClient.signInIntent
        Log.d(logTag, "Starting google signIn intent")
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            viewModel.googleSignIn(task, exception)
        }
    }
}
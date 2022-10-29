package com.gregorchristiaens.karatelessons.startup

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.gregorchristiaens.karatelessons.R
import com.gregorchristiaens.karatelessons.databinding.FragmentStartupBinding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class StartUpFragment : Fragment() {

    private val logTag = "KLT.StartUpFragment"
    private var _binding: FragmentStartupBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: StartUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[StartUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStartupBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectLatestLifecycleFlow(viewModel.load) { percent: Int ->
            if (percent < 10) binding.progressSpinner.visibility = View.VISIBLE
            else binding.progressSpinner.visibility = View.GONE
        }
        viewModel.skipLogin.observe(viewLifecycleOwner) {
            if (it) Navigation.findNavController(binding.root)
                .navigate(R.id.action_startUpFragment_to_profileFragment)
        }
        viewModel.toLogin.observe(viewLifecycleOwner) {
            if (it) Navigation.findNavController(binding.root)
                .navigate(R.id.action_startUpFragment_to_loginFragment)
        }
        /*
        val displayMetrics = requireContext().resources.displayMetrics
        val dpHeight = displayMetrics.heightPixels / displayMetrics.density
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        Log.d(logTag,"Height = $dpHeight  || Width = $dpWidth")
         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun <T> Fragment.collectLatestLifecycleFlow(
        flow: Flow<T>,
        collect: suspend (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                flow.collect(collect)
            }
        }
    }
}
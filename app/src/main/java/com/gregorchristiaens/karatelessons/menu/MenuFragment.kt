package com.gregorchristiaens.karatelessons.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.gregorchristiaens.karatelessons.R
import com.gregorchristiaens.karatelessons.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private val logTag = "KLT.MenuFragment"
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MenuViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MenuViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.menuLessonsButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_menuFragment_to_lessonFragment)
        }
        binding.menuProfileButton.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_menuFragment_to_profileFragment)

        }
        binding.menuCheckIn.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_menuFragment_to_lessonCheckInFragment)
        }
        viewModel.toLogin.observe(viewLifecycleOwner) {
            if (it) {
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_menuFragment_to_loginFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
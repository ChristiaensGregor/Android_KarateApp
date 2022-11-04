package com.gregorchristiaens.karatelessons.lesson_check_in

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gregorchristiaens.karatelessons.databinding.FragmentLessonCheckInBinding


class LessonCheckInFragment : Fragment() {

    private var _binding: FragmentLessonCheckInBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LessonCheckInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LessonCheckInViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonCheckInBinding.inflate(inflater, container, false)
        //binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
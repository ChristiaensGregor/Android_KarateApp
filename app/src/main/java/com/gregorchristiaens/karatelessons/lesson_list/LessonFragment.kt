package com.gregorchristiaens.karatelessons.lesson_list

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.gregorchristiaens.karatelessons.databinding.FragmentLessonListBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LessonFragment : Fragment() {

    private var _binding: FragmentLessonListBinding? = null
    private val binding: FragmentLessonListBinding get() = _binding!!

    private lateinit var viewModel: LessonViewModel
    private lateinit var adapter: LessonRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LessonViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonListBinding.inflate(inflater, container, false)
        adapter = LessonRecyclerViewAdapter()
        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.lessons.observe(viewLifecycleOwner) { lessons ->
            var sortedLessons = lessons
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                sortedLessons = lessons.sortedBy { lesson -> LocalDate.parse(lesson.date, formatter) }
            }
            var validLessons = sortedLessons
            validLessons = validLessons.filter { lesson -> !lesson.expired }
            lessons.apply { adapter.lessons = validLessons }
        }
    }
}
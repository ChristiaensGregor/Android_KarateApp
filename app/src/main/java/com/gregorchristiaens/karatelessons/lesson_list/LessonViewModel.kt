package com.gregorchristiaens.karatelessons.lesson_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gregorchristiaens.karatelessons.domain.Lesson
import com.gregorchristiaens.karatelessons.repository.LessonRepository

class LessonViewModel : ViewModel() {
    private val lessonRepository = LessonRepository.getInstance()
    val lessons: LiveData<List<Lesson>> get() = lessonRepository.lessons

    init {
    }
}
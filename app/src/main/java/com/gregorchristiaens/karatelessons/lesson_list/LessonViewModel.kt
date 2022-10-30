package com.gregorchristiaens.karatelessons.lesson_list


import androidx.lifecycle.ViewModel
import com.gregorchristiaens.karatelessons.repository.LessonRepository

class LessonViewModel : ViewModel() {
    private val lessonRepository = LessonRepository.getInstance()
    val lessons get() = lessonRepository.lessons
}
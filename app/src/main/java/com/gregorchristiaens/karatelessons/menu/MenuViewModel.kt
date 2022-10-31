package com.gregorchristiaens.karatelessons.menu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.karatelessons.repository.LessonRepository
import com.gregorchristiaens.karatelessons.repository.UserRepository

class MenuViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val lessonRepository = LessonRepository.getInstance()

    private var _toLogin = MutableLiveData(false)
    val toLogin: LiveData<Boolean> get() = _toLogin

    init {
        lessonRepository.getLessons()
    }

    fun logout() {
        auth.signOut()
        _toLogin.value = true
    }
}
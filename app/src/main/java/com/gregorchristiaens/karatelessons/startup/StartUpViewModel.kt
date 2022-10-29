package com.gregorchristiaens.karatelessons.startup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.karatelessons.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StartUpViewModel : ViewModel() {

    private val tag = "KLT.StartUpViewModel"
    private val userRepository = UserRepository.getInstance()

    //Async Counter flow to simulate loading data
    private var _load = MutableStateFlow(10)
    val load: StateFlow<Int> = _load.asStateFlow()

    private var doneLoading = false
    private var userFound = false
    private var _skipLogin = MutableLiveData(false)
    val skipLogin: LiveData<Boolean> get() = _skipLogin
    private var _toLogin = MutableLiveData(false)
    val toLogin: LiveData<Boolean> get() = _toLogin

    private val auth = FirebaseAuth.getInstance()

    init {
        val loggedInUser = auth.currentUser
        if (loggedInUser != null) {
            doneLoading = false
            userFound = true
            loadData()
            userRepository.getUser(loggedInUser.uid)
            _skipLogin.value = userFound && doneLoading
        } else {
            _toLogin.value = true
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            for (i in 0..10) {
                _load.emit(i)
                delay(400L)
            }
            delay(700L)
            Log.d(tag, "Done loading")
            doneLoading = true
            _skipLogin.value = userFound && doneLoading
        }
    }
}
package com.gregorchristiaens.karatelessons.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.gregorchristiaens.karatelessons.domain.User
import com.gregorchristiaens.karatelessons.repository.UserRepository

class RegisterViewModel : ViewModel() {

    private val tag = "KLT.RegisterViewModel"

    var userName = MutableLiveData<String>()
    private var _userNameError = MutableLiveData<String>()
    val userNameError: LiveData<String> get() = _userNameError

    var email = MutableLiveData<String>()
    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError

    var password = MutableLiveData<String>()
    private var _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError

    var repeatPassword = MutableLiveData<String>()
    private var _repeatPasswordError = MutableLiveData<String>()
    val repeatPasswordError: LiveData<String> get() = _repeatPasswordError

    private var _toProfile = MutableLiveData(false)
    val toProfile: LiveData<Boolean> get() = _toProfile

    private val userRepository = UserRepository.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun register() {
        val userName = userName.value
        val email = email.value
        val password = password.value
        val repeat = repeatPassword.value
        if (validateEmail(email) &&
            validatePassword(password) &&
            validateRepeatPassword(repeat) &&
            email != null && password != null && repeat != null && userName != null
        ) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val newUser = task.result.user
                    if (newUser != null) userRepository.addUser(
                        User(
                            newUser.uid,
                            email,
                            userName,
                            null,
                            null,
                            null
                        )
                    )
                    _toProfile.value = true
                } else {
                    val message = task.exception?.message.toString()
                    if (message.contains("password")) {
                        _passwordError.value = message
                    } else {
                        _emailError.value = message
                    }
                    Log.d(tag, task.exception?.message.toString())
                }
            }
        }
    }

    private fun validateEmail(email: String?): Boolean {
        if (email.isNullOrEmpty() || email == "" || email.trim() == "") {
            _emailError.value = "Email can not be empty"
            emailError.value?.let { Log.d(tag, it) }
            return false
        }
        _emailError.value = ""
        return true
    }

    private fun validatePassword(password: String?): Boolean {
        if (password.isNullOrEmpty() || password == "" || password.trim() == "") {
            _passwordError.value = "Password can not be empty"
            passwordError.value?.let { Log.d(tag, it) }
            return false
        }
        _passwordError.value = ""
        return true
    }

    private fun validateRepeatPassword(repeatPassword: String?): Boolean {
        if (repeatPassword.isNullOrEmpty() || repeatPassword == "" || repeatPassword.trim() == "") {
            _repeatPasswordError.value = "Password repeat can not be empty"
            repeatPasswordError.value?.let { Log.d(tag, it) }
            return false
        } else if (repeatPassword != password.value) {
            _repeatPasswordError.value = "The two entered passwords must match"
            repeatPasswordError.value?.let { Log.d(tag, it) }
            return false
        }
        _repeatPasswordError.value = ""
        return true
    }
}
package com.gregorchristiaens.karatelessons.forgot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel() {

    private val logTag = "KLT.ForgotPasswordVM"
    var email = MutableLiveData<String>()
    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError!!

    private val auth = FirebaseAuth.getInstance()

    fun forgotPassword() {
        val email = email.value
        if (!email.isNullOrEmpty()) {
            auth.sendPasswordResetEmail(email)
        }
    }
}
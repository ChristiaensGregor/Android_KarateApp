package com.gregorchristiaens.karatelessons.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.gregorchristiaens.karatelessons.domain.User
import com.gregorchristiaens.karatelessons.repository.LessonRepository
import com.gregorchristiaens.karatelessons.repository.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val logTag = "KLT.ProfileViewModel"

    val user: LiveData<User> get() = userRepository.user

    private val userRepository = UserRepository.getInstance()
    private val auth = FirebaseAuth.getInstance()


    private var _toLogin = MutableLiveData(false)
    val toLogin: LiveData<Boolean> get() = _toLogin

    private var _toast = MutableLiveData<String>()
    val toast: LiveData<String> get() = _toast

    init {
    }

    fun leaveClub() {
        Log.d(logTag, "Leave club")
    }

    fun logout() {
        auth.signOut()
        _toLogin.value = true
    }

    fun deleteAccount() {
        Log.d(logTag, "Delete user account")
        userRepository.deleteCurrentUser()
        auth.currentUser?.delete()?.addOnSuccessListener {
            Log.d(logTag, "Delete users account")
            logout()
        }?.addOnFailureListener {
            Log.d(logTag, "Failed to delete users account")
            Log.d(logTag, it.toString())
            Log.d(logTag, it.message.toString())
            if (it.javaClass == FirebaseAuthRecentLoginRequiredException::class.java) {
                revalidateCredentials()
            }
        }
    }

    private fun revalidateCredentials() {
        viewModelScope.launch {
            _toast.value =
                "In order to delete your account we need to verify this is really you."
            delay(2000L)
            _toast.value = "We will log you out, please login again and try again."
            delay(2000L)
            logout()
        }
    }
}
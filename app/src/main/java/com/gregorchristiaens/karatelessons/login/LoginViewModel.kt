package com.gregorchristiaens.karatelessons.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.gregorchristiaens.karatelessons.domain.User
import com.gregorchristiaens.karatelessons.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val logTag = "KLT.LoginViewModel"
    var email = MutableLiveData<String>()
    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String> get() = _emailError

    var password = MutableLiveData<String>()
    private var _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String> get() = _passwordError

    private val auth = FirebaseAuth.getInstance()
    private val userRepository = UserRepository.getInstance()

    private var _toProfile = MutableLiveData(false)
    val toProfile: LiveData<Boolean> get() = _toProfile

    fun login() {
        val email = email.value
        val password = password.value
        if (validateEmail(email) && validatePassword(password) && email != null && password != null) {
            Log.d(logTag, "Valid email & password format")
            viewModelScope.launch {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(logTag, "Login Successful")
                        auth.currentUser?.let { userRepository.getUser(it.uid) }
                        _toProfile.value = true
                    } else {
                        val message = task.exception?.message.toString()
                        if (message.contains("password")) {
                            _passwordError.value = message
                        } else {
                            _emailError.value = message
                        }
                    }
                }
            }
        }
    }

    private fun validateEmail(email: String?): Boolean {
        if (email.isNullOrEmpty() || email == "" || email.trim() == "") {
            _emailError.value = "Email can not be empty"
            emailError.value?.let { Log.d(logTag, it) }
            return false
        }
        _emailError.value = ""
        return true
    }

    private fun validatePassword(password: String?): Boolean {
        if (password.isNullOrEmpty() || password == "" || password.trim() == "") {
            _passwordError.value = "Password can not be empty"
            passwordError.value?.let { Log.d(logTag, it) }
            return false
        }
        _passwordError.value = ""
        return true
    }

    fun googleSignIn(task: Task<GoogleSignInAccount>, exception: Exception?) {
        if (task.isSuccessful) {
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(logTag, "firebaseAuthWithGoogle:" + account.id)
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener { signInAttempt ->
                        if (signInAttempt.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(logTag, "signInWithCredential:success")
                            val user = auth.currentUser
                            if (user != null && user.displayName != null && user.email != null) {
                                val userData =
                                    User(user.uid, user.email!!, "googlePW", null, null, null)
                                userRepository.checkUser(user.uid, userData)
                            }
                            _toProfile.value = true
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(
                                logTag,
                                "signInWithCredential:failure",
                                signInAttempt.exception
                            )
                            //Update ui no user
                        }
                    }
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(logTag, "Google sign in failed", e)
                //_loginError.value = "Google sign in failed, please try again"
            }
        } else {
            Log.w(logTag, exception)
            //_loginError.value = "Could not connect to Google Play Services"
        }
    }


}
package com.gregorchristiaens.karatelessons.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.gregorchristiaens.karatelessons.domain.User
import java.lang.IllegalArgumentException

class UserRepository : Repository() {

    private val logTag = "KLT.UserRepository"
    override val childPaths: ArrayList<String> get() = arrayListOf("users")
    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun addUser(user: User) {
        database.child(user.id).setValue(user).addOnCompleteListener {
            Log.d(logTag, "Adding user to remote database")
            _user.value = user
        }.addOnFailureListener { exception ->
            Log.d(logTag, exception.message.toString())
        }
    }

    fun getUser(id: String) {
        database.child(id).get().addOnSuccessListener {
            Log.d(logTag, "Getting User from Database")
            try {
                val user = it.getValue(User::class.java)
                if (user != null) {
                    _user.value = user as User
                    Log.d(logTag, "Got User:${user}")
                } else {
                    throw IllegalArgumentException("Could not convert the database object to the local User class")
                }
            } catch (ex: IllegalArgumentException) {
                Log.d(logTag, ex.message.toString())
            }
        }.addOnFailureListener { exception ->
            Log.d(logTag, exception.message.toString())
        }
    }

    fun checkUser(id: String, userData: User) {
        database.child(id).get().addOnSuccessListener {
            Log.d(logTag, "Checking if user has data in Database")
            try {
                val existingUser = it.getValue(User::class.java)
                if (existingUser != null) {
                    _user.value = existingUser as User
                    Log.d(logTag, "Got User:${existingUser}")
                } else {
                    addUser(userData)
                }
            } catch (ex: IllegalArgumentException) {
                Log.d(logTag, ex.message.toString())
            }
        }.addOnFailureListener { exception ->
            Log.d(logTag, exception.message.toString())
        }
    }

    fun deleteCurrentUser() {
        val user = user.value
        if (user != null) {
            database.child(user.id).removeValue().addOnSuccessListener {
                Log.d(logTag, "Removed users data from database")
            }.addOnFailureListener {
                Log.d(logTag, "Failed to remove users data from database")
                Log.d(logTag, it.message.toString())
            }
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(): UserRepository {
            var instance: UserRepository? = INSTANCE
            if (instance == null) {
                instance = UserRepository()
                instance.getDatabaseInstance()
                INSTANCE = instance
            }
            return instance
        }
    }
}
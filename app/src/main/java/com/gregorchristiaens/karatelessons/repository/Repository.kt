package com.gregorchristiaens.karatelessons.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

abstract class Repository {
    private val basePath =
        "https://karate-lessons-8c130-default-rtdb.europe-west1.firebasedatabase.app/"
    var database: DatabaseReference =
        Firebase.database(basePath).reference
    abstract val childPaths: ArrayList<String>

    fun getDatabaseInstance() {
        if (childPaths.isNotEmpty())
            for (childPath in childPaths) {
                database = database.child(childPath)
            }
    }
}
package com.gregorchristiaens.karatelessons.repository

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.gregorchristiaens.karatelessons.FireBaseOptions

abstract class Repository {
    private val basePath = FireBaseOptions().databaseBasePath
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
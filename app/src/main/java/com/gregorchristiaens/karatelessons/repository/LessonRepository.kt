package com.gregorchristiaens.karatelessons.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.gregorchristiaens.karatelessons.domain.Lesson
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException

class LessonRepository : Repository() {

    private val logTag = "KLT.LessonRepository"
    override val childPaths: ArrayList<String> get() = arrayListOf("lessons")

    private var _lessons = MutableLiveData<List<Lesson>>()
    val lessons: LiveData<List<Lesson>> get() = _lessons

    private var _lesson = MutableLiveData<Lesson>()
    val lesson: LiveData<Lesson> get() = _lesson

    fun getLessons() {
        database.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NullSafeMutableLiveData")
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(logTag, "Getting lessons from Database")
                Log.d(logTag, "Got Lessons: ${snapshot.value}")
                val list = ArrayList<Lesson>()
                for (value in snapshot.children) {
                    val lesson = value.getValue(Lesson::class.java)
                    if (lesson == null) {
                        throw IllegalArgumentException("Could not convert the database object to the local Lesson class")
                    } else {
                        list.add(lesson)
                    }
                }
                _lessons.value = list
                sortLessons()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(logTag, "Failed to read value.", error.toException())
            }
        })
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun sortLessons() {
        var list = lessons.value
        if (!list.isNullOrEmpty()) {
            list = list.sortedBy { it.date }
            _lessons.value = list
        }
    }

    private fun addLesson(lesson: Lesson) {
        try {
            database.child("lessons").child(lesson.id)
                .setValue(lesson)
                .addOnCompleteListener {
                    Log.d(logTag, "Saved Lesson: ${lesson.id}")
                }
                .addOnFailureListener {
                    Log.d(logTag, "Failed to save the lesson data in firebase database")
                }
        } catch (err: InvocationTargetException) {
            err.message?.let { Log.d(logTag, it) }
        }

    }

    companion object {
        @Volatile
        private var INSTANCE: LessonRepository? = null

        fun getInstance(): LessonRepository {
            var instance: LessonRepository? = INSTANCE
            if (instance == null) {
                instance = LessonRepository()
                instance.getDatabaseInstance()
                INSTANCE = instance
            }
            return instance
        }
    }
}
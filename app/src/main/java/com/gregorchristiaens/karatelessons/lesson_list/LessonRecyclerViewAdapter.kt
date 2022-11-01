package com.gregorchristiaens.karatelessons.lesson_list

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.gregorchristiaens.karatelessons.databinding.FragmentLessonBinding
import com.gregorchristiaens.karatelessons.domain.Lesson
import com.gregorchristiaens.karatelessons.repository.LessonRepository
import com.gregorchristiaens.karatelessons.repository.UserRepository

class LessonRecyclerViewAdapter : RecyclerView.Adapter<LessonRecyclerViewAdapter.ViewHolder>() {

    private val logTag = "KLT.LessonAdapter"
    lateinit var binding: FragmentLessonBinding
    private val lessonRepository = LessonRepository.getInstance()
    private val userRepository = UserRepository.getInstance()
    private val user = UserRepository.getInstance().user.value?.id

    var lessons: List<Lesson> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = FragmentLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lesson = lessons[position]
        holder.titleDate.text = lesson.date
        holder.titleType.text = lesson.type
        holder.titleLocation.text = lesson.location
        holder.cardGear.text = getGear(lesson.type)
        holder.participants.text = lesson.participants.count().toString()
        if (lesson.participants.contains(user)) {
            holder.participateButton.text = "Leave"
            holder.participateButton.setOnClickListener {
                if (!user.isNullOrEmpty()) {
                    Log.d(logTag, "Leave: ${lesson.id}")
                    lesson.participants.remove(user)
                    Log.d(logTag, lesson.participants.toString())
                    lessonRepository.addLesson(lesson)
                }
            }
        } else {
            holder.participateButton.text = "Participate"
            holder.participateButton.setOnClickListener {
                if (!user.isNullOrEmpty()) {
                    Log.d(logTag, "Participate: ${lesson.id}")
                    lesson.participants.add(user)
                    lessonRepository.addLesson(lesson)
                }
            }
        }
    }

    override fun getItemCount(): Int = lessons.size

    inner class ViewHolder(binding: FragmentLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleDate: TextView = binding.titleDate
        val titleType: TextView = binding.titleType
        val titleLocation: TextView = binding.titleLocation
        val cardGear: TextView = binding.lessonGear
        val participateButton: Button = binding.lessonParticipateButton
        val participants: TextView = binding.lessonParticipantsCount
        override fun toString(): String {
            return super.toString() + " '" + titleDate.text + titleType.text + titleLocation.text + "'"
        }
    }

    private fun getGear(type: String): String {
        return when (type) {
            "Standard" -> "Gi, Belt"
            "Kumite" -> "Gi, Belt, Boxing gloves, Shin guards"
            "Kobudo" -> "Gi, Belt, Bo / Sai / Tonfa / Nunchaku"
            else -> "Gi, Belt"
        }
    }

}
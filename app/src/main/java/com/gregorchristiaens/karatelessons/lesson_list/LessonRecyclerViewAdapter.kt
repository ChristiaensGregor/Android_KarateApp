package com.gregorchristiaens.karatelessons.lesson_list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.gregorchristiaens.karatelessons.databinding.FragmentLessonBinding
import com.gregorchristiaens.karatelessons.domain.Lesson

class LessonRecyclerViewAdapter : RecyclerView.Adapter<LessonRecyclerViewAdapter.ViewHolder>() {

    lateinit var binding: FragmentLessonBinding

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
    }

    override fun getItemCount(): Int = lessons.size

    inner class ViewHolder(binding: FragmentLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleDate: TextView = binding.titleDate
        val titleType: TextView = binding.titleType
        val titleLocation: TextView = binding.titleLocation
        val cardGear: TextView = binding.lessonGear
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
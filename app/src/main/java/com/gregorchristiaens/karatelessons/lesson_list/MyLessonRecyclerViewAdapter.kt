package com.gregorchristiaens.karatelessons.lesson_list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.gregorchristiaens.karatelessons.databinding.FragmentLessonBinding
import com.gregorchristiaens.karatelessons.domain.Lesson

class MyLessonRecyclerViewAdapter(
    private val values: List<Lesson>
) : RecyclerView.Adapter<MyLessonRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentLessonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.id
        holder.contentView.text = item.date
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentLessonBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}
package com.project.clonecoding.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.project.clonecoding.domain.model.CalendarEventCategory


@Entity(tableName = "CalendarEvent")
data class CalendarEventEntity(
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val note: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isRemind: Boolean,
    val category: CalendarEventCategory
){
    @PrimaryKey(autoGenerate = true) var id: Int? = null
}
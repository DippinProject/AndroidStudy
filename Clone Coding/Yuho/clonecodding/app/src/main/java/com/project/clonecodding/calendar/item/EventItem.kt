package com.project.clonecodding.calendar.item

import androidx.compose.ui.graphics.Color


data class EventItem(
    val title: String,
    val note: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isRemind: Boolean,
    val category: EventCategory
)

sealed class EventCategory(val color: Color) {
    data object Brainstorm : EventCategory(Color(0xff735BF2))
    data object Design : EventCategory(Color(0xff00b383))
    data object Workout : EventCategory(Color(0xff0095ff))
}
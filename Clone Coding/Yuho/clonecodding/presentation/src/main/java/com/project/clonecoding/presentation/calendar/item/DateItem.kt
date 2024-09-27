package com.project.clonecoding.presentation.calendar.item

data class DateItem(
    val day: Int,
    val isCurrDay: Boolean,
    val eventList: List<EventItem> = listOf()
)
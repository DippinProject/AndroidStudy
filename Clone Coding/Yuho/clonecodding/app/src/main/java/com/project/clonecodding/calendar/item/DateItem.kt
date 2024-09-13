package com.project.clonecodding.calendar.item

data class DateItem(
    val day: Int,
    val isCurrDay: Boolean,
    val eventList: List<EventItem> = listOf()
)
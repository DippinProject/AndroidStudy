package com.project.clonecoding.domain.model

data class CalendarEventsOfDateModel(
    val year: Int,
    val month: Int,
    val day: Int,
    val events: List<CalendarEventModel>,
    val isCurrDay: Boolean = true
)

data class CalendarEventModel(
    val title: String,
    val note: String,
    val date: String,
    val startTime: String,
    val endTime: String,
    val isRemind: Boolean,
    val category: CalendarEventCategory,
)

enum class CalendarEventCategory(val key: String, val colorHex: Long){
    Brainstorm(key = "brainstorm", colorHex =  0xff735BF2),
    Design(key = "design", colorHex =  0xff00b383),
    Workout(key = "workout", colorHex =  0xff0095ff)
}
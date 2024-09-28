package com.project.clonecoding.domain.model

import java.time.LocalDate

/**
 * 하나의 일자에 여러개의 이벤트가 담겨있는 캘린더 모델
 * 실질적으로 UI에 사용됨
 */
data class CalendarEventsOfDateModel(
    val year: Int,
    val month: Int,
    val day: Int,
    var events: List<CalendarEventModel>,
    val isCurrDay: Boolean = true
)

/**
 * 하나의 일자에 하나의 이벤트가 담겨있는 캘린더 모델
 * 이벤트를 추가하는 경우에 사용
 */
data class CalendarEventAddModel(
    val date: LocalDate,
    val event: CalendarEventModel
)

/**
 * 이벤트의 정보를 담고있는 모델
 */
data class CalendarEventModel(
    val title: String,
    val note: String,
    val startTime: String,
    val endTime: String,
    val isRemind: Boolean,
    val category: CalendarEventCategory?,
)

/**
 * 이벤트가 분류될 수 있는 카테고리
 */
enum class CalendarEventCategory(val key: String, val colorHex: Long){
    Brainstorm(key = "Brainstorm", colorHex =  0xff735BF2),
    Design(key = "Design", colorHex =  0xff00b383),
    Workout(key = "Workout", colorHex =  0xff0095ff)
}
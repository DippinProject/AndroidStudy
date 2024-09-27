package com.project.clonecoding.presentation.calendar

import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import java.time.LocalDate

data class CalendarState(
    val todayLocalDate: LocalDate = LocalDate.now(),

    val prevLocalDate: LocalDate = todayLocalDate.minusMonths(1),
    val nextLocalDate: LocalDate = todayLocalDate.plusMonths(1),

    val currYear: Int = todayLocalDate.year,
    val currMonth: Int = todayLocalDate.month.value,
    val currDay: Int = todayLocalDate.dayOfMonth,

    val calendarDayList: List<CalendarEventsOfDateModel> = listOf(),

    val isLoading: Boolean = false
)
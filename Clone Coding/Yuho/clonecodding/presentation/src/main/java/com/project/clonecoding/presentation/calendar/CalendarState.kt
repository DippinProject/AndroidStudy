package com.project.clonecoding.presentation.calendar

import com.project.clonecoding.domain.model.CalendarEventAddModel
import com.project.clonecoding.domain.model.CalendarEventModel
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import java.time.LocalDate

data class CalendarState(
    val todayLocalDate: LocalDate = LocalDate.now(),
    val prevLocalDate: LocalDate = todayLocalDate.minusMonths(1),
    val nextLocalDate: LocalDate = todayLocalDate.plusMonths(1),

    // 현재 선택된 날짜
    val selectedLocalDate: LocalDate = LocalDate.now(),

    // 현재 표기되고 있는 캘린더 날짜
    val currYear: Int = todayLocalDate.year,
    val currMonth: Int = todayLocalDate.month.value,

    val calendarDayList: List<CalendarEventsOfDateModel> = listOf(),

    // 추가하려는 이벤트 정보를 담는 모델
    val currWritingEvent: CalendarEventAddModel? = null,

    val isLoading: Boolean = false
)
package com.project.clonecoding.presentation.calendar

import com.project.clonecoding.presentation.calendar.item.DateItem
import java.time.LocalDate

data class CalendarState(
    val todayLocalDate: LocalDate = LocalDate.now(),

    val prevLocalDate: LocalDate = todayLocalDate.minusMonths(1),
    val nextLocalDate: LocalDate = todayLocalDate.plusMonths(1),

    val currYear: Int = todayLocalDate.year,
    val currMonth: Int = todayLocalDate.month.value,
    val currDay: Int = todayLocalDate.dayOfMonth,

    val calendarDayList: List<DateItem> = (1..todayLocalDate.month.maxLength()).map {
        DateItem(
            day = it,
            isCurrDay = true,
            eventList = listOf(),
        )
    }.toMutableList().apply {
        val startDayOfWeek = LocalDate.of(currYear, currMonth, 1).dayOfWeek.value
        val endDayOfWeek =
            LocalDate.of(currYear, currMonth, todayLocalDate.month.maxLength()).dayOfWeek.value
        val prevMonthDaysCount = startDayOfWeek - 1
        val nextMonthDaysCount = 7 - endDayOfWeek

        // 이전달 일자 표기용
        (0 until prevMonthDaysCount).forEach { minusCount ->
            add(
                0, DateItem(
                    day = prevLocalDate.month.maxLength() - minusCount,
                    isCurrDay = false,
                    eventList = listOf(),
                )
            )

        }

        // 다음달 일자 표기용
        (1..nextMonthDaysCount).forEach { day ->
            add(
                DateItem(
                    day = day,
                    isCurrDay = false,
                    eventList = listOf(),
                )
            )
        }
    }
)
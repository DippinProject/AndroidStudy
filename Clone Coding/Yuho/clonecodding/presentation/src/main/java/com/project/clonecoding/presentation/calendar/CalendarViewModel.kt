package com.project.clonecoding.presentation.calendar

import androidx.lifecycle.ViewModel
import com.project.clonecoding.presentation.calendar.item.DateItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

class CalendarViewModel : ViewModel() {

    private val _state: MutableStateFlow<CalendarState> = MutableStateFlow(CalendarState())
    val state get() = _state.asStateFlow()


    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnPrevMonth -> {
                val currLocalDate = LocalDate.of(
                    _state.value.currYear,
                    _state.value.currMonth,
                    _state.value.currDay
                ).minusMonths(1)

                val dayItems = (1..currLocalDate.lengthOfMonth()).map {
                    DateItem(
                        day = it,
                        isCurrDay = true,
                        eventList = listOf(),
                    )
                }.toMutableList().apply {
                    addExtraDays(list = this, currLocalDate = currLocalDate)
                }

                _state.value = _state.value.copy(
                    currYear = currLocalDate.year,
                    currMonth = currLocalDate.monthValue,
                    currDay = currLocalDate.dayOfMonth,
                    calendarDayList = dayItems
                )
            }

            is CalendarEvent.OnNextMonth -> {
                val currLocalDate = LocalDate.of(
                    _state.value.currYear,
                    _state.value.currMonth,
                    _state.value.currDay
                ).plusMonths(1)

                val dayItems = (1..currLocalDate.lengthOfMonth()).map {
                    DateItem(
                        day = it,
                        isCurrDay = true,
                        eventList = listOf(),
                    )
                }.toMutableList().apply {
                    addExtraDays(list = this, currLocalDate = currLocalDate)
                }

                _state.value = _state.value.copy(
                    currYear = currLocalDate.year,
                    currMonth = currLocalDate.monthValue,
                    currDay = currLocalDate.dayOfMonth,
                    calendarDayList = dayItems
                )
            }

            is CalendarEvent.OnDayChanged -> {
                _state.value = _state.value.copy(
                    currDay = event.day
                )
            }
        }
    }

    private fun getPrevMonthData(){

    }



    private fun addExtraDays(list: MutableList<DateItem>, currLocalDate: LocalDate) {
        val prevLocalDate = currLocalDate.minusMonths(1)
        val startDayOfWeek =
            LocalDate.of(currLocalDate.year, currLocalDate.month, 1).dayOfWeek.value
        val endDayOfWeek =
            LocalDate.of(
                currLocalDate.year,
                currLocalDate.month,
                currLocalDate.lengthOfMonth()
            ).dayOfWeek.value
        val prevMonthDaysCount = startDayOfWeek - 1
        val nextMonthDaysCount = 7 - endDayOfWeek

        // 이전달 일자 표기용
        (0 until prevMonthDaysCount).forEach { minusCount ->
            list.add(
                0, DateItem(
                    day = prevLocalDate.month.maxLength() - minusCount,
                    isCurrDay = false,
                    eventList = listOf(),
                )
            )

        }

        // 다음달 일자 표기용
        (1..nextMonthDaysCount).forEach { day ->
            list.add(
                DateItem(
                    day = day,
                    isCurrDay = false,
                    eventList = listOf(),
                )
            )
        }
    }

}
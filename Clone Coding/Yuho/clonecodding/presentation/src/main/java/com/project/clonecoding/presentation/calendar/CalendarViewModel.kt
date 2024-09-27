package com.project.clonecoding.presentation.calendar

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.clonecoding.common.DataState
import com.project.clonecoding.domain.usecase.AddCalendarEventUseCase
import com.project.clonecoding.domain.usecase.GetDateMatchedEventsUseCase
import com.project.clonecoding.domain.usecase.GetYearMonthMatchedEventsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val addCalendarEventUseCase: AddCalendarEventUseCase,
    private val getDateMatchedEventsUseCase: GetDateMatchedEventsUseCase,
    private val getYearMonthMatchedEventsUseCase: GetYearMonthMatchedEventsUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<CalendarState> = MutableStateFlow(CalendarState())
    val state get() = _state.asStateFlow()

    init {
        getMatchedMonthDays(_state.value.todayLocalDate)
    }

    fun onEvent(event: CalendarEvent) {
        when (event) {
            is CalendarEvent.OnPrevMonth -> {
                val currLocalDate = LocalDate.of(
                    _state.value.currYear,
                    _state.value.currMonth,
                    _state.value.currDay
                ).minusMonths(1)

                getMatchedMonthDays(date = currLocalDate)
            }

            is CalendarEvent.OnNextMonth -> {
                val currLocalDate = LocalDate.of(
                    _state.value.currYear,
                    _state.value.currMonth,
                    _state.value.currDay
                ).plusMonths(1)

                getMatchedMonthDays(date = currLocalDate)
            }

            is CalendarEvent.OnDayChanged -> {
                _state.value = _state.value.copy(
                    currDay = event.day
                )
            }
        }
    }


    /**
     * 현재 date에 맞는 Calendar 관련 정보들을 가져온다.
     * @param date
     *
     */
    private fun getMatchedMonthDays(date: LocalDate) {
        viewModelScope.launch {
            getYearMonthMatchedEventsUseCase(date).collect{ result ->
                when (result) {
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }

                    is DataState.Success -> {
                        result.data?.let { items ->
                            _state.value = _state.value.copy(
                                currYear = date.year,
                                currMonth = date.monthValue,
                                currDay = date.dayOfMonth,
                                calendarDayList = items
                            )
                        }
                    }

                    is DataState.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }

            }
        }
    }

}
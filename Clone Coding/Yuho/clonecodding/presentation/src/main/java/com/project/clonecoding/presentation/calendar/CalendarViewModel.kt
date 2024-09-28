package com.project.clonecoding.presentation.calendar

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.clonecoding.common.DataState
import com.project.clonecoding.common.DateUtils.ymdBaseDateTimeFormatter
import com.project.clonecoding.domain.model.CalendarEventAddModel
import com.project.clonecoding.domain.model.CalendarEventModel
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
                    1
                ).minusMonths(1)

                getMatchedMonthDays(date = currLocalDate)
            }

            is CalendarEvent.OnNextMonth -> {
                val currLocalDate = LocalDate.of(
                    _state.value.currYear,
                    _state.value.currMonth,
                    1
                ).plusMonths(1)

                getMatchedMonthDays(date = currLocalDate)
            }

            is CalendarEvent.OnDayChanged -> {
                _state.value = _state.value.copy(
                    selectedLocalDate = LocalDate.of(
                        _state.value.currYear,
                        _state.value.currMonth,
                        event.day
                    )
                )
            }

            is CalendarEvent.OnAddSheetOpen -> {
                _state.value = _state.value.copy(
                    currWritingEvent = CalendarEventAddModel(
                        date = LocalDate.of(
                            _state.value.selectedLocalDate.year,
                            _state.value.selectedLocalDate.monthValue,
                            _state.value.selectedLocalDate.dayOfMonth
                        ),
                        event = CalendarEventModel(
                            title = "",
                            note = "",
                            startTime = "",
                            endTime = "",
                            isRemind = false,
                            category = null,
                        )
                    )
                )
            }

            is CalendarEvent.OnAddSheetClose -> {
                _state.value = _state.value.copy(
                    currWritingEvent = null
                )
            }

            is CalendarEvent.RequestEventAdd -> {
                addCalendarEvent()
            }

            is CalendarEvent.OnTitleChanged -> {
                val currentEvent = _state.value.currWritingEvent?.event
                val updatedEvent = currentEvent?.copy(title = event.newTitle)
                val updatedWritingEvent =
                    updatedEvent?.let { _state.value.currWritingEvent?.copy(event = it) }

                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
            }

            is CalendarEvent.OnNoteChanged -> {
                val currentEvent = _state.value.currWritingEvent?.event
                val updatedEvent = currentEvent?.copy(note = event.newNote)
                val updatedWritingEvent =
                    updatedEvent?.let { _state.value.currWritingEvent?.copy(event = it) }

                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
            }

            is CalendarEvent.OnDateChanged -> {
                val date = LocalDate.parse(event.newDateStr, ymdBaseDateTimeFormatter)
                val updatedWritingEvent = _state.value.currWritingEvent?.copy(date = date)
                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
            }

            is CalendarEvent.OnStartTimeChanged -> {
                val currentEvent = _state.value.currWritingEvent?.event
                val updatedEvent = currentEvent?.copy(startTime = event.newStartTime)
                val updatedWritingEvent =
                    updatedEvent?.let { _state.value.currWritingEvent?.copy(event = it) }

                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
            }

            is CalendarEvent.OnEndTimeChanged -> {
                val currentEvent = _state.value.currWritingEvent?.event
                val updatedEvent = currentEvent?.copy(endTime = event.newEndTime)
                val updatedWritingEvent =
                    updatedEvent?.let { _state.value.currWritingEvent?.copy(event = it) }

                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
            }

            is CalendarEvent.OnIsRemindMeChanged -> {
                val currentEvent = _state.value.currWritingEvent?.event
                val updatedEvent = currentEvent?.copy(isRemind = !currentEvent.isRemind)
                val updatedWritingEvent =
                    updatedEvent?.let { _state.value.currWritingEvent?.copy(event = it) }

                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
            }

            is CalendarEvent.OnCategoryChanged -> {
                val currentEvent = _state.value.currWritingEvent?.event
                val updatedEvent = currentEvent?.copy(category = event.newCategory)
                val updatedWritingEvent =
                    updatedEvent?.let { _state.value.currWritingEvent?.copy(event = it) }

                _state.value = _state.value.copy(currWritingEvent = updatedWritingEvent)
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
            getYearMonthMatchedEventsUseCase(date).collect { result ->
                when (result) {
                    is DataState.Loading -> {
                        _state.value = _state.value.copy(isLoading = result.isLoading)
                    }

                    is DataState.Success -> {
                        result.data?.let { items ->
                            _state.value = _state.value.copy(
                                currYear = date.year,
                                currMonth = date.monthValue,
                                calendarDayList = items,
                                isLoading = false
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


    /**
     * 캘린더 이벤트 일정을 추가하는 함수
     */
    private fun addCalendarEvent() {
        viewModelScope.launch {
            with(_state.value) {
                if (currWritingEvent?.event != null) {
                    addCalendarEventUseCase(currWritingEvent).collect { result ->
                        when (result) {
                            is DataState.Loading -> {
                                _state.value = _state.value.copy(isLoading = result.isLoading)
                            }

                            is DataState.Success -> {
                                if (result.data != null) {
                                    val targetIdx =
                                        _state.value.calendarDayList.indexOfFirst {
                                            it.year == result.data!!.year && it.month == result.data!!.month && it.day == result.data!!.day
                                        }
                                    if(targetIdx > -1){
                                        val newCalendarDayList = _state.value.calendarDayList.apply{
                                            this[targetIdx].events = result.data!!.events
                                        }
                                        _state.value = _state.value.copy(calendarDayList = newCalendarDayList)
                                    }
                                }
                                onEvent(CalendarEvent.OnAddSheetClose)
                            }

                            is DataState.Error -> {
                                _state.value = _state.value.copy(isLoading = false)
                            }
                        }
                    }
                }
            }
        }
    }

}
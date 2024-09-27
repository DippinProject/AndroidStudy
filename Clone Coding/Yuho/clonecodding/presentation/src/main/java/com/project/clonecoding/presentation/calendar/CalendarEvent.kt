package com.project.clonecoding.presentation.calendar

sealed class CalendarEvent {
    data object OnPrevMonth : CalendarEvent()
    data object OnNextMonth : CalendarEvent()
    data class OnDayChanged(val day: Int) : CalendarEvent()
}
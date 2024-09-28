package com.project.clonecoding.presentation.calendar

import com.project.clonecoding.domain.model.CalendarEventCategory

sealed class CalendarEvent {
    data object OnPrevMonth : CalendarEvent()
    data object OnNextMonth : CalendarEvent()
    data class OnDayChanged(val day: Int) : CalendarEvent()
    data object OnAddSheetOpen : CalendarEvent()
    data object OnAddSheetClose : CalendarEvent()
    data object RequestEventAdd : CalendarEvent()
    data class OnTitleChanged(val newTitle: String) : CalendarEvent()
    data class OnNoteChanged(val newNote: String) : CalendarEvent()
    data class OnDateChanged(val newDateStr: String) : CalendarEvent()
    data class OnStartTimeChanged(val newStartTime: String) : CalendarEvent()
    data class OnEndTimeChanged(val newEndTime: String) : CalendarEvent()
    data object OnIsRemindMeChanged : CalendarEvent()
    data class OnCategoryChanged(val newCategory: CalendarEventCategory) : CalendarEvent()
}
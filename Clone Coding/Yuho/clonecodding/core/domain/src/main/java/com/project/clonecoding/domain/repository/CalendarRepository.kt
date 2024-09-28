package com.project.clonecoding.domain.repository

import com.project.clonecoding.common.DataState
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import com.project.clonecoding.domain.model.CalendarEventModel
import kotlinx.coroutines.flow.Flow

interface CalendarRepository {
    suspend fun addCalendarEvent(
        year: Int,
        month: Int,
        day: Int,
        model: CalendarEventModel
    ): Flow<DataState<Boolean>>

    suspend fun getYearMonthMatchedEvents(
        year: Int,
        month: Int,
    ): Flow<DataState<List<CalendarEventsOfDateModel>>>

    suspend fun getDateMatchedEvents(
        year: Int,
        month: Int,
        day: Int
    ): Flow<DataState<CalendarEventsOfDateModel>>
}
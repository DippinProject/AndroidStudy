package com.project.clonecoding.domain.usecase

import com.project.clonecoding.domain.model.CalendarEventModel
import com.project.clonecoding.domain.repository.CalendarRepository
import javax.inject.Inject

class AddCalendarEventUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
        day: Int,
        model: CalendarEventModel
    ) = calendarRepository.addCalendarEvent(
        year = year,
        month = month,
        day = day,
        model = model
    )
}
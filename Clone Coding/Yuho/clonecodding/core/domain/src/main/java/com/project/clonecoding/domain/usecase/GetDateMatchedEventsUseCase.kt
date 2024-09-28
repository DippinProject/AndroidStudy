package com.project.clonecoding.domain.usecase

import com.project.clonecoding.domain.repository.CalendarRepository
import javax.inject.Inject

class GetDateMatchedEventsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(
        year: Int,
        month: Int,
        day: Int
    ) = calendarRepository.getDateMatchedEvents(
        year = year,
        month = month,
        day = day,
    )
}
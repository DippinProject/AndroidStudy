package com.project.clonecoding.domain.usecase

import com.project.clonecoding.common.DataState
import com.project.clonecoding.domain.model.CalendarEventAddModel
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import com.project.clonecoding.domain.repository.CalendarRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AddCalendarEventUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val getDateMatchedEventsUseCase: GetDateMatchedEventsUseCase
) {
    @OptIn(FlowPreview::class)
    suspend operator fun invoke(
        model: CalendarEventAddModel
    ): Flow<DataState<CalendarEventsOfDateModel>> {
        return calendarRepository.addCalendarEvent(
            year = model.date.year,
            month = model.date.monthValue,
            day = model.date.dayOfMonth,
            model = model.event
        ).flatMapConcat { dataState ->
            when (dataState) {
                // 추가에 성공한 경우에는, 해당 데이터를 가져옴
                is DataState.Success -> {
                    getDateMatchedEventsUseCase.invoke(
                        year = model.date.year,
                        month = model.date.monthValue,
                        day = model.date.dayOfMonth
                    ).map { result ->
                        when (result) {
                            is DataState.Loading -> DataState.Loading(isLoading = result.isLoading)
                            is DataState.Success -> DataState.Success(result.data)
                            is DataState.Error -> DataState.Error(result.message ?: "Unknown Error")
                        }
                    }
                }

                is DataState.Error -> flowOf(DataState.Error(message = dataState.message ?: ""))
                is DataState.Loading -> flowOf(DataState.Loading(isLoading = dataState.isLoading))
            }
        }
    }
}

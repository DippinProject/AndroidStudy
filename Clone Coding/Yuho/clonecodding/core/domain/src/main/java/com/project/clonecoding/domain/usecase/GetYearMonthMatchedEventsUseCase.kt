package com.project.clonecoding.domain.usecase

import com.project.clonecoding.common.DataState
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import com.project.clonecoding.domain.repository.CalendarRepository
import com.project.clonecoding.domain.utils.CalendarUtils.addExtraDays
import com.project.clonecoding.domain.utils.CalendarUtils.getCalendarDaysSettingList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetYearMonthMatchedEventsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(
        date: LocalDate
    ): Flow<DataState<List<CalendarEventsOfDateModel>>> {
        return calendarRepository.getYearMonthMatchedEvents(
            year = date.year,
            month = date.monthValue
        ).map { dataState ->
            when (dataState) {
                is DataState.Success -> {
                    // 우선, 표시해줄 모든 일자를 먼저 만들어놓는다.
                    val modifiedData = getCalendarDaysSettingList(date).toMutableList().apply {
                        // 캘린더의 빈 자리를 같이 채워서 반환하도록 함.
                        addExtraDays(date)
                    }

                    // 이벤트가 있는 일자에 한해서, modifiedData에 업데이트해준다.
                    val dataStateMap =
                        dataState.data?.associateBy { Triple(it.year, it.month, it.day) }

                    modifiedData.map { modifiedModel ->
                        dataStateMap?.get(
                            Triple(
                                modifiedModel.year,
                                modifiedModel.month,
                                modifiedModel.day
                            )
                        )
                            ?.let { dataStateModel ->
                                modifiedModel.copy(events = dataStateModel.events)
                            }
                    }

                    DataState.Success(modifiedData)
                }

                else -> dataState
            }
        }
    }
}
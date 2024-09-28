package com.project.clonecoding.data.repository

import android.util.Log
import com.project.clonecoding.common.DataState
import com.project.clonecoding.data.local.db.CalendarDatabase
import com.project.clonecoding.data.local.entity.CalendarEventEntity
import com.project.clonecoding.data.mapper.toCalendarEventOfDateModel
import com.project.clonecoding.data.mapper.toCalendarEventOfDateModelList
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import com.project.clonecoding.domain.model.CalendarEventModel
import com.project.clonecoding.domain.repository.CalendarRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarDatabase: CalendarDatabase
) : CalendarRepository {
    override suspend fun addCalendarEvent(
        year: Int,
        month: Int,
        day: Int,
        model: CalendarEventModel
    ): Flow<DataState<Boolean>> {
        return flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val res = calendarDatabase.getCalendarDao().insert(
                    entity = CalendarEventEntity(
                        year = year,
                        month = month,
                        day = day,
                        title = model.title,
                        note = model.note,
                        startTime = model.startTime,
                        endTime = model.endTime,
                        isRemind = model.isRemind,
                        category = model.category,
                    )
                )

                if (res == -1L) {
                    emit(DataState.Loading(isLoading = false))
                } else {
                    emit(DataState.Success(data = true))
                    emit(DataState.Loading(isLoading = false))
                }
            } catch (e: Exception) {
                emit(DataState.Error(message = e.message ?: ""))
                emit(DataState.Loading(isLoading = false))
            }
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun getYearMonthMatchedEvents(
        year: Int,
        month: Int
    ): Flow<DataState<List<CalendarEventsOfDateModel>>> {
        return flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val resList = calendarDatabase.getCalendarDao().getYearMonthMatchedEntities(
                    year = year,
                    month = month
                )

                emit(DataState.Success(data = resList.toCalendarEventOfDateModelList()))
                emit(DataState.Loading(isLoading = false))
            } catch (e: Exception) {
                emit(DataState.Error(message = e.message ?: ""))
                emit(DataState.Loading(isLoading = false))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getDateMatchedEvents(
        year: Int,
        month: Int,
        day: Int
    ): Flow<DataState<CalendarEventsOfDateModel>> {
        return flow {
            emit(DataState.Loading(isLoading = true))
            try {
                val resList = calendarDatabase.getCalendarDao().getDateMatchedEntities(
                    year = year,
                    month = month,
                    day = day
                )

                emit(DataState.Success(data = resList.toCalendarEventOfDateModel()))
                emit(DataState.Loading(isLoading = false))
            } catch (e: Exception) {
                emit(DataState.Error(message = e.message ?: ""))
                emit(DataState.Loading(isLoading = false))
            }
        }.flowOn(Dispatchers.IO)
    }

}
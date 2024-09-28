package com.project.clonecoding.domain.di

import com.project.clonecoding.domain.repository.CalendarRepository
import com.project.clonecoding.domain.usecase.AddCalendarEventUseCase
import com.project.clonecoding.domain.usecase.GetDateMatchedEventsUseCase
import com.project.clonecoding.domain.usecase.GetYearMonthMatchedEventsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {
    @Provides
    @Singleton
    fun provideAddCalendarEventUseCase(
        calendarRepository: CalendarRepository,
        getDateMatchedEventsUseCase: GetDateMatchedEventsUseCase
    ) = AddCalendarEventUseCase(calendarRepository, getDateMatchedEventsUseCase)

    @Provides
    @Singleton
    fun provideGetDateMatchedEventsUseCase(calendarRepository: CalendarRepository) =
        GetDateMatchedEventsUseCase(calendarRepository)

    @Provides
    @Singleton
    fun provideGetYearMonthMatchedEventsUseCase(calendarRepository: CalendarRepository) =
        GetYearMonthMatchedEventsUseCase(calendarRepository)
}
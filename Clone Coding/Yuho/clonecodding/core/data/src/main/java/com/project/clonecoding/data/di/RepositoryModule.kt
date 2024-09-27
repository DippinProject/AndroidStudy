package com.project.clonecoding.data.di

import com.project.clonecoding.data.local.db.CalendarDatabase
import com.project.clonecoding.data.repository.CalendarRepositoryImpl
import com.project.clonecoding.domain.repository.CalendarRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideCalendarRepository(calendarDatabase: CalendarDatabase): CalendarRepository {
        return CalendarRepositoryImpl(calendarDatabase = calendarDatabase)
    }
}
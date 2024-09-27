package com.project.clonecoding.data.di

import android.app.Application
import androidx.room.Room
import com.project.clonecoding.data.local.dao.CalendarDao
import com.project.clonecoding.data.local.db.CalendarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    internal fun provideCalendarDatabase(application: Application): CalendarDatabase{
        return Room.databaseBuilder(
            application,
            CalendarDatabase::class.java,
            CalendarDatabase.CALENDAR_DATABASE
        ).build()
    }

    @Provides
    internal fun provideCalendarDao(calendarDatabase: CalendarDatabase): CalendarDao{
        return calendarDatabase.getCalendarDao()
    }
}
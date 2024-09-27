package com.project.clonecoding.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.project.clonecoding.data.local.dao.CalendarDao
import com.project.clonecoding.data.local.entity.CalendarEventEntity


@Database(entities = [CalendarEventEntity::class], version = 1)
abstract class CalendarDatabase : RoomDatabase() {
    abstract fun getCalendarDao(): CalendarDao

    companion object{
        const val CALENDAR_DATABASE = "calendar-db"
    }
}


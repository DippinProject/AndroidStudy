package com.project.clonecoding.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.clonecoding.data.local.entity.CalendarEventEntity

@Dao
interface CalendarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: CalendarEventEntity): Long

    @Query("SELECT * FROM CalendarEvent WHERE year = :year AND month = :month AND day = :day")
    fun getDateMatchedEntities(year: Int, month: Int, day: Int): List<CalendarEventEntity>

    @Query("SELECT * FROM CalendarEvent WHERE year = :year AND month = :month ORDER BY day ASC")
    fun getYearMonthMatchedEntities(year: Int, month: Int): List<CalendarEventEntity>
}
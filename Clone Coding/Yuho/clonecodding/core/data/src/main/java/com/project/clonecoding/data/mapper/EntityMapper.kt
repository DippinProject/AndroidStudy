package com.project.clonecoding.data.mapper

import com.project.clonecoding.data.local.entity.CalendarEventEntity
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import com.project.clonecoding.domain.model.CalendarEventModel

fun CalendarEventEntity.toCalendarEventModel() = CalendarEventModel(
    title = title,
    note = note,
    date = date,
    startTime = startTime,
    endTime = endTime,
    isRemind = isRemind,
    category = category,
)

fun List<CalendarEventEntity>.toCalendarEventOfDateModel(): CalendarEventsOfDateModel? {
    return if (this.isEmpty()) {
        null
    } else {
        CalendarEventsOfDateModel(
            year = this[0].year,
            month = this[0].month,
            day = this[0].day,
            events = this.map { it.toCalendarEventModel() }
        )
    }
}

fun List<CalendarEventEntity>.toCalendarEventOfDateModelList(): List<CalendarEventsOfDateModel> {
    return if (this.isEmpty()) {
        listOf()
    } else {
        this.groupBy { Triple(it.year, it.month, it.day) }.map { form ->
            val (year, month, day) = form.key
            val entries = form.value
            CalendarEventsOfDateModel(
                year = year,
                month = month,
                day = day,
                events = entries.map {
                    it.toCalendarEventModel()
                }
            )
        }
    }
}
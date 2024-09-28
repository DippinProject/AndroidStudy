package com.project.clonecoding.domain.utils

import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import java.time.LocalDate

object CalendarUtils {
    /**
     * 년,월에 해당하는 전체 일자가 셋팅된 CalendarEventOfDateModel을 가져옴
     *
     */
    fun getCalendarDaysSettingList(date: LocalDate) =
        (1..date.lengthOfMonth()).map {
            CalendarEventsOfDateModel(
                date.year,
                date.monthValue,
                day = it,
                isCurrDay = true,
                events = listOf(),
            )
        }



    /**
     * 캘린더 자리를 채우기 위해, 이전달과 다음달 일자를 넣어주는 함수
     * @param currLocalDate
     *
     */
    fun MutableList<CalendarEventsOfDateModel>?.addExtraDays(currLocalDate: LocalDate) {
        if(this == null) return

        val prevLocalDate = currLocalDate.minusMonths(1)
        val nextLocalDate = currLocalDate.plusMonths(1)
        val startDayOfWeek =
            LocalDate.of(currLocalDate.year, currLocalDate.month, 1).dayOfWeek.value
        val endDayOfWeek =
            LocalDate.of(
                currLocalDate.year,
                currLocalDate.month,
                currLocalDate.lengthOfMonth()
            ).dayOfWeek.value

        // 이전달 자리 채워야 하는 개수 계산
        val prevMonthDaysCount = startDayOfWeek - 1
        // 다음달 자리 채워야 하는 개수 계산
        val nextMonthDaysCount = 7 - endDayOfWeek

        // 이전달 일자 표기용
        (0 until prevMonthDaysCount).forEach { minusCount ->
            this.add(
                0, CalendarEventsOfDateModel(
                    year = prevLocalDate.year,
                    month = prevLocalDate.monthValue,
                    day = prevLocalDate.month.maxLength() - minusCount,
                    isCurrDay = false,
                    events = listOf(),
                )
            )

        }

        // 다음달 일자 표기용
        (1..nextMonthDaysCount).forEach { day ->
            this.add(
                CalendarEventsOfDateModel(
                    year = nextLocalDate.year,
                    month = nextLocalDate.monthValue,
                    day = day,
                    isCurrDay = false,
                    events = listOf(),
                )
            )
        }
    }
}
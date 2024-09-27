package com.project.clonecoding.presentation.calendar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.clonecoding.presentation.calendar.item.DateItem
import com.project.clonecoding.presentation.calendar.item.EventCategory
import com.project.clonecoding.presentation.calendar.item.EventItem
import com.project.clonecoding.presentation.R
import com.project.clonecoding.presentation.theme.ClonecoddingTheme
import com.project.clonecoding.presentation.theme.White
import java.time.Month
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel = viewModel()) {
    ClonecoddingTheme {
        Scaffold(
            bottomBar = {
                CustomCurvedShape()
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFDFDFD))
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CalendarBody(
                    viewModel = calendarViewModel,
                    modifier = Modifier
                        .widthIn(max = 450.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun CalendarScreenPreview() {
    ClonecoddingTheme {
        Scaffold(
            bottomBar = {
                CustomCurvedShape()
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFDFDFD))
                    .padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CalendarBody(
                    modifier = Modifier
                        .widthIn(max = 450.dp)
                        .fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CalendarBody(
    viewModel: CalendarViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val state = viewModel.state.collectAsStateWithLifecycle()

        CalendarDateController(
            currYear = state.value.currYear,
            currMonth = state.value.currMonth,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp),
            onPrevMonth = {
                viewModel.onEvent(CalendarEvent.OnPrevMonth)
            },
            onNextMonth = {
                viewModel.onEvent(CalendarEvent.OnNextMonth)
            }
        )

        var selectedItemDay by rememberSaveable {
            mutableIntStateOf(-1)
        }

        var selectedItem by remember(selectedItemDay) {
            mutableStateOf(
                state.value.calendarDayList.firstOrNull { it.isCurrDay && it.day == selectedItemDay }
            )
        }

        CalendarContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            dayItems = state.value.calendarDayList,
            selectedItem = selectedItem,
            onSelectedDayChanged = { day ->
                selectedItemDay = day
            }
        )

        CalendarScheduleList(
            modifier = Modifier.weight(1f),
            eventList = selectedItem?.eventList ?: listOf()
        )
    }
}

@Composable
private fun CalendarDateController(
    currYear: Int,
    currMonth: Int,
    modifier: Modifier = Modifier,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(width = 1.dp, color = Color(0x77ced3de), shape = RoundedCornerShape(10.dp))
                .clickable {
                    onPrevMonth()
                },
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                tint = Color(0xff222b45),
                contentDescription = "controller_arrow_left"
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Month.of(currMonth)
                    .getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH),
                color = Color(0xff222b45),
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = currYear.toString(),
                color = Color(0xff8f9bb3),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            Spacer(modifier = Modifier.height(10.dp))
        }



        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .border(width = 1.dp, color = Color(0x77ced3de), shape = RoundedCornerShape(10.dp))
                .clickable {
                    onNextMonth()
                },
        ) {
            Icon(
                modifier = Modifier.align(Alignment.Center),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = Color(0xff222b45),
                contentDescription = "controller_arrow_right"
            )
        }
    }
}

@Composable
private fun CalendarContent(
    dayItems: List<DateItem>,
    selectedItem: DateItem?,
    modifier: Modifier = Modifier,
    onSelectedDayChanged: (Int) -> Unit
) {
    Column(modifier = modifier) {

        val items = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(items.size) { idx ->
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = items[idx],
                    color = Color(0xff8f9bb3),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(items = dayItems) { item ->

                val isSelected = item.isCurrDay && item.day == selectedItem?.day

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(6f / 5f)
                        .then(
                            if (item.isCurrDay) {
                                Modifier.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    onSelectedDayChanged(item.day)
                                }
                            } else {
                                Modifier
                            }
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) Color(0xff735bf2) else Color.Transparent),
                        ) {
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                text = item.day.toString(),
                                color = if (item.isCurrDay) {
                                    if (isSelected) White else Color(0xff222b45)
                                } else {
                                    Color(0xff8f9bb3)
                                },
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .height(5.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        val viewCategoryList = mutableListOf<EventCategory>()
                        item.eventList.forEach {
                            when (it.category) {
                                is EventCategory.Design -> viewCategoryList.add(EventCategory.Design)
                                is EventCategory.Workout -> viewCategoryList.add(EventCategory.Workout)
                                is EventCategory.Brainstorm -> viewCategoryList.add(EventCategory.Brainstorm)
                            }
                        }
                        viewCategoryList.forEach {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 1.dp)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(it.color)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(1.5.dp)
                                        .clip(CircleShape)
                                        .background(White)
                                        .align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
private fun CalendarScheduleList(modifier: Modifier = Modifier, eventList: List<EventItem>) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        Log.d("TAG", "CalendarScheduleList: ${dragAmount}")
                    }
                },
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(30.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xffced3de))
            )
        }

        LazyColumn(
            modifier = modifier.padding(start = 17.dp, end = 17.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(items = eventList) {
                CalendarScheduleItem(item = it, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}


@Composable
private fun CalendarScheduleItem(item: EventItem, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(vertical = 16.dp, horizontal = 14.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(item.category.color)
                ) {
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(White)
                            .align(Alignment.Center)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "${item.startTime} ~ ${item.endTime}",
                    fontSize = 12.sp,
                    color = Color(0xff8f9bb3),
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_menu_hor),
                contentDescription = "menu_horizontal"
            )
        }

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = item.title,
            color = Color(0xff222b45),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = item.note,
            color = Color(0xff8f9bb3),
            fontSize = 12.sp,
            fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )

    }
}

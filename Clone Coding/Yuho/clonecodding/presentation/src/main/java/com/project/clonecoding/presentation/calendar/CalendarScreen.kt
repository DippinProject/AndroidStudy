package com.project.clonecoding.presentation.calendar

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.project.clonecoding.domain.model.CalendarEventCategory
import com.project.clonecoding.domain.model.CalendarEventModel
import com.project.clonecoding.domain.model.CalendarEventsOfDateModel
import com.project.clonecoding.presentation.R
import com.project.clonecoding.presentation.theme.ClonecoddingTheme
import com.project.clonecoding.presentation.theme.White
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.Month
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CalendarScreen(calendarViewModel: CalendarViewModel = hiltViewModel()) {
    val state = calendarViewModel.state.collectAsStateWithLifecycle()
    ClonecoddingTheme {
        Scaffold(
            bottomBar = {
                CustomCurvedShape(onAddEvent = {
                    calendarViewModel.onEvent(CalendarEvent.OnAddSheetOpen)
                })
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

                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
                if (state.value.currWritingEvent != null) {
                    AddEventBottomSheet(
                        sheetState = sheetState,
                        title = state.value.currWritingEvent?.event?.title ?: "",
                        note = state.value.currWritingEvent?.event?.note ?: "",
                        date = state.value.currWritingEvent?.date ?: LocalDate.now(),
                        startTime = state.value.currWritingEvent?.event?.startTime ?: "",
                        endTime = state.value.currWritingEvent?.event?.endTime ?: "",
                        isRemindMe = state.value.currWritingEvent?.event?.isRemind ?: false,
                        category = state.value.currWritingEvent?.event?.category,
                        modifier = Modifier.fillMaxWidth(),
                        callback = object : AddEventBottomSheetCallback {
                            override fun onDismiss() {
                                calendarViewModel.onEvent(CalendarEvent.OnAddSheetClose)
                            }

                            override fun onTitleChanged(newTitle: String) {
                                calendarViewModel.onEvent(CalendarEvent.OnTitleChanged(newTitle))
                            }

                            override fun onNoteChanged(newNote: String) {
                                calendarViewModel.onEvent(CalendarEvent.OnNoteChanged(newNote))
                            }

                            override fun onDateChanged(newDateStr: String) {
                                calendarViewModel.onEvent(CalendarEvent.OnDateChanged(newDateStr))
                            }

                            override fun onStartTimeChanged(newStartTime: String) {
                                calendarViewModel.onEvent(
                                    CalendarEvent.OnStartTimeChanged(
                                        newStartTime
                                    )
                                )
                            }

                            override fun onEndTimeChanged(newEndTime: String) {
                                calendarViewModel.onEvent(CalendarEvent.OnEndTimeChanged(newEndTime))
                            }

                            override fun onIsRemindMeChanged() {
                                calendarViewModel.onEvent(CalendarEvent.OnIsRemindMeChanged)
                            }

                            override fun onCategoryChanged(newCategory: CalendarEventCategory) {
                                calendarViewModel.onEvent(
                                    CalendarEvent.OnCategoryChanged(
                                        newCategory
                                    )
                                )
                            }

                            override fun requestEventAdd() {
                                calendarViewModel.onEvent(CalendarEvent.RequestEventAdd)
                            }
                        }
                    )
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview
fun CalendarScreenPreview() {
    ClonecoddingTheme {
        var isAddEventActivate by rememberSaveable {
            mutableStateOf(false)
        }
        Scaffold(
            bottomBar = {
                CustomCurvedShape(onAddEvent = {
                    isAddEventActivate = true
                })
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
    viewModel: CalendarViewModel = hiltViewModel(),
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

        var selectedItem by remember(state.value.selectedLocalDate) {
            mutableStateOf(
                state.value.calendarDayList.firstOrNull {
                    it.isCurrDay && it.year == state.value.selectedLocalDate.year && it.month == state.value.selectedLocalDate.monthValue && it.day == state.value.selectedLocalDate.dayOfMonth
                }
            )
        }

        // 슬라이드 이벤트의 쿨타임을 관리하는 상태
        var isGestureEnabled by rememberSaveable { mutableStateOf(true) }
        LaunchedEffect(isGestureEnabled) {
            if (!isGestureEnabled) {
                delay(300) // 0.3초 지연
                isGestureEnabled = true
            }
        }

        CalendarContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        if (isGestureEnabled) {
                            if (dragAmount > 100) {
                                viewModel.onEvent(CalendarEvent.OnPrevMonth)
                                isGestureEnabled = false
                            } else if (dragAmount < -100) {
                                viewModel.onEvent(CalendarEvent.OnNextMonth)
                                isGestureEnabled = false
                            }
                        }
                    }
                },
            dayItems = state.value.calendarDayList,
            selectedItem = selectedItem,
            onSelectedDayChanged = { day ->
                viewModel.onEvent(CalendarEvent.OnDayChanged(day))
            }
        )

        CalendarScheduleList(
            modifier = Modifier.weight(1f),
            eventList = selectedItem?.events ?: listOf()
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
    dayItems: List<CalendarEventsOfDateModel>,
    selectedItem: CalendarEventsOfDateModel?,
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

                val isSelected =
                    item.isCurrDay && item.year == selectedItem?.year && item.month == selectedItem.month && item.day == selectedItem.day

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
                        val viewCategoryList = mutableListOf<CalendarEventCategory>()
                        item.events.forEach {
                            when (it.category) {
                                CalendarEventCategory.Design -> viewCategoryList.add(
                                    CalendarEventCategory.Design
                                )

                                CalendarEventCategory.Workout -> viewCategoryList.add(
                                    CalendarEventCategory.Workout
                                )

                                CalendarEventCategory.Brainstorm -> viewCategoryList.add(
                                    CalendarEventCategory.Brainstorm
                                )

                                else -> {}
                            }
                        }
                        viewCategoryList.forEach {
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 1.dp)
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(Color(it.colorHex))
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
private fun CalendarScheduleList(
    modifier: Modifier = Modifier,
    eventList: List<CalendarEventModel>
) {
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
private fun CalendarScheduleItem(item: CalendarEventModel, modifier: Modifier = Modifier) {
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
                        .background(Color(item.category?.colorHex ?: 0x00ff0000))
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEventBottomSheet(
    sheetState: SheetState,
    title: String,
    note: String,
    date: LocalDate,
    startTime: String,
    endTime: String,
    isRemindMe: Boolean,
    category: CalendarEventCategory?,
    modifier: Modifier = Modifier,
    callback: AddEventBottomSheetCallback,
) {

    ModalBottomSheet(
        modifier = modifier
            .navigationBarsPadding(),
        onDismissRequest = { callback.onDismiss() },
        sheetState = sheetState,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(White)
                .padding(
                    horizontal = 15.dp
                ),
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp),
                text = "Add New Event",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color(0xff222b45),
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 21.dp),
                value = title,
                onValueChange = {
                    callback.onTitleChanged(it)
                },
                label = {
                    Text(
                        "Event name",
                        color = Color(0xff8f9bb3),
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                },
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xffedf1f7),
                    focusedBorderColor = Color(0xffedf1f7)
                )
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .height(90.dp),
                value = note,
                onValueChange = {
                    callback.onNoteChanged(it)
                },
                label = {
                    Text(
                        "Type the note here...",
                        color = Color(0xff8f9bb3),
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xffedf1f7),
                    focusedBorderColor = Color(0xffedf1f7)
                )
            )

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = date.toString(),
                onValueChange = {
                    callback.onDateChanged(date.toString())
                },
                label = {
                    Text(
                        "Date",
                        color = Color(0xff8f9bb3),
                        fontWeight = FontWeight.Normal,
                        fontSize = 15.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        tint = Color(0xff8f9bb3),
                        contentDescription = "DateRange"
                    )
                },
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color(0xffedf1f7),
                    focusedBorderColor = Color(0xffedf1f7)
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = startTime,
                    onValueChange = {
                        callback.onStartTimeChanged(it)
                    },
                    label = {
                        Text(
                            "Start time",
                            color = Color(0xff8f9bb3),
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffedf1f7),
                        focusedBorderColor = Color(0xffedf1f7)
                    )
                )

                Spacer(modifier = Modifier.width(14.dp))

                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = endTime,
                    onValueChange = {
                        callback.onEndTimeChanged(it)
                    },
                    label = {
                        Text(
                            "End time",
                            color = Color(0xff8f9bb3),
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    },
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xffedf1f7),
                        focusedBorderColor = Color(0xffedf1f7)
                    )
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Reminds me",
                    color = Color(0xff222b45),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )

                IconToggleButton(checked = isRemindMe, onCheckedChange = {
                    callback.onIsRemindMeChanged()
                }) {

                }
            }

            Text(
                modifier = Modifier.padding(top = 15.dp),
                text = "Select Category",
                color = Color(0xff222b45),
                fontSize = 17.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

            FlowRow(
                modifier = Modifier
                    .padding(top = 17.dp)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val categoryList = listOf(
                    CalendarEventCategory.Brainstorm,
                    CalendarEventCategory.Design,
                    CalendarEventCategory.Workout
                )
                categoryList.forEach {
                    val text = it.key
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                callback.onCategoryChanged(it)
                            }
                            .background(Color(it.colorHex).copy(alpha = 0.07f))
                            .then(
                                if (it == category) {
                                    Modifier.border(
                                        width = 1.dp,
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color(it.colorHex)
                                    )
                                } else {
                                    Modifier
                                }
                            )
                            .padding(start = 13.dp, end = 11.dp, top = 14.dp, bottom = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(it.colorHex))
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(White)
                                    .align(Alignment.Center)
                            )
                        }

                        Spacer(modifier = Modifier.width(7.dp))

                        Text(
                            text = text,
                            color = Color(0xff222b45),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(7.dp))
                    .background(Color(0xff735bf2))
                    .clickable {
                        callback.requestEventAdd()
                    }
                    .padding(vertical = 14.dp)
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Create Event",
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

interface AddEventBottomSheetCallback {
    fun onDismiss()
    fun onTitleChanged(newTitle: String)
    fun onNoteChanged(newNote: String)
    fun onDateChanged(newDateStr: String)
    fun onStartTimeChanged(newStartTime: String)
    fun onEndTimeChanged(newEndTime: String)
    fun onIsRemindMeChanged()
    fun onCategoryChanged(newCategory: CalendarEventCategory)
    fun requestEventAdd()
}
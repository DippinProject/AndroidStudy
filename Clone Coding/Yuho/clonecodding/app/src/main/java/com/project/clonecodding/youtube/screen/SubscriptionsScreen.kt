package com.project.clonecodding.youtube.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.clonecodding.R
import com.project.clonecodding.common.BottomNavigationBar
import com.project.clonecodding.common.YoutubeTopBar
import com.project.clonecodding.ui.theme.Black
import com.project.clonecodding.ui.theme.ClonecoddingTheme
import com.project.clonecodding.ui.theme.White
import com.project.clonecodding.youtube.StatusBarColorsTheme
import com.project.clonecodding.youtube.model.DummyData.channelImgList
import com.project.clonecodding.youtube.model.DummyData.shortsList
import com.project.clonecodding.youtube.model.DummyData.videoList
import com.project.clonecodding.youtube.model.ShortsModel
import com.project.clonecodding.youtube.model.YoutubeVideoModel

@Composable
fun SubscriptionsScreen(navController: NavHostController) {
    val channelImgListState by remember {
        mutableStateOf(channelImgList)
    }
    val videoListState by remember {
        mutableStateOf(videoList)
    }
    val shortsListState by remember {
        mutableStateOf(shortsList)
    }

    StatusBarColorsTheme()

    ClonecoddingTheme {
        Scaffold(
            topBar = { YoutubeTopBar() },
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            Column(modifier = Modifier
                .background(White)
                .padding(innerPadding)) {
                SubscriptionsTopChannel(
                    channelList = channelImgListState,
                    modifier = Modifier.fillMaxWidth()
                )
                SubscriptionsBody(
                    videoList = videoListState,
                    shortsList = shortsListState,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SubscriptionsScreenPreview() {
    val channelImgListState by remember {
        mutableStateOf(channelImgList)
    }
    val videoListState by remember {
        mutableStateOf(videoList)
    }
    val shortsListState by remember {
        mutableStateOf(shortsList)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SubscriptionsTopChannel(
            channelList = channelImgListState,
            modifier = Modifier.fillMaxWidth()
        )
        SubscriptionsBody(
            videoList = videoListState,
            shortsList = shortsListState,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun SubscriptionsTopChannel(channelList: List<Int>, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(White)
            .padding(start = 12.dp, end = 12.dp, bottom = 7.dp)
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            items(items = channelList) { channelDrawableId ->
                Image(
                    modifier = Modifier.size(38.dp),
                    painter = painterResource(id = channelDrawableId),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun SubscriptionsBody(
    videoList: List<YoutubeVideoModel> = listOf(),
    shortsList: List<ShortsModel> = listOf(),
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        itemsIndexed(items = videoList) { idx, video ->
            YoutubeVideoItem(video = video, modifier = Modifier.fillMaxWidth())
            if (idx == 0) {
                SubscriptionsShorts(shortsList = shortsList, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun SubscriptionsShorts(shortsList: List<ShortsModel> = listOf(), modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_youtube_shorts_selected),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(13.dp))

                Text(
                    text = "Shorts", fontSize = 16.sp, color = Black, style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }

            TextButton(onClick = { /*TODO*/ }) {
                Text(
                    text = "View all",
                    color = Color(0xFF0074B5),
                    fontSize = 16.sp,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )
            }
        }

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = shortsList) { item ->
                SubscriptionsShortsItem(shorts = item, modifier = Modifier.width(169.dp))
            }
        }
    }
}

@Composable
fun SubscriptionsShortsItem(shorts: ShortsModel, modifier: Modifier = Modifier) {
    val ratio = 405f / 720f
    Box(
        modifier = modifier
            .aspectRatio(ratio)
            .clip(
                RoundedCornerShape(12.dp)
            )
    ) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = shorts.channelImgUrl,
            contentDescription = null
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.End
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.MoreVert,
                tint = Color.White,
                contentDescription = null
            )

            Box(modifier = Modifier.weight(1f))

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                text = shorts.title, color = White, fontSize = 12.sp, style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = shorts.viewers,
                color = Color(0xFFF2F2F2),
                fontSize = 8.sp,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}

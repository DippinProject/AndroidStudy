package com.project.clonecoding.presentation.youtube.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.clonecoding.presentation.R
import com.project.clonecoding.presentation.base.SimpleCircle
import com.project.clonecoding.presentation.base.SimpleLine
import com.project.clonecoding.presentation.common.BottomNavigationBar
import com.project.clonecoding.presentation.common.YoutubeTopBar
import com.project.clonecoding.presentation.theme.Black
import com.project.clonecoding.presentation.theme.ClonecoddingTheme
import com.project.clonecoding.presentation.theme.White
import com.project.clonecoding.presentation.youtube.StatusBarColorsTheme
import com.project.clonecoding.presentation.youtube.model.DummyData.playLists
import com.project.clonecoding.presentation.youtube.model.DummyData.videoList
import com.project.clonecoding.presentation.youtube.model.PlayListModel
import com.project.clonecoding.presentation.youtube.model.YoutubeVideoModel

@Composable
fun LibraryScreen(navController: NavHostController) {
    StatusBarColorsTheme()

    ClonecoddingTheme {
        Scaffold(
            topBar = { YoutubeTopBar() },
            bottomBar = { BottomNavigationBar(navController = navController) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(White)
                    .padding(innerPadding),
            ) {
                val videoHistoryState by remember {
                    mutableStateOf(videoList)
                }
                val playListsState by remember {
                    mutableStateOf(playLists)
                }
                LibraryHistory(videoHistory = videoHistoryState, modifier = Modifier.fillMaxWidth())

                SimpleLine(modifier = Modifier.padding(vertical = 26.dp))

                LibraryYourVideos(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(19.dp))

                LibraryYourMovies(modifier = Modifier.fillMaxWidth())

                SimpleLine(modifier = Modifier.padding(vertical = 26.dp))

                LibraryPlayLists(playLists = playListsState, modifier = Modifier.fillMaxWidth())


            }
        }
    }
}


@Composable
@Preview
fun LibraryScreenPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        val videoHistoryState by remember {
            mutableStateOf(videoList)
        }

        val playListsState by remember {
            mutableStateOf(playLists)
        }
        LibraryHistory(videoHistory = videoHistoryState, modifier = Modifier.fillMaxWidth())

        SimpleLine(modifier = Modifier.padding(vertical = 26.dp))

        LibraryYourVideos(modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(19.dp))

        LibraryYourMovies(modifier = Modifier.fillMaxWidth())

        SimpleLine(modifier = Modifier.padding(vertical = 26.dp))

        LibraryPlayLists(playLists = playListsState, modifier = Modifier.fillMaxWidth())
    }
}


@Composable
fun LibraryHistory(
    videoHistory: List<YoutubeVideoModel> = listOf(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        LibraryHistoryHeader(modifier = Modifier.fillMaxWidth())
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(items = videoHistory) { video ->
                LibraryHistoryItem(
                    item = video,
                    modifier = Modifier
                        .width(116.dp)
                        .wrapContentHeight()
                ) {
                    // click event
                }
            }
        }
    }
}

@Composable
fun LibraryHistoryHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp, vertical = 4.5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_youtube_history),
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(13.dp))

            Text(
                text = "History",
                color = Black,
                fontSize = 16.sp,
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }

        Text(
            text = "View all",
            color = Color(0xFF007485),
            fontSize = 16.sp,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}

@Composable
fun LibraryHistoryItem(
    item: YoutubeVideoModel,
    modifier: Modifier = Modifier,
    onHistoryClick: (YoutubeVideoModel) -> Unit
) {
    val imageRatio = 1280f / 720f
    Column(modifier = modifier.clickable { onHistoryClick(item) }) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageRatio)
                .clip(RoundedCornerShape(4.dp)),
            model = item.thumbnailImgUrl,
            contentScale = ContentScale.Fit,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontSize = 10.sp,
                    color = Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )

                Text(
                    text = item.channelName,
                    fontSize = 10.sp,
                    color = Color(0x80000000),
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.viewers,
                        fontSize = 10.sp,
                        color = Color(0x80000000),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )

                    SimpleCircle(
                        modifier = Modifier
                            .padding(horizontal = 1.5.dp)
                            .size(6.dp)
                    )

                    Text(
                        text = item.uploadDate,
                        fontSize = 10.sp,
                        color = Color(0x80000000),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }

            Icon(
                modifier = Modifier.width(12.dp),
                imageVector = Icons.Default.MoreVert,
                tint = Black,
                contentDescription = null
            )
        }
    }
}

@Composable
fun LibraryYourVideos(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_youtube_videos),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Your videos", fontSize = 16.sp, color = Black, style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}

@Composable
fun LibraryYourMovies(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_youtube_movies),
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Your Movies", fontSize = 16.sp, color = Black, style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )
    }
}

@Composable
fun LibraryPlayLists(playLists: List<PlayListModel> = listOf(), modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(horizontal = 10.dp)) {
        Text(
            modifier = Modifier.padding(horizontal = 2.dp),
            text = "Playlists", fontSize = 14.sp, color = Black, style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            )
        )

        Spacer(modifier = Modifier.height(26.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = playLists) { item ->
                LibraryPlayListsItem(item = item, modifier = Modifier.fillMaxWidth())
            }
        }

    }
}

@Composable
fun LibraryPlayListsItem(item: PlayListModel, modifier: Modifier = Modifier) {
    val imageRatio = 1280f / 720f
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .width(116.dp)
                .aspectRatio(imageRatio)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.Bottom),
        ) {

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = item.imgId),
                contentDescription = null
            )

            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xCC000000))
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        tint = White,
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_youtube_playlist),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    val countStr = if (item.videoCount > 811) "811+" else item.videoCount.toString()
                    Text(
                        text = countStr, color = White, fontSize = 10.sp, style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }

        }

        Spacer(modifier = Modifier.width(11.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title, fontSize = 10.sp, color = Black, style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = item.channelName,
                fontSize = 10.sp,
                color = Color(0x80000000),
                style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    )
                )
            )
        }
    }
}
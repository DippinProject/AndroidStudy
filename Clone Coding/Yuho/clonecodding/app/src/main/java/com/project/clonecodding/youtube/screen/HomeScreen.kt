package com.project.clonecodding.youtube.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.clonecodding.R
import com.project.clonecodding.base.SimpleCircle
import com.project.clonecodding.common.BottomNavigationBar
import com.project.clonecodding.common.YoutubeTopBar
import com.project.clonecodding.ui.theme.Black
import com.project.clonecodding.ui.theme.ClonecoddingTheme
import com.project.clonecodding.ui.theme.Gray10
import com.project.clonecodding.ui.theme.Gray20
import com.project.clonecodding.ui.theme.Gray30
import com.project.clonecodding.ui.theme.White
import com.project.clonecodding.youtube.StatusBarColorsTheme
import com.project.clonecodding.youtube.model.DummyData.videoList
import com.project.clonecodding.youtube.model.HomeContentType
import com.project.clonecodding.youtube.model.YoutubeVideoModel

@Composable
fun HomeScreen(navController: NavHostController) {
    StatusBarColorsTheme()

    ClonecoddingTheme {
        Scaffold(
            topBar = {
                YoutubeTopBar()
            },
            bottomBar = {
                BottomNavigationBar(navController = navController)
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(White)
            ) {
                var selectedChip by remember {
                    mutableStateOf<HomeContentType>(HomeContentType.All)
                }

                var youtubeVideos by remember {
                    mutableStateOf(videoList)
                }

                HomeHeaderChips(selectedChipItem = selectedChip) { item ->
                    if (selectedChip.type != item.type) {
                        selectedChip = item
                        // All일 경우, video 전체를 할당, All이 아닐 경우 해당되는 contentType으로 filtering
                        youtubeVideos = if (selectedChip.type == HomeContentType.All.type) {
                            videoList
                        } else {
                            videoList.filter { video -> video.contentType.type == selectedChip.type }
                        }
                    }
                }

                HomeBody(videoList = youtubeVideos, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        HomeHeaderChips(onChipClicked = {})
        HomeBody(modifier = Modifier.weight(1f))
    }
}


@Composable
fun HomeHeaderChips(
    modifier: Modifier = Modifier,
    selectedChipItem: HomeContentType = HomeContentType.All,
    onChipClicked: (HomeContentType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 6.dp, horizontal = 12.dp)
    ) {

        val chipItems = listOf(
            HomeContentType.Compass,
            HomeContentType.All,
            HomeContentType.Music,
            HomeContentType.Figma,
            HomeContentType.Mixes,
            HomeContentType.Graphics,
            HomeContentType.Games
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(9.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(items = chipItems) { item ->
                if (item.type == "compass") {
                    Image(
                        modifier = Modifier.size(30.75.dp, 24.75.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_youtube_compass),
                        contentDescription = null
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .width(1.dp)
                            .height(24.75.dp)
                            .padding(start = 9.dp),
                        color = Gray10,
                        thickness = 1.dp
                    )

                    Spacer(modifier = Modifier.width(9.dp))

                } else {
                    val (textColor, backgroundColor) = if (selectedChipItem.type == item.type) {
                        Pair(White, Gray30)
                    } else {
                        Pair(Black, Gray20)
                    }
                    Text(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(backgroundColor)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .clickable { onChipClicked(item) },
                        text = item.text,
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun HomeBody(videoList: List<YoutubeVideoModel> = listOf(), modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        LazyColumn {
            items(items = videoList) { video ->
                YoutubeVideoItem(video = video, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
fun YoutubeVideoItem(video: YoutubeVideoModel, modifier: Modifier) {
    val imageRatio = 1280f / 720f
    Column(modifier = modifier) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(imageRatio),
            model = video.thumbnailImgUrl,
            contentScale = ContentScale.Fit,
            contentDescription = null
        )
        Row(
            modifier = modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 16.dp)
                    .size(42.dp)
                    .clip(CircleShape),
                model = video.channelImgUrl,
                contentScale = ContentScale.Crop,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(30.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = video.title,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Black,
                    style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        )
                    )
                )

                Spacer(modifier = Modifier.height(1.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = video.channelName,
                        fontSize = 12.sp,
                        color = Color(0xFF939393),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )

                    SimpleCircle(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(6.dp)
                    )

                    Text(
                        text = video.viewers,
                        fontSize = 12.sp,
                        color = Color(0xFF939393),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )

                    SimpleCircle(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(6.dp)
                    )

                    Text(
                        text = video.uploadDate,
                        fontSize = 12.sp,
                        color = Color(0xFF939393),
                        style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            )
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.width(30.dp))

            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Default.MoreVert,
                contentDescription = null
            )
        }
    }
}

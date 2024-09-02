package com.project.clonecodding.youtube.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.clonecodding.R
import com.project.clonecodding.ui.theme.Black
import com.project.clonecodding.ui.theme.ClonecoddingTheme
import com.project.clonecodding.ui.theme.White
import com.project.clonecodding.youtube.StatusBarColorsTheme
import com.project.clonecodding.youtube.model.DummyData
import com.project.clonecodding.youtube.model.ShortsModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortsScreen(navController: NavHostController) {
    val shortsListState by remember {
        mutableStateOf(DummyData.shortsList)
    }
    val pagerState = rememberPagerState(pageCount = { shortsListState.size })

    StatusBarColorsTheme(true)

    ClonecoddingTheme {
        Scaffold { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                VerticalPager(
                    state = pagerState,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) { index ->
                    ShortsItem(shorts = shortsListState[index], modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun ShortsScreenPreview() {
    val shortsListState by remember {
        mutableStateOf(DummyData.shortsList)
    }

    val pagerState = rememberPagerState(pageCount = { shortsListState.size })

    VerticalPager(
        state = pagerState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) { index ->
        ShortsItem(shorts = shortsListState[index], modifier = Modifier.fillMaxSize())
    }

}


@Composable
fun ShortsItem(shorts: ShortsModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri("android.resource://com.project.clonecodding/${shorts.videoId}"))
            repeatMode = REPEAT_MODE_ONE
        }
    }
    val playerView = PlayerView(context).apply {
        this.player = player
        useController = false
    }
    val playWhenReady by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(player) {
        player.prepare()
        player.playWhenReady = playWhenReady
    }

    DisposableEffect(Unit) {
        onDispose {
            player.release()
        }
    }

    val shortsRatio = 405f / 720f
    Box(modifier = modifier.background(Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.weight(0.5f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(shortsRatio)
            ) {

                AndroidView(
                    factory = { playerView },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(shortsRatio)
                )

                ShortsFrame(
                    channelImgUrl = shorts.channelImgUrl,
                    channelName = shorts.channelName,
                    channelTitle = shorts.title,
                    likeCount = shorts.likeCount,
                    reviewCount = shorts.reviewCount,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(modifier = Modifier.weight(0.5f))

        }

    }
}

@Composable
fun ShortsFrame(
    channelImgUrl: String,
    channelName: String,
    channelTitle: String,
    likeCount: String,
    reviewCount: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.Bottom

        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = channelTitle,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = White,
                        fontSize = 14.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(width = 1.dp, color = Black, shape = CircleShape),
                            model = channelImgUrl,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )

                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f),
                            text = channelName,
                            color = White,
                            fontSize = 14.sp,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )

                        TextButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(White),
                            contentPadding = PaddingValues(horizontal = 19.dp, vertical = 10.dp),
                            shape = RoundedCornerShape(16.dp),
                            onClick = { /*TODO*/ }) {

                            Text(
                                text = "Subscribe",
                                color = Black,
                                fontSize = 16.sp,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_like),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = likeCount,
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_dislike),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Dislike",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_comment),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = reviewCount,
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_share),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Share",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_menu_hor),
                    tint = White,
                    contentDescription = null
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Black)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        model = channelImgUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}


@Composable
@Preview
fun ShortsFramePreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "zzzz",
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        color = White,
                        fontSize = 14.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(width = 1.dp, color = Black, shape = CircleShape),
                            model = "",
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(11.dp))

                        Text(
                            modifier = Modifier
                                .wrapContentHeight()
                                .weight(1f),
                            text = "zzzzz",
                            color = White,
                            fontSize = 14.sp,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )

                        TextButton(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(White),
                            contentPadding = PaddingValues(horizontal = 19.dp, vertical = 10.dp),
                            shape = RoundedCornerShape(16.dp),
                            onClick = { /*TODO*/ }) {

                            Text(
                                text = "Subscribe",
                                color = Black,
                                fontSize = 16.sp,
                                style = TextStyle(
                                    platformStyle = PlatformTextStyle(
                                        includeFontPadding = false
                                    )
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(32.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_like),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "133",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_dislike),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Dislike",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_dislike),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "143",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Column {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(id = R.drawable.ic_dislike),
                        tint = White,
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Share",
                        color = White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraLight,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }

                Icon(
                    modifier = Modifier.size(30.dp),
                    painter = painterResource(id = R.drawable.ic_menu_hor),
                    tint = White,
                    contentDescription = null
                )

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Black)
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        model = "",
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
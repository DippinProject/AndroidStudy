package com.project.clonecodding.tinder.screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.clonecodding.R
import com.project.clonecodding.tinder.TinderEvent
import com.project.clonecodding.tinder.model.TinderModel
import com.project.clonecodding.tinder.viewmodel.TinderViewModel
import com.project.clonecodding.ui.theme.ClonecoddingTheme
import kotlinx.coroutines.launch

@Composable
fun TinderScreen(navController: NavHostController, tinderViewModel: TinderViewModel = viewModel()) {
    ClonecoddingTheme {
        Scaffold { innerPadding ->
            TinderScreenBody(
                modifier = Modifier.padding(innerPadding),
                tinderViewModel = tinderViewModel
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun TinderScreenBody(
    modifier: Modifier = Modifier,
    tinderViewModel: TinderViewModel = viewModel()
) {
    val state = tinderViewModel.state.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { state.value.modelList.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(11.dp))

        Image(
            modifier = Modifier.size(42.dp),
            painter = painterResource(id = R.drawable.ic_tinder_logo),
            contentDescription = "tinder_logo"
        )

        Spacer(modifier = Modifier.height(50.dp))

        HorizontalPager(
            modifier = Modifier
                .weight(1f),
            state = pagerState,
            verticalAlignment = Alignment.CenterVertically
        ) { index ->
            TinderCardItem(
                page = index,
                currentPage = pagerState.currentPage,
                item = state.value.modelList[index],
                animationDuration = 2000,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 26.dp)

            )
        }

        TinderBottomButton(
            onDislikeClick = {
                coroutineScope.launch {
                    val prevPage = (pagerState.currentPage - 1).coerceAtLeast(0)
                    pagerState.animateScrollToPage(prevPage)
                }
                tinderViewModel.onEvent(TinderEvent.OnDislike)
            },
            onFavoriteClick = {
                tinderViewModel.onEvent(TinderEvent.OnFavorite)
            },
            onLikeClick = {
                coroutineScope.launch {
                    val prevPage = (pagerState.currentPage + 1).coerceAtLeast(0)
                    pagerState.animateScrollToPage(prevPage)
                }
                tinderViewModel.onEvent(TinderEvent.OnLike)
            }
        )
    }

}

@Composable
fun TinderBottomButton(
    modifier: Modifier = Modifier,
    onDislikeClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onLikeClick: () -> Unit
) {
    Row(modifier = modifier.padding(vertical = 30.dp)) {
        IconButton(modifier = Modifier.size(62.dp), onClick = onDislikeClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_tinder_dislike),
                contentDescription = "tinder_dislike"
            )
        }
        Spacer(modifier = Modifier.width(44.dp))
        IconButton(modifier = Modifier.size(42.dp), onClick = onFavoriteClick) {
            Image(
                painter = painterResource(id = R.drawable.ic_tinder_favorite),
                contentDescription = "tinder_favorite"
            )
        }
        Spacer(modifier = Modifier.width(44.dp))
        IconButton(modifier = Modifier.size(62.dp), onClick = onLikeClick) {

            Image(
                painter = painterResource(id = R.drawable.ic_tinder_like),
                contentDescription = "tinder_like"
            )
        }

    }
}

@Composable
fun TinderCardItem(
    page: Int,
    currentPage: Int,
    item: TinderModel,
    animationDuration: Int,
    modifier: Modifier = Modifier
) {
    val transition = updateTransition(targetState = page == currentPage, label = "pageTransition")

    val translationX by transition.animateFloat(
        transitionSpec = { tween(durationMillis = animationDuration) },
        label = "translationX"
    ) { isCurrentPage ->
        if (isCurrentPage) 0f else 200f
    }

    val translationY by transition.animateFloat(
        transitionSpec = { tween(durationMillis = animationDuration) },
        label = "translationY"
    ) { isCurrentPage ->
        if (isCurrentPage) 0f else -200f
    }

    val rotationZ by transition.animateFloat(
        transitionSpec = { tween(durationMillis = animationDuration) },
        label = "rotationZ"
    ) { isCurrentPage ->
        if (isCurrentPage) 0f else -45f
    }


    Box(modifier = modifier
        .clip(RoundedCornerShape(30.dp))
        .graphicsLayer {
            this.translationX = translationX
            this.translationY = translationY
            this.rotationZ = rotationZ
            cameraDistance = 8 * density
        }) {
        AsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = item.imgUrl,
            contentScale = ContentScale.Crop,
            contentDescription = "${item.imgUrl}"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 22.dp, vertical = 22.dp)
                .align(Alignment.BottomCenter)
        ) {

            val title = "${item.name},${item.age}"
            val sub1 = "Lives in ${item.place}"
            val sub2 = "${item.distance} miles away"
            Text(
                text = title,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = sub1,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = sub2,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
        }
    }
}
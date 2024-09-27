package com.project.clonecoding.presentation.youtube

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.clonecoding.presentation.R
import com.project.clonecoding.presentation.theme.Black
import com.project.clonecoding.presentation.theme.ClonecoddingTheme
import com.project.clonecoding.presentation.theme.White
import com.project.clonecoding.presentation.youtube.screen.HomeScreen
import com.project.clonecoding.presentation.youtube.screen.LibraryScreen
import com.project.clonecoding.presentation.youtube.screen.ShortsScreen
import com.project.clonecoding.presentation.youtube.screen.SubscriptionsScreen

class YoutubeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClonecoddingTheme {
                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }
}

@Composable
private fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        modifier = modifier
    ) {
        composable(BottomNavItem.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(BottomNavItem.Shorts.route) {
            ShortsScreen(navController = navController)
        }
        composable(BottomNavItem.Subscriptions.route) {
            SubscriptionsScreen(navController = navController)
        }
        composable(BottomNavItem.Library.route) {
            LibraryScreen(navController = navController)
        }
    }
}


@Composable
fun StatusBarColorsTheme(isDarkTheme: Boolean = isSystemInDarkTheme()) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = if(isDarkTheme){
                Black.toArgb()
            }else{
                White.toArgb()
            }

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = !isDarkTheme
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = !isDarkTheme
        }
    }
}

sealed class BottomNavItem(
    val route: String,
    val titleId: Int,
    val iconId: Int,
    val selectedIconId: Int
) {
    data object Home : BottomNavItem(
        "home",
        R.string.nav_home,
        R.drawable.ic_youtube_home,
        R.drawable.ic_youtube_home_selected
    )

    data object Shorts :
        BottomNavItem(
            "shorts",
            R.string.nav_shorts,
            R.drawable.ic_youtube_shorts,
            R.drawable.ic_youtube_shorts
        )

    data object Subscriptions : BottomNavItem(
        "subscriptions",
        R.string.nav_subscriptions,
        R.drawable.ic_youtube_subscriptions,
        R.drawable.ic_youtube_subscriptions_selected
    )

    data object Library : BottomNavItem(
        "library",
        R.string.nav_library,
        R.drawable.ic_youtube_library,
        R.drawable.ic_youtube_library_selected
    )
}

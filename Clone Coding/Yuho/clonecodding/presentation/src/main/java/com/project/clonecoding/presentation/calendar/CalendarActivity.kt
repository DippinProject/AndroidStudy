package com.project.clonecoding.presentation.calendar

import android.app.Activity
import android.graphics.PointF
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.clonecoding.presentation.R
import com.project.clonecoding.presentation.theme.Black
import com.project.clonecoding.presentation.theme.ClonecoddingTheme
import com.project.clonecoding.presentation.theme.White
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CalendarActivity: ComponentActivity() {
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
        startDestination = CalendarNavItem.Home.route,
        modifier = modifier
    ) {
        composable(CalendarNavItem.Home.route) {
            CalendarScreen()
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

@Composable
fun CustomCurvedShape(modifier: Modifier = Modifier, onAddEvent: () -> Unit) {
    var botNavWidthSize by remember { mutableIntStateOf(0) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .onSizeChanged {
            botNavWidthSize = it.width
        }) {
        Canvas(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp)
        ) {
            val circleSize = size.width * 51f / 390f
            val width = size.width
            val height = size.height

            // Path 객체를 사용하여 곡선을 그림
            val path = Path().apply {

                val firstCurveStartPoint =
                    PointF(width / 2 - circleSize * 1.5f, 0f)
                val firstCurveEndPoint = PointF(width / 2 - circleSize * 0.5f, height * 3 / 5f)
                val firstCurveControlPoint1 = PointF(
                    width / 2 - (circleSize * 5 / 6f),
                    firstCurveStartPoint.y
                )
                val firstCurveControlPoint2 = PointF(
                    firstCurveEndPoint.x - (circleSize * 4 / 6f),
                    firstCurveEndPoint.y
                )

                val secondCurveStartPoint = PointF(width / 2 + circleSize * 0.5f, height * 3 / 5f)
                val secondCurveEndPoint = PointF(width / 2 + circleSize * 1.5f, 0f)
                val secondCurveControlPoint1 = PointF(
                    secondCurveStartPoint.x + circleSize - (circleSize / 4),
                    secondCurveStartPoint.y
                )
                val secondCurveControlPoint2 = PointF(
                    width / 2 + (circleSize * 5 / 6f),
                    secondCurveEndPoint.y
                )

                // 왼쪽 상단에서 시작
                moveTo(0f, height / 4f)
                lineTo(firstCurveStartPoint.x, firstCurveStartPoint.y)

                cubicTo(
                    firstCurveControlPoint1.x, firstCurveControlPoint1.y,
                    firstCurveControlPoint2.x, firstCurveControlPoint2.y,
                    firstCurveEndPoint.x, firstCurveEndPoint.y
                )
                quadraticBezierTo(
                    width / 2f,
                    height * 62f / 100f,
                    secondCurveStartPoint.x,
                    secondCurveStartPoint.y
                )

                cubicTo(
                    secondCurveControlPoint1.x, secondCurveControlPoint1.y,
                    secondCurveControlPoint2.x, secondCurveControlPoint2.y,
                    secondCurveEndPoint.x, secondCurveEndPoint.y
                )

                lineTo(width, height / 4f)

                lineTo(width, height)
                lineTo(0f, height)
                close()
            }


            for (i in 1..100) {
                translate(top = -14 + 0.1f * i) {
                    drawPath(
                        path = path,
                        color = Color.Black.copy(alpha = 0.001f * i),
                        style = Stroke(width = 0.05f),
                    )
                }
            }

            // Path 내부를 흰색으로 채움
            drawPath(
                path = path,
                color = Color.White,
                style = Fill
            )
        }


        Box(
            Modifier
                .then(
                    with(LocalDensity.current) {
                        Modifier.size(
                            width = (botNavWidthSize * 51f / 390f).toDp(),
                            height = (botNavWidthSize * 51f / 390f).toDp(),
                        )
                    }
                )
                .clip(CircleShape)
                .background(Color(0xff735bf2))
                .align(Alignment.TopCenter)
                .clickable {
                    onAddEvent()
                }
        ) {
            Image(
                modifier = Modifier.align(Alignment.Center),
                painter = painterResource(id = R.drawable.ic_add_plus),
                contentDescription = "icon_add_event"
            )
        }
    }

}

sealed class CalendarNavItem(
    val route: String,
    val titleId: Int
) {
    data object Home : CalendarNavItem(
        "home",
        R.string.nav_home,
    )
}

package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onDismiss: () -> Unit,
  modifier: Modifier = Modifier
) {
  LaunchedEffect(Unit) {
    delay(2200)
    onDismiss()
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 1.0f,
    targetValue = 1.08f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Slate900)
      .clickable { onDismiss() }
      .testTag("splash_screen"),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(24.dp)
    ) {
      // Pulsing Glowing Logo
      Box(
        modifier = Modifier
          .size(140.dp)
          .scale(pulseScale)
          .shadow(
            elevation = 24.dp,
            shape = CircleShape,
            ambientColor = AmberAccent,
            spotColor = GoldYellow
          )
          .clip(CircleShape)
          .border(3.dp, AmberAccent, CircleShape)
          .background(Slate800),
        contentAlignment = Alignment.Center
      ) {
        Image(
          painter = painterResource(id = R.drawable.sadra_chain_logo),
          contentDescription = "Sadra Chain Logo",
          contentScale = ContentScale.Crop,
          modifier = Modifier.fillMaxSize()
        )
      }

      Spacer(modifier = Modifier.height(28.dp))

      Text(
        text = "صدرہ چین",
        style = MaterialTheme.typography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          color = AmberAccent,
          fontSize = 28.sp,
          textAlign = TextAlign.Center
        )
      )

      Text(
        text = "Sadra Chain",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.SemiBold,
          color = GoldYellow,
          fontSize = 18.sp,
          letterSpacing = 1.5.sp,
          textAlign = TextAlign.Center
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
        text = "آن لائن ارننگ اور انویسٹمنٹ پلیٹ فارم",
        style = MaterialTheme.typography.bodyMedium.copy(
          color = Slate400,
          fontSize = 14.sp,
          textAlign = TextAlign.Center
        )
      )

      Spacer(modifier = Modifier.height(36.dp))

      // Custom Spinner Loader
      CircularProgressIndicator(
        modifier = Modifier
          .size(44.dp)
          .testTag("splash_loader"),
        color = AmberAccent,
        trackColor = Slate800,
        strokeWidth = 4.dp
      )

      Spacer(modifier = Modifier.height(20.dp))

      Text(
        text = "لوڈنگ ہو رہی ہے...",
        style = MaterialTheme.typography.bodySmall.copy(
          color = Slate400,
          fontSize = 12.sp
        )
      )
    }
  }
}

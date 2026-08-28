package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ActivePlan
import com.example.data.SadraNavTab
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.PurpleAdmin
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import java.util.Locale

@Composable
fun DashboardScreen(
  balance: Double,
  timerFormatted: String,
  timerSeconds: Int,
  activePlans: List<ActivePlan>,
  onClaimDaily: () -> Unit,
  onNavigateToTab: (SadraNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  fun openUrl(url: String) {
    try {
      val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
      context.startActivity(intent)
    } catch (_: Exception) {
      // Fallback
    }
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulseScale"
  )

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("dashboard_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header Title
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "صدرہ چین (Sadra Chain)",
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 22.sp
          ),
          textAlign = TextAlign.Center
        )
        Text(
          text = "خوش آمدید! آپ کا محفوظ منافع ڈیش بورڈ",
          style = MaterialTheme.typography.bodySmall.copy(
            color = Slate400,
            fontSize = 12.sp
          ),
          textAlign = TextAlign.Center
        )
      }
    }

    // 1. Live Balance Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("live_balance_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Box(
              modifier = Modifier
                .size(10.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(EmeraldGreen)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "موجودہ لائیو بیلنس (روپے):",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = Slate400,
                fontSize = 14.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Big Glowing Balance Ticker (formatted to 4 decimals as in HTML: 250.0000)
          Text(
            text = "Rs. ${String.format(Locale.US, "%.4f", balance)}",
            style = MaterialTheme.typography.headlineMedium.copy(
              fontWeight = FontWeight.ExtraBold,
              color = EmeraldGreen,
              fontSize = 32.sp,
              textAlign = TextAlign.Center
            ),
            modifier = Modifier.testTag("live_balance_text")
          )

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = "⚡ لائیو منافع خودکار طور پر شامل ہو رہا ہے",
            style = MaterialTheme.typography.bodySmall.copy(
              color = SkyBlue,
              fontSize = 12.sp,
              fontWeight = FontWeight.Medium
            )
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = { onNavigateToTab(SadraNavTab.PLANS) },
              colors = ButtonDefaults.buttonColors(containerColor = RoyalBlue),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("invest_now_btn")
            ) {
              Icon(Icons.Filled.TrendingUp, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("پلان خریدیں", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }

            Button(
              onClick = { onNavigateToTab(SadraNavTab.WALLET) },
              colors = ButtonDefaults.buttonColors(containerColor = Slate700),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("withdraw_now_btn")
            ) {
              Icon(Icons.Filled.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("ودڈرا کریں", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
            }
          }
        }
      }
    }

    // 2. 24-Hour Profit Countdown Timer Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("profit_timer_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Filled.HourglassTop,
              contentDescription = null,
              tint = GoldYellow,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "اگلے پرافٹ کا 24 گھنٹے کا ٹائمر:",
              style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Timer Box matching .timer-box style from HTML
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(10.dp))
              .background(Slate700)
              .border(1.dp, Slate600, RoundedCornerShape(10.dp))
              .padding(vertical = 16.dp)
              .testTag("countdown_timer_box"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = timerFormatted,
              style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = GoldYellow,
                fontSize = 28.sp,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
              )
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          val progress = (86400 - timerSeconds).toFloat() / 86400f
          LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
              .fillMaxWidth()
              .height(6.dp)
              .clip(RoundedCornerShape(3.dp)),
            color = GoldYellow,
            trackColor = Slate700
          )

          Spacer(modifier = Modifier.height(14.dp))

          Button(
            onClick = onClaimDaily,
            colors = ButtonDefaults.buttonColors(
              containerColor = EmeraldGreen,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("claim_daily_btn")
          ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ڈیلی منافع کلیم کریں (Claim Daily Profit)",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }

    // Active Plans Banner (if any active)
    if (activePlans.isNotEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Slate800)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "🔥 آپ کے ایکٹو پلانز (${activePlans.size})",
              fontWeight = FontWeight.Bold,
              color = SkyBlue,
              fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            activePlans.forEach { plan ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(plan.name, color = Color.White, fontSize = 13.sp)
                Text(
                  "+Rs. ${plan.dailyProfit.toInt()}/دن",
                  color = EmeraldGreen,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp
                )
              }
            }
          }
        }
      }
    }

    // 3. Social Links and Support Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("social_links_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Filled.SupportAgent,
              contentDescription = null,
              tint = SkyBlue,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "سوشل لنکس اور سپورٹ",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 16.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // WhatsApp Channel Button
          Button(
            onClick = { openUrl("https://whatsapp.com/channel/sadrachain") },
            colors = ButtonDefaults.buttonColors(
              containerColor = DarkEmerald,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("whatsapp_channel_btn")
          ) {
            Text(
              text = "📢 واٹس ایپ چینل جوائن کریں",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // WhatsApp Group Button
          Button(
            onClick = { openUrl("https://chat.whatsapp.com/sadrachain_group") },
            colors = ButtonDefaults.buttonColors(
              containerColor = DarkEmerald,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("whatsapp_group_btn")
          ) {
            Text(
              text = "💬 واٹس ایپ گروپ جوائن کریں",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Telegram Admin Contact Button
          Button(
            onClick = { openUrl("https://t.me/sadrachain_admin") },
            colors = ButtonDefaults.buttonColors(
              containerColor = PurpleAdmin,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("admin_contact_btn")
          ) {
            Text(
              text = "👤 ایڈمن سے رابطہ کریں (Telegram Support)",
              fontWeight = FontWeight.Bold,
              fontSize = 14.sp
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

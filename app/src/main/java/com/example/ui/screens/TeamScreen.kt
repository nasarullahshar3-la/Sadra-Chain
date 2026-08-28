package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import java.util.Locale

@Composable
fun TeamScreen(
  referralLink: String,
  referralCode: String,
  teamMembers: Int,
  commission: Double,
  onCopyLink: (String) -> Unit,
  onSimulateReferral: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  fun shareReferral() {
    val sendIntent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(
        Intent.EXTRA_TEXT,
        "صدرہ چین (Sadra Chain) آن لائن انویسٹمنٹ پلیٹ فارم جوائن کریں اور روزانہ منافع حاصل کریں!\nریفرل لنک: $referralLink"
      )
      type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "شیئر کریں بذریعہ"))
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("team_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Referral Link Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .testTag("referral_link_card"),
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
          Box(
            modifier = Modifier
              .size(48.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(Slate700),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              Icons.Filled.CardGiftcard,
              contentDescription = null,
              tint = SkyBlue,
              modifier = Modifier.size(28.dp)
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          Text(
            text = "آپ کا ریفرل لنک",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = Color.White,
              fontSize = 18.sp
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = "اپنے دوستوں کو جوڑیں اور کمیشن حاصل کریں",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Slate400,
              fontSize = 13.sp
            ),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Readonly Link Box matching HTML <input type="text" ... readonly>
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Slate700)
              .border(1.dp, Slate600, RoundedCornerShape(8.dp))
              .padding(horizontal = 14.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = referralLink,
              color = Color.White,
              fontSize = 13.sp,
              textAlign = TextAlign.Center
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            // Sky Blue "لنک کاپی کریں" button from HTML
            Button(
              onClick = { onCopyLink(referralLink) },
              colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = Slate900
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("copy_referral_link_btn")
            ) {
              Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "لنک کاپی کریں",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }

            Button(
              onClick = { shareReferral() },
              colors = ButtonDefaults.buttonColors(
                containerColor = DarkEmerald,
                contentColor = Color.White
              ),
              shape = RoundedCornerShape(8.dp),
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("share_referral_btn")
            ) {
              Icon(Icons.Filled.Share, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "شیئر کریں",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
              )
            }
          }
        }
      }
    }

    // 2. Team Details Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("team_details_card"),
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
            Icon(Icons.Filled.Group, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "آپ کی ٹیم کی تفصیلات",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 16.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Card(
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(containerColor = Slate700),
              modifier = Modifier.weight(1f)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text("کل ٹیم ممبرز", color = Slate400, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "$teamMembers",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 24.sp
                  )
                )
              }
            }

            Card(
              shape = RoundedCornerShape(10.dp),
              colors = CardDefaults.cardColors(containerColor = Slate700),
              modifier = Modifier.weight(1f)
            ) {
              Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Text("ریفرل کمیشن", color = Slate400, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = "Rs. ${String.format(Locale.US, "%.2f", commission)}",
                  style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen,
                    fontSize = 20.sp
                  )
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Simulated Referral Action for Testing
          Button(
            onClick = onSimulateReferral,
            colors = ButtonDefaults.buttonColors(containerColor = Slate700),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(40.dp)
              .testTag("simulate_referral_btn")
          ) {
            Icon(Icons.Filled.PersonAdd, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "+ ٹیم ممبر اور کمیشن ٹیسٹ کریں",
              color = SkyBlue,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
        }
      }
    }

    // 3. Referral Commission Structure Table
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "📊 ریفرل لیول کمیشن چارٹ",
            fontWeight = FontWeight.Bold,
            color = GoldYellow,
            fontSize = 15.sp
          )
          Spacer(modifier = Modifier.height(10.dp))

          listOf(
            Triple("لیول 1 (Level 1)", "ڈائریکٹ ریفرل", "10%"),
            Triple("لیول 2 (Level 2)", "سیکنڈری ریفرل", "5%"),
            Triple("لیول 3 (Level 3)", "ٹیم نیٹ ورک", "2%")
          ).forEach { (level, desc, percent) ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(level, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(desc, color = Slate400, fontSize = 11.sp)
              }
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(Slate700)
                  .padding(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(percent, color = EmeraldGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}

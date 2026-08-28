package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InvestmentPlan
import com.example.data.PlansRepository
import com.example.ui.components.BuyPlanDialog
import com.example.ui.components.DepositDialog
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBorder
import com.example.ui.theme.AmberDark
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800

@Composable
fun PlansScreen(
  balance: Double,
  onBuyPlan: (InvestmentPlan) -> Unit,
  onSubmitDeposit: (amount: String, method: String, senderNumber: String, trxId: String) -> Unit,
  onCopyText: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedPlanForBuy by remember { mutableStateOf<InvestmentPlan?>(null) }
  var showDepositDialog by remember { mutableStateOf(false) }

  if (selectedPlanForBuy != null) {
    BuyPlanDialog(
      plan = selectedPlanForBuy!!,
      currentBalance = balance,
      onDismiss = { selectedPlanForBuy = null },
      onConfirmBuy = {
        onBuyPlan(selectedPlanForBuy!!)
      },
      onDepositClick = {
        showDepositDialog = true
      }
    )
  }

  if (showDepositDialog) {
    DepositDialog(
      onDismiss = { showDepositDialog = false },
      onSubmitDeposit = onSubmitDeposit,
      onCopyText = onCopyText
    )
  }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("plans_screen"),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // 1. Official Deposit Accounts Box
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .border(1.dp, AmberBorder, RoundedCornerShape(12.dp))
          .testTag("deposit_official_box"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = AmberDark),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "💳 ڈپازٹ کے آفیشل نمبرز",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = GoldYellow,
              fontSize = 16.sp
            ),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Easypaisa Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(Slate800.copy(alpha = 0.6f))
              .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("Easypaisa: ", color = Slate400, fontSize = 13.sp)
              Text(PlansRepository.officialEasypaisa, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            IconButton(
              onClick = { onCopyText(PlansRepository.officialEasypaisa) },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Filled.ContentCopy, contentDescription = "Copy Easypaisa", tint = GoldYellow, modifier = Modifier.size(16.dp))
            }
          }

          Spacer(modifier = Modifier.height(6.dp))

          // JazzCash Row
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(Slate800.copy(alpha = 0.6f))
              .padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text("JazzCash: ", color = Slate400, fontSize = 13.sp)
              Text(PlansRepository.officialJazzCash, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
            IconButton(
              onClick = { onCopyText(PlansRepository.officialJazzCash) },
              modifier = Modifier.size(28.dp)
            ) {
              Icon(Icons.Filled.ContentCopy, contentDescription = "Copy JazzCash", tint = GoldYellow, modifier = Modifier.size(16.dp))
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { showDepositDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(38.dp)
              .testTag("open_deposit_dialog_btn")
          ) {
            Icon(Icons.Filled.Payments, contentDescription = null, tint = Color.Black, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ڈپازٹ سلپ / Trx ID جمع کروائیں",
              color = Color.Black,
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
            )
          }
        }
      }
    }

    // 2. Heading
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "انویسٹمنٹ پلانز (250 تا 22,000)",
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 17.sp
          )
        )
        Text(
          text = "10 پلانز دستیاب",
          color = SkyBlue,
          fontSize = 12.sp
        )
      }
    }

    // 3. 10 Plans Cards
    items(PlansRepository.allPlans, key = { it.id }) { plan ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("plan_card_${plan.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Slate700),
              contentAlignment = Alignment.Center
            ) {
              Text(plan.iconEmoji, fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
              Text(
                text = plan.titleText,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 15.sp
                )
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = plan.dailyReturnText,
                style = MaterialTheme.typography.bodySmall.copy(
                  color = Slate400,
                  fontSize = 13.sp
                )
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Green "خریدیں" button as in HTML
          Button(
            onClick = { selectedPlanForBuy = plan },
            colors = ButtonDefaults.buttonColors(
              containerColor = EmeraldGreen,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .height(38.dp)
              .testTag("buy_plan_btn_${plan.id}")
          ) {
            Text(
              text = "خریدیں",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp
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

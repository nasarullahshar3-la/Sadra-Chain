package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.InvestmentPlan
import com.example.data.PlansRepository
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberBorder
import com.example.ui.theme.AmberDark
import com.example.ui.theme.DarkEmerald
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900
import java.util.Locale

@Composable
fun DepositDialog(
  onDismiss: () -> Unit,
  onSubmitDeposit: (amount: String, method: String, senderNumber: String, trxId: String) -> Unit,
  onCopyText: (String) -> Unit
) {
  var selectedMethod by remember { mutableStateOf("Easypaisa") }
  var amountText by remember { mutableStateOf("") }
  var senderNumber by remember { mutableStateOf("") }
  var trxId by remember { mutableStateOf("") }
  var errorMessage by remember { mutableStateOf<String?>(null) }

  val targetNumber = if (selectedMethod == "Easypaisa") PlansRepository.officialEasypaisa else PlansRepository.officialJazzCash

  AlertDialog(
    onDismissRequest = onDismiss,
    modifier = Modifier.testTag("deposit_dialog"),
    containerColor = Slate800,
    shape = RoundedCornerShape(16.dp),
    title = {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "💳 رقم ڈپازٹ کریں (Deposit Funds)",
          style = MaterialTheme.typography.titleMedium.copy(
            color = GoldYellow,
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          )
        )
        IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
          Icon(Icons.Filled.Close, contentDescription = "Close", tint = Slate400)
        }
      }
    },
    text = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        // Method Selector Tabs
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          listOf("Easypaisa", "JazzCash").forEach { method ->
            val isSelected = selectedMethod == method
            Box(
              modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(if (isSelected) RoyalBlue else Slate700)
                .clickable { selectedMethod = method }
                .padding(vertical = 10.dp),
              contentAlignment = Alignment.Center
            ) {
              Text(
                text = if (method == "Easypaisa") "ایزی پیسہ (Easypaisa)" else "جاز کیش (JazzCash)",
                color = if (isSelected) Color.White else Slate400,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 12.sp,
                textAlign = TextAlign.Center
              )
            }
          }
        }

        // Account Details Box
        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = AmberDark),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, AmberBorder, RoundedCornerShape(8.dp))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "$selectedMethod آفیشل نمبر:",
                color = GoldYellow,
                fontSize = 11.sp
              )
              Text(
                text = targetNumber,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
              )
            }
            Button(
              onClick = { onCopyText(targetNumber) },
              colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
              shape = RoundedCornerShape(6.dp),
              modifier = Modifier.height(34.dp)
            ) {
              Icon(Icons.Filled.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Black)
              Spacer(modifier = Modifier.width(4.dp))
              Text("کاپی", color = Color.Black, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
          }
        }

        // Input Fields
        OutlinedTextField(
          value = amountText,
          onValueChange = { amountText = it; errorMessage = null },
          label = { Text("رقم (PKR) درج کریں - کم از کم 250") },
          placeholder = { Text("مثال: 1000") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SkyBlue,
            unfocusedBorderColor = Slate600,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = SkyBlue,
            unfocusedLabelColor = Slate400
          )
        )

        OutlinedTextField(
          value = senderNumber,
          onValueChange = { senderNumber = it; errorMessage = null },
          label = { Text("آپ کا بھیجنے والا اکاؤنٹ نمبر") },
          placeholder = { Text("03XXXXXXXXX") },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SkyBlue,
            unfocusedBorderColor = Slate600,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = SkyBlue,
            unfocusedLabelColor = Slate400
          )
        )

        OutlinedTextField(
          value = trxId,
          onValueChange = { trxId = it; errorMessage = null },
          label = { Text("ٹرانزیکشن ID (Trx ID / TID)") },
          placeholder = { Text("مثال: 1234567890") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = SkyBlue,
            unfocusedBorderColor = Slate600,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedLabelColor = SkyBlue,
            unfocusedLabelColor = Slate400
          )
        )

        if (errorMessage != null) {
          Text(
            text = errorMessage!!,
            color = Color(0xFFEF4444),
            fontSize = 12.sp
          )
        }
      }
    },
    confirmButton = {
      Button(
        onClick = {
          if (amountText.isBlank() || trxId.isBlank()) {
            errorMessage = "براہ کرم رقم اور Trx ID دونوں درج کریں"
          } else {
            onSubmitDeposit(amountText, selectedMethod, senderNumber, trxId)
            onDismiss()
          }
        },
        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(44.dp)
          .testTag("submit_deposit_btn")
      ) {
        Text("ڈپازٹ جمع کروائیں (Submit Deposit)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
      }
    }
  )
}

@Composable
fun BuyPlanDialog(
  plan: InvestmentPlan,
  currentBalance: Double,
  onDismiss: () -> Unit,
  onConfirmBuy: () -> Unit,
  onDepositClick: () -> Unit
) {
  val hasEnoughBalance = currentBalance >= plan.price

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = Slate800,
    shape = RoundedCornerShape(16.dp),
    modifier = Modifier.testTag("buy_plan_dialog"),
    title = {
      Text(
        text = "پلان کی تصدیق: ${plan.urduName} (${plan.englishName})",
        style = MaterialTheme.typography.titleMedium.copy(
          color = GoldYellow,
          fontWeight = FontWeight.Bold,
          fontSize = 18.sp
        )
      )
    },
    text = {
      Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = "پلان کی تفصیلات:",
          color = Slate400,
          fontSize = 13.sp
        )

        Card(
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(containerColor = Slate700),
          modifier = Modifier.fillMaxWidth()
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("انویسٹمنٹ قیمت:", color = Slate400, fontSize = 13.sp)
              Text("Rs. ${plan.price.toInt()}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("روزانہ منافع (Daily Profit):", color = Slate400, fontSize = 13.sp)
              Text("Rs. ${plan.dailyProfit.toInt()}", color = EmeraldGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text("کل ریٹرن (30 دن):", color = Slate400, fontSize = 13.sp)
              Text("Rs. ${(plan.dailyProfit * plan.durationDays).toInt()}", color = SkyBlue, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text("آپ کا موجودہ بیلنس:", color = Slate400, fontSize = 13.sp)
          Text("Rs. ${String.format(Locale.US, "%.2f", currentBalance)}", color = if (hasEnoughBalance) EmeraldGreen else Color(0xFFEF4444), fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        if (!hasEnoughBalance) {
          Text(
            text = "⚠️ بیلنس ناکافی ہے! مزید Rs. ${(plan.price - currentBalance).toInt()} درکار ہیں۔",
            color = Color(0xFFEF4444),
            fontSize = 12.sp
          )
        }
      }
    },
    confirmButton = {
      if (hasEnoughBalance) {
        Button(
          onClick = {
            onConfirmBuy()
            onDismiss()
          },
          colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("confirm_buy_btn")
        ) {
          Text("ابھی خریدیں (Confirm Buy)", fontWeight = FontWeight.Bold)
        }
      } else {
        Button(
          onClick = {
            onDismiss()
            onDepositClick()
          },
          colors = ButtonDefaults.buttonColors(containerColor = AmberAccent),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.testTag("go_to_deposit_btn")
        ) {
          Text("ڈپازٹ کریں (Deposit Funds)", color = Color.Black, fontWeight = FontWeight.Bold)
        }
      }
    },
    dismissButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = Slate700)
      ) {
        Text("منسوخ کریں (Cancel)", color = Slate400)
      }
    }
  )
}

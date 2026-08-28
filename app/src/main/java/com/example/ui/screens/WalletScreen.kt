package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.WithdrawalItem
import com.example.data.WithdrawalStatus
import com.example.ui.theme.CrimsonRed
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldYellow
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate600
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import java.util.Locale

@Composable
fun WalletScreen(
  balance: Double,
  withdrawals: List<WithdrawalItem>,
  onRequestWithdrawal: (amount: String, method: String, accountNo: String, accountTitle: String) -> Unit,
  modifier: Modifier = Modifier
) {
  var amountText by remember { mutableStateOf("") }
  var selectedMethod by remember { mutableStateOf("ایزی پیسہ") }
  var accountNumber by remember { mutableStateOf("") }
  var accountTitle by remember { mutableStateOf("") }
  var isDropdownExpanded by remember { mutableStateOf(false) }
  var validationError by remember { mutableStateOf<String?>(null) }

  val methodOptions = listOf("ایزی پیسہ", "جاز کیش", "بینک ٹرانسفر")

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp)
      .testTag("wallet_screen"),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Withdrawal Form Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp)
          .testTag("withdrawal_form_card"),
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null, tint = CrimsonRed, modifier = Modifier.size(22.dp))
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "رقم نکلوانا (Withdrawal)",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  color = Color.White,
                  fontSize = 17.sp
                )
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Available Balance Display
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(Slate700)
              .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "دستیاب بیلنس:",
              color = Slate400,
              fontSize = 14.sp
            )
            Text(
              text = "Rs. ${String.format(Locale.US, "%.2f", balance)}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = EmeraldGreen,
                fontSize = 18.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Amount Label & Input
          Text(
            text = "رقم (PKR):",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it; validationError = null },
            placeholder = { Text("کم از کم 500 روپے") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("withdraw_amount_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SkyBlue,
              unfocusedBorderColor = Slate600,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedPlaceholderColor = Slate400,
              unfocusedPlaceholderColor = Slate400
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Account Method Label & Selector
          Text(
            text = "اکاؤنٹ کا طریقہ:",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(4.dp))

          Box(modifier = Modifier.fillMaxWidth()) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, Slate600, RoundedCornerShape(4.dp))
                .background(Slate700)
                .clickable { isDropdownExpanded = true }
                .padding(horizontal = 14.dp, vertical = 14.dp)
                .testTag("method_dropdown"),
              contentAlignment = Alignment.CenterStart
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(selectedMethod, color = Color.White, fontSize = 14.sp)
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null, tint = Slate400)
              }
            }

            DropdownMenu(
              expanded = isDropdownExpanded,
              onDismissRequest = { isDropdownExpanded = false },
              modifier = Modifier
                .background(Slate800)
                .border(1.dp, Slate600)
            ) {
              methodOptions.forEach { method ->
                DropdownMenuItem(
                  text = { Text(method, color = Color.White) },
                  onClick = {
                    selectedMethod = method
                    isDropdownExpanded = false
                  }
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Account Number Label & Input
          Text(
            text = "اکاؤنٹ نمبر:",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = accountNumber,
            onValueChange = { accountNumber = it; validationError = null },
            placeholder = { Text("03XXXXXXXXX") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("withdraw_account_input"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SkyBlue,
              unfocusedBorderColor = Slate600,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedPlaceholderColor = Slate400,
              unfocusedPlaceholderColor = Slate400
            )
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Account Holder Name
          Text(
            text = "اکاؤنٹ ہولڈر کا نام (اختیاری):",
            color = Slate400,
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(4.dp))
          OutlinedTextField(
            value = accountTitle,
            onValueChange = { accountTitle = it },
            placeholder = { Text("Account Holder Name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = SkyBlue,
              unfocusedBorderColor = Slate600,
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,
              focusedPlaceholderColor = Slate400,
              unfocusedPlaceholderColor = Slate400
            )
          )

          if (validationError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = validationError!!,
              color = CrimsonRed,
              fontSize = 12.sp
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          // Crimson Red Button: "ودڈرا کی درخواست بھیجیں" from HTML
          Button(
            onClick = {
              if (amountText.isBlank()) {
                validationError = "براہ کرم رقم درج کریں (کم از کم 500 روپے)"
              } else if (accountNumber.isBlank()) {
                validationError = "براہ کرم اکاؤنٹ نمبر درج کریں"
              } else {
                onRequestWithdrawal(amountText, selectedMethod, accountNumber, accountTitle)
                amountText = ""
                accountNumber = ""
                accountTitle = ""
              }
            },
            colors = ButtonDefaults.buttonColors(
              containerColor = CrimsonRed,
              contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
              .fillMaxWidth()
              .height(48.dp)
              .testTag("submit_withdraw_btn")
          ) {
            Icon(Icons.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "ودڈرا کی درخواست بھیجیں",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp
            )
          }
        }
      }
    }

    // 2. Withdrawal History Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .testTag("withdraw_history_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Slate800),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(Icons.Filled.History, contentDescription = null, tint = SkyBlue, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "ودڈرا ہسٹری",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 16.sp
              )
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Table Header
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(Slate700)
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text("رقم", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1f))
            Text("طریقہ", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 13.sp, modifier = Modifier.weight(1.2f))
            Text("اسٹیٹس", color = Slate400, fontWeight = FontWeight.Bold, fontSize = 13.sp, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
          }

          Spacer(modifier = Modifier.height(4.dp))

          // Table Rows
          if (withdrawals.isEmpty()) {
            Text(
              text = "کوئی ٹرانزیکشن موجود نہیں",
              color = Slate400,
              fontSize = 13.sp,
              modifier = Modifier.padding(16.dp),
              textAlign = TextAlign.Center
            )
          } else {
            withdrawals.forEach { item ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 10.dp, vertical = 10.dp)
                  .border(0.5.dp, Slate700),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "Rs. ${item.amount.toInt()}",
                  color = Color.White,
                  fontWeight = FontWeight.SemiBold,
                  fontSize = 13.sp,
                  modifier = Modifier.weight(1f)
                )

                Text(
                  text = item.method,
                  color = Slate400,
                  fontSize = 12.sp,
                  modifier = Modifier.weight(1.2f)
                )

                val statusColor = when (item.status) {
                  WithdrawalStatus.APPROVED -> EmeraldGreen
                  WithdrawalStatus.PENDING -> GoldYellow
                  WithdrawalStatus.REJECTED -> CrimsonRed
                }

                Text(
                  text = "${item.status.urduText} (${item.status.englishText})",
                  color = statusColor,
                  fontWeight = FontWeight.Bold,
                  fontSize = 12.sp,
                  textAlign = TextAlign.End,
                  modifier = Modifier.weight(1f)
                )
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

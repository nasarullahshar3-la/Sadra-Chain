package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ActivePlan
import com.example.data.DepositItem
import com.example.data.InvestmentPlan
import com.example.data.PlansRepository
import com.example.data.SadraNavTab
import com.example.data.WithdrawalItem
import com.example.data.WithdrawalStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SadraChainViewModel : ViewModel() {

  private val _currentTab = MutableStateFlow(SadraNavTab.DASHBOARD)
  val currentTab: StateFlow<SadraNavTab> = _currentTab.asStateFlow()

  private val _liveBalance = MutableStateFlow(250.0000)
  val liveBalance: StateFlow<Double> = _liveBalance.asStateFlow()

  private val _countdownSeconds = MutableStateFlow(24 * 3600 - 1) // 23:59:59
  val countdownSeconds: StateFlow<Int> = _countdownSeconds.asStateFlow()

  private val _activePlans = MutableStateFlow<List<ActivePlan>>(emptyList())
  val activePlans: StateFlow<List<ActivePlan>> = _activePlans.asStateFlow()

  private val _withdrawalList = MutableStateFlow(PlansRepository.initialWithdrawals)
  val withdrawalList: StateFlow<List<WithdrawalItem>> = _withdrawalList.asStateFlow()

  private val _depositList = MutableStateFlow<List<DepositItem>>(emptyList())
  val depositList: StateFlow<List<DepositItem>> = _depositList.asStateFlow()

  private val _teamMembers = MutableStateFlow(0)
  val teamMembers: StateFlow<Int> = _teamMembers.asStateFlow()

  private val _referralCommission = MutableStateFlow(0.0)
  val referralCommission: StateFlow<Double> = _referralCommission.asStateFlow()

  private val _userMessage = MutableSharedFlow<String>()
  val userMessage: SharedFlow<String> = _userMessage.asSharedFlow()

  val referralCode = "ref12345"
  val referralLink = "https://t.me/sadrachain_bot?start=ref12345"

  init {
    startLiveTickerAndTimer()
  }

  fun selectTab(tab: SadraNavTab) {
    _currentTab.value = tab
  }

  private fun startLiveTickerAndTimer() {
    viewModelScope.launch {
      while (isActive) {
        delay(1000)

        // Calculate earning increment per second
        var additionalPerSecond = 0.0005
        val active = _activePlans.value
        if (active.isNotEmpty()) {
          val totalDaily = active.sumOf { it.dailyProfit }
          additionalPerSecond += (totalDaily / 86400.0)
        }

        _liveBalance.value += additionalPerSecond

        // Decrement timer
        if (_countdownSeconds.value > 0) {
          _countdownSeconds.value -= 1
        } else {
          _countdownSeconds.value = 24 * 3600 // auto-reset after 24h
        }
      }
    }
  }

  fun formatTimer(): String {
    val totalSec = _countdownSeconds.value
    val hours = totalSec / 3600
    val minutes = (totalSec % 3600) / 60
    val seconds = totalSec % 60
    return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
  }

  fun buyPlan(plan: InvestmentPlan, onSuccess: () -> Unit, onError: (String) -> Unit) {
    val current = _liveBalance.value
    if (current < plan.price) {
      val needed = plan.price - current
      val errorMsg = "ناکافی بیلنس! مطلوبہ بیلنس: Rs. ${plan.price.toInt()} (مزید ڈپازٹ کریں Rs. ${String.format(Locale.US, "%.2f", needed)})"
      viewModelScope.launch {
        _userMessage.emit(errorMsg)
      }
      onError(errorMsg)
      return
    }

    // Deduct price and add active plan
    _liveBalance.value = current - plan.price
    val newActive = ActivePlan(
      planId = plan.id,
      name = "${plan.urduName} (${plan.englishName})",
      price = plan.price,
      dailyProfit = plan.dailyProfit
    )
    _activePlans.value = _activePlans.value + newActive

    val successMsg = "مبارک ہو! آپ کا پلان '${plan.urduName}' کامیابی سے ایکٹو ہو گیا ہے (ڈیلی منافع: Rs. ${plan.dailyProfit.toInt()})"
    viewModelScope.launch {
      _userMessage.emit(successMsg)
    }
    onSuccess()
  }

  fun requestWithdrawal(
    amountText: String,
    method: String,
    accountNumber: String,
    accountTitle: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    val amount = amountText.toDoubleOrNull()
    if (amount == null || amount < 500) {
      val err = "کم از کم ودڈرا رقم 500 روپے ہے (Minimum Rs. 500)"
      viewModelScope.launch { _userMessage.emit(err) }
      onError(err)
      return
    }

    if (accountNumber.trim().length < 10) {
      val err = "براہ کرم درست اکاؤنٹ نمبر درج کریں (مثال: 03XXXXXXXXX)"
      viewModelScope.launch { _userMessage.emit(err) }
      onError(err)
      return
    }

    if (amount > _liveBalance.value) {
      val err = "آپ کا دستیاب بیلنس کم ہے! (دستیاب: Rs. ${String.format(Locale.US, "%.2f", _liveBalance.value)})"
      viewModelScope.launch { _userMessage.emit(err) }
      onError(err)
      return
    }

    // Deduct and add to pending withdrawals
    _liveBalance.value -= amount

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val newItem = WithdrawalItem(
      id = "W${System.currentTimeMillis().toString().takeLast(4)}",
      amount = amount,
      method = method,
      accountNumber = accountNumber.trim(),
      accountTitle = if (accountTitle.isBlank()) "Account Holder" else accountTitle.trim(),
      status = WithdrawalStatus.PENDING,
      formattedDate = dateFormat.format(Date())
    )

    _withdrawalList.value = listOf(newItem) + _withdrawalList.value
    val msg = "ودڈرا کی درخواست کامیابی سے موصول ہو گئی ہے! جلد تصدیق ہو جائے گی۔"
    viewModelScope.launch { _userMessage.emit(msg) }
    onSuccess()
  }

  fun submitDeposit(
    amountText: String,
    method: String,
    senderNumber: String,
    trxId: String,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
  ) {
    val amount = amountText.toDoubleOrNull()
    if (amount == null || amount < 250) {
      val err = "کم از کم ڈپازٹ رقم 250 روپے ہے (Minimum Rs. 250)"
      viewModelScope.launch { _userMessage.emit(err) }
      onError(err)
      return
    }

    if (trxId.trim().isBlank()) {
      val err = "براہ کرم ٹرانزیکشن ID (Trx ID / TID) درج کریں"
      viewModelScope.launch { _userMessage.emit(err) }
      onError(err)
      return
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val deposit = DepositItem(
      id = "D${System.currentTimeMillis().toString().takeLast(4)}",
      amount = amount,
      method = method,
      trxId = trxId.trim(),
      senderAccount = senderNumber.trim(),
      formattedDate = dateFormat.format(Date()),
      isApproved = true
    )

    _depositList.value = listOf(deposit) + _depositList.value
    _liveBalance.value += amount

    val msg = "ڈپازٹ کامیابی سے ایڈ ہو گیا! Rs. ${amount.toInt()} بیلنس میں جمع کر دیے گئے ہیں۔"
    viewModelScope.launch { _userMessage.emit(msg) }
    onSuccess()
  }

  fun simulateNewReferral() {
    _teamMembers.value += 1
    val commissionBonus = 50.0
    _referralCommission.value += commissionBonus
    _liveBalance.value += commissionBonus
    viewModelScope.launch {
      _userMessage.emit("نئے ممبر نے ریفرل سے جوائن کیا! آپ کو Rs. ${commissionBonus.toInt()} کمیشن مل گیا!")
    }
  }

  fun claimDailyProfit() {
    val bonus = if (_activePlans.value.isNotEmpty()) {
      _activePlans.value.sumOf { it.dailyProfit }
    } else {
      15.0 // Base starter claim
    }

    _liveBalance.value += bonus
    _countdownSeconds.value = 24 * 3600
    viewModelScope.launch {
      _userMessage.emit("مبارک ہو! ڈیلی پرافٹ Rs. ${bonus.toInt()} کامیابی سے بیلنس میں شامل کر دیا گیا ہے!")
    }
  }
}

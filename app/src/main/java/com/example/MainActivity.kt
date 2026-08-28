package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.InvestmentPlan
import com.example.data.SadraNavTab
import com.example.ui.components.DepositDialog
import com.example.ui.components.SadraBottomBar
import com.example.ui.components.SadraSegmentedTabs
import com.example.ui.components.SadraTopBar
import com.example.ui.components.SplashScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.PlansScreen
import com.example.ui.screens.TeamScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Slate900
import com.example.viewmodel.SadraChainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

  private val viewModel: SadraChainViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        SadraChainApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun SadraChainApp(viewModel: SadraChainViewModel) {
  var showSplash by remember { mutableStateOf(true) }
  var showGlobalDepositDialog by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val clipboardManager = LocalClipboardManager.current
  val snackbarHostState = remember { SnackbarHostState() }
  val scope = rememberCoroutineScope()

  val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
  val balance by viewModel.liveBalance.collectAsStateWithLifecycle()
  val timerSeconds by viewModel.countdownSeconds.collectAsStateWithLifecycle()
  val activePlans by viewModel.activePlans.collectAsStateWithLifecycle()
  val withdrawalList by viewModel.withdrawalList.collectAsStateWithLifecycle()
  val teamMembers by viewModel.teamMembers.collectAsStateWithLifecycle()
  val referralCommission by viewModel.referralCommission.collectAsStateWithLifecycle()

  LaunchedEffect(Unit) {
    viewModel.userMessage.collectLatest { message ->
      snackbarHostState.showSnackbar(message)
    }
  }

  fun copyToClipboard(text: String, label: String = "کاپی ہو گیا!") {
    clipboardManager.setText(AnnotatedString(text))
    scope.launch {
      snackbarHostState.showSnackbar("$label: $text")
    }
    Toast.makeText(context, "$label: $text", Toast.LENGTH_SHORT).show()
  }

  Crossfade(targetState = showSplash, label = "splash_fade") { isSplash ->
    if (isSplash) {
      SplashScreen(onDismiss = { showSplash = false })
    } else {
      CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .background(Slate900),
          containerColor = Slate900,
          snackbarHost = { SnackbarHost(snackbarHostState) },
          topBar = {
            Column {
              SadraTopBar(
                currentTab = currentTab,
                onDepositClick = { showGlobalDepositDialog = true }
              )
              SadraSegmentedTabs(
                currentTab = currentTab,
                onTabSelected = { viewModel.selectTab(it) }
              )
            }
          },
          bottomBar = {
            SadraBottomBar(
              currentTab = currentTab,
              onTabSelected = { viewModel.selectTab(it) }
            )
          }
        ) { innerPadding ->
          Box(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            AnimatedContent(
              targetState = currentTab,
              transitionSpec = { fadeIn() togetherWith fadeOut() },
              label = "tab_content"
            ) { targetTab ->
              when (targetTab) {
                SadraNavTab.DASHBOARD -> {
                  DashboardScreen(
                    balance = balance,
                    timerFormatted = viewModel.formatTimer(),
                    timerSeconds = timerSeconds,
                    activePlans = activePlans,
                    onClaimDaily = { viewModel.claimDailyProfit() },
                    onNavigateToTab = { viewModel.selectTab(it) }
                  )
                }

                SadraNavTab.PLANS -> {
                  PlansScreen(
                    balance = balance,
                    onBuyPlan = { plan: InvestmentPlan ->
                      viewModel.buyPlan(
                        plan = plan,
                        onSuccess = { viewModel.selectTab(SadraNavTab.DASHBOARD) },
                        onError = {}
                      )
                    },
                    onSubmitDeposit = { amount, method, senderNumber, trxId ->
                      viewModel.submitDeposit(
                        amountText = amount,
                        method = method,
                        senderNumber = senderNumber,
                        trxId = trxId,
                        onSuccess = {},
                        onError = {}
                      )
                    },
                    onCopyText = { copyToClipboard(it, "نمبر کاپی ہو گیا") }
                  )
                }

                SadraNavTab.TEAM -> {
                  TeamScreen(
                    referralLink = viewModel.referralLink,
                    referralCode = viewModel.referralCode,
                    teamMembers = teamMembers,
                    commission = referralCommission,
                    onCopyLink = { copyToClipboard(it, "ریفرل لنک کاپی ہو گیا") },
                    onSimulateReferral = { viewModel.simulateNewReferral() }
                  )
                }

                SadraNavTab.WALLET -> {
                  WalletScreen(
                    balance = balance,
                    withdrawals = withdrawalList,
                    onRequestWithdrawal = { amount, method, accountNo, accountTitle ->
                      viewModel.requestWithdrawal(
                        amountText = amount,
                        method = method,
                        accountNumber = accountNo,
                        accountTitle = accountTitle,
                        onSuccess = {},
                        onError = {}
                      )
                    }
                  )
                }
              }
            }
          }

          if (showGlobalDepositDialog) {
            DepositDialog(
              onDismiss = { showGlobalDepositDialog = false },
              onSubmitDeposit = { amount, method, senderNumber, trxId ->
                viewModel.submitDeposit(
                  amountText = amount,
                  method = method,
                  senderNumber = senderNumber,
                  trxId = trxId,
                  onSuccess = {},
                  onError = {}
                )
              },
              onCopyText = { copyToClipboard(it, "نمبر کاپی ہو گیا") }
            )
          }
        }
      }
    }
  }
}

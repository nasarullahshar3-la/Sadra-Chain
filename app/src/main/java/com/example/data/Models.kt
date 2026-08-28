package com.example.data

enum class SadraNavTab(val id: String, val urduLabel: String, val englishLabel: String) {
  DASHBOARD("dashboard", "ڈیش بورڈ", "Dashboard"),
  PLANS("plans", "پلانز", "Plans"),
  TEAM("team", "ٹیم / ریفرل", "Team / Referral"),
  WALLET("wallet", "ودڈرا", "Withdraw")
}

data class InvestmentPlan(
  val id: Int,
  val urduName: String,
  val englishName: String,
  val price: Double,
  val dailyProfit: Double,
  val durationDays: Int = 30,
  val iconEmoji: String = "💎"
) {
  val titleText: String
    get() = "$id. $urduName ($englishName)"

  val dailyReturnText: String
    get() = "Rs. ${price.toInt()} | ڈیلی: Rs. ${dailyProfit.toInt()}"

  val totalReturnText: String
    get() = "کل پرافٹ: Rs. ${(dailyProfit * durationDays).toInt()} (${durationDays} دن)"
}

enum class WithdrawalStatus(val urduText: String, val englishText: String) {
  APPROVED("منظور", "Approved"),
  PENDING("التوا", "Pending"),
  REJECTED("مسترد", "Rejected")
}

data class WithdrawalItem(
  val id: String,
  val amount: Double,
  val method: String,
  val accountNumber: String,
  val accountTitle: String,
  val status: WithdrawalStatus,
  val formattedDate: String
)

data class DepositItem(
  val id: String,
  val amount: Double,
  val method: String,
  val trxId: String,
  val senderAccount: String,
  val formattedDate: String,
  val isApproved: Boolean = true
)

data class ActivePlan(
  val planId: Int,
  val name: String,
  val price: Double,
  val dailyProfit: Double,
  val activatedAt: Long = System.currentTimeMillis()
)

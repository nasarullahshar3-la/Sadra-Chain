package com.example.data

object PlansRepository {
  val officialEasypaisa = "03441830080"
  val officialJazzCash = "03288266019"
  val officialEasypaisaTitle = "Sadra Chain Official Deposit"
  val officialJazzCashTitle = "Sadra Chain Official Deposit"

  val allPlans: List<InvestmentPlan> = listOf(
    InvestmentPlan(1, "مائکرو", "Micro", 250.0, 15.0, iconEmoji = "🌱"),
    InvestmentPlan(2, "منی", "Mini", 500.0, 30.0, iconEmoji = "⚡"),
    InvestmentPlan(3, "اسٹارٹر", "Starter", 1000.0, 65.0, iconEmoji = "🚀"),
    InvestmentPlan(4, "بیسک", "Basic", 2500.0, 150.0, iconEmoji = "⭐"),
    InvestmentPlan(5, "برانز", "Bronze", 5000.0, 310.0, iconEmoji = "🥉"),
    InvestmentPlan(6, "سلور", "Silver", 8000.0, 500.0, iconEmoji = "🥈"),
    InvestmentPlan(7, "گولڈ", "Gold", 12000.0, 780.0, iconEmoji = "🥇"),
    InvestmentPlan(8, "پلاٹینم", "Platinum", 15000.0, 980.0, iconEmoji = "👑"),
    InvestmentPlan(9, "ایلیٹ", "Elite", 18000.0, 1200.0, iconEmoji = "🔥"),
    InvestmentPlan(10, "الٹیمیٹ", "Ultimate", 22000.0, 1500.0, iconEmoji = "💎")
  )

  val initialWithdrawals: List<WithdrawalItem> = listOf(
    WithdrawalItem(
      id = "W1001",
      amount = 1000.0,
      method = "ایزی پیسہ (Easypaisa)",
      accountNumber = "03001234567",
      accountTitle = "User Account",
      status = WithdrawalStatus.APPROVED,
      formattedDate = "2026-08-25 14:30"
    ),
    WithdrawalItem(
      id = "W1002",
      amount = 500.0,
      method = "جاز کیش (JazzCash)",
      accountNumber = "03219876543",
      accountTitle = "User Account",
      status = WithdrawalStatus.PENDING,
      formattedDate = "2026-08-27 10:15"
    )
  )
}

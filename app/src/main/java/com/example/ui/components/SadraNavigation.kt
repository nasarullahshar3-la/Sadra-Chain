package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SadraNavTab
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.SkyBlue
import com.example.ui.theme.Slate400
import com.example.ui.theme.Slate700
import com.example.ui.theme.Slate800
import com.example.ui.theme.Slate900

@Composable
fun SadraTopBar(
  currentTab: SadraNavTab,
  onDepositClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
      .testTag("top_bar"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Slate800),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      // App Branding
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .border(2.dp, AmberAccent, CircleShape)
            .background(Slate900),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.sadra_chain_logo),
            contentDescription = "Sadra Logo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
          Text(
            text = "صدرہ چین",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold,
              color = AmberAccent,
              fontSize = 18.sp
            )
          )
          Text(
            text = "Sadra Chain Official",
            style = MaterialTheme.typography.bodySmall.copy(
              color = Slate400,
              fontSize = 11.sp
            )
          )
        }
      }

      // Quick Deposit / Add Funds Action Button
      Button(
        onClick = onDepositClick,
        colors = ButtonDefaults.buttonColors(
          containerColor = EmeraldGreen,
          contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
          .height(38.dp)
          .testTag("quick_deposit_btn")
      ) {
        Text(
          text = "+ ڈپازٹ",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp
        )
      }
    }
  }
}

@Composable
fun SadraSegmentedTabs(
  currentTab: SadraNavTab,
  onTabSelected: (SadraNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 4.dp)
      .testTag("segmented_nav_bar"),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Slate800),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      SadraNavTab.entries.forEach { tab ->
        val isSelected = currentTab == tab
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Slate700 else Color.Transparent)
            .border(
              width = if (isSelected) 1.dp else 0.dp,
              color = if (isSelected) SkyBlue else Color.Transparent,
              shape = RoundedCornerShape(8.dp)
            )
            .testTag("tab_${tab.id}"),
          contentAlignment = Alignment.Center
        ) {
          Button(
            onClick = { onTabSelected(tab) },
            colors = ButtonDefaults.buttonColors(
              containerColor = Color.Transparent,
              contentColor = if (isSelected) SkyBlue else Slate400
            ),
            elevation = null,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = tab.urduLabel,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
              ),
              maxLines = 1
            )
          }
        }
      }
    }
  }
}

@Composable
fun SadraBottomBar(
  currentTab: SadraNavTab,
  onTabSelected: (SadraNavTab) -> Unit,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier
      .navigationBarsPadding()
      .testTag("bottom_nav"),
    containerColor = Slate800,
    tonalElevation = 8.dp
  ) {
    SadraNavTab.entries.forEach { tab ->
      val isSelected = currentTab == tab
      val (icon, selectedIcon) = when (tab) {
        SadraNavTab.DASHBOARD -> Pair(Icons.Outlined.Dashboard, Icons.Filled.Dashboard)
        SadraNavTab.PLANS -> Pair(Icons.Outlined.ShoppingBag, Icons.Filled.ShoppingBag)
        SadraNavTab.TEAM -> Pair(Icons.Outlined.Group, Icons.Filled.Group)
        SadraNavTab.WALLET -> Pair(Icons.Outlined.AccountBalanceWallet, Icons.Filled.AccountBalanceWallet)
      }

      NavigationBarItem(
        selected = isSelected,
        onClick = { onTabSelected(tab) },
        icon = {
          Icon(
            imageVector = if (isSelected) selectedIcon else icon,
            contentDescription = tab.englishLabel,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(
            text = tab.urduLabel,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            fontSize = 11.sp
          )
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = SkyBlue,
          selectedTextColor = SkyBlue,
          indicatorColor = Slate700,
          unselectedIconColor = Slate400,
          unselectedTextColor = Slate400
        ),
        modifier = Modifier.testTag("bottom_tab_${tab.id}")
      )
    }
  }
}

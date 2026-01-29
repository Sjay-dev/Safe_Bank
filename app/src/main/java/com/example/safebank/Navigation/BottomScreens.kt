package com.example.safebank.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomScreen (val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomScreen("home", "Home", Icons.Default.Home)
    object Settings : BottomScreen("settings", "Settings", Icons.Default.Settings)
}

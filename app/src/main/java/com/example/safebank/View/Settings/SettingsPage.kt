package com.example.safebank.View.Settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import com.example.safebank.ViewModel.ThemeMode
import com.example.safebank.ViewModel.ThemeViewModel

@Composable
fun SettingsScreen() {
    val themeViewModel: ThemeViewModel = hiltViewModel()
    val selectedTheme by themeViewModel.themeMode.collectAsState()
    var showAppearanceDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Appearance Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { showAppearanceDialog = true },
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Appearance",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        // Logout Button (Bottom)
        TextButton(
            onClick = { /* TODO Logout */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Text(
                text = "Logout",
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (showAppearanceDialog) {
        AppearanceDialog(
            selectedTheme = selectedTheme,
            onThemeSelected = { themeViewModel.setTheme(it); showAppearanceDialog = false },
            onDismiss = { showAppearanceDialog = false }
        )
    }

}


@Composable
fun AppearanceDialog(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Appearance")
        },
        text = {
            Column {

                ThemeOption("Follow system", ThemeMode.SYSTEM, selectedTheme, onThemeSelected)
                ThemeOption("Light mode", ThemeMode.LIGHT, selectedTheme, onThemeSelected)
                ThemeOption("Dark mode", ThemeMode.DARK, selectedTheme, onThemeSelected)
            }
        },
        confirmButton = {}
    )
}

@Composable
fun ThemeOption(
    title: String,
    mode: ThemeMode,
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onThemeSelected(mode) }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selectedTheme == mode,
            onClick = { onThemeSelected(mode) }
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(text = title)
    }
}

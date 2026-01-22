package com.example.safebank.ui.theme.DesignSystem

import androidx.compose.foundation.clickable
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Link(
    text : String ,
    onClick: () -> Unit ,
    modifier: Modifier = Modifier
){
   Text(
       text = text ,
       modifier = modifier.clickable{onClick() },
       style = MaterialTheme.typography.titleSmall ,
       color = MaterialTheme.colorScheme.primary,
       textAlign = TextAlign.Center
   )
}
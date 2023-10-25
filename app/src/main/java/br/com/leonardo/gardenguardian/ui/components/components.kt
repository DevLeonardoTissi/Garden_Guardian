package br.com.leonardo.gardenguardian.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MyAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = { TextButton(onClick = { onConfirmation() }) { Text(text = ("confirm")) } },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        icon = { Icon(icon, contentDescription = null) },
        dismissButton = { TextButton(onClick = { onDismissRequest }) { Text(text = "Dismiss") } }
    )
}
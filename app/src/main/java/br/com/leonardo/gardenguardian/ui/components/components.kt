package br.com.leonardo.gardenguardian.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.ui.DEFAULT_IMAGE_URL
import br.com.leonardo.gardenguardian.ui.theme.DarkGreen
import coil.compose.AsyncImage

@Composable
fun MyAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = { onConfirmation() }) { Text(text = ("confirm")) } },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        icon = { Icon(icon, contentDescription = null) },
        dismissButton = { TextButton(onClick = { onDismissRequest() }) { Text(text = "Dismiss") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    searchText: String?,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchText?: "",
        onValueChange = { newValue ->
            onSearchChange(newValue)
        },
        modifier,
        shape = RoundedCornerShape(100),
        leadingIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_grass), contentDescription = "ícone de lupa", tint = DarkGreen)
        },
        label = {
            Text(text = "Link da imagem")
        },
        placeholder = {
            Text("Link da imagem")
        })
}




@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: (newUrl:String?) -> Unit,
     url : String?,
    imageDescription: String,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                var newUrl by remember { mutableStateOf(url) }


                AsyncImage(
                    model = if (newUrl.isNullOrBlank()) DEFAULT_IMAGE_URL else newUrl,
                    contentDescription = imageDescription,
                    contentScale = ContentScale.Fit,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    modifier = Modifier
                        .height(160.dp)
                )

                Text(
                    text = "Cole o link da imagem da sua planta",
                    modifier = Modifier.padding(16.dp),
                )

                SearchTextField(
                    searchText = newUrl,
                    onSearchChange = {newUrl = it},
                    modifier = Modifier.height(80.dp).padding(10.dp)
                )



                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation(newUrl) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
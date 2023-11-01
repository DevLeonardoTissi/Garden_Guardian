package br.com.leonardo.gardenguardian.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
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
import br.com.leonardo.gardenguardian.ui.theme.md_theme_light_primary
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun MyAlertDialog(
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        confirmButton = { TextButton(onClick = { onConfirmation() }) { Text(text = ("confirm")) } },
        title = { Text(text = dialogTitle) },
        text = { Text(text = dialogText) },
        icon = { Icon(icon, contentDescription = null) },
        dismissButton = { TextButton(onClick = { onDismissRequest() }) { Text(text = "Dismiss") } },
    )
}


@Composable
fun tryConnectionDeviceAlertDialog() {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.bluetooth))
    Dialog(onDismissRequest = {}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(170.dp)
                )

                Text(
                    text = "Tentendo conectar ao dispositivo",
                    modifier = Modifier.padding(16.dp),
                )
            }
        }
    }
}

@Composable
fun AnimatedAlertDialogWithConfirmButton(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    rawRes: Int,
    text:String
) {

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(rawRes))
    Dialog(onDismissRequest = {onDismissRequest()}) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier.size(170.dp)
                )

                Text(
                    text = text,
                    modifier = Modifier.padding(16.dp),
                )


                TextButton(
                    onClick = { onConfirmation() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Ok")
                }
            }
        }
    }
}




@Composable
fun SearchTextField(
    searchText: String?,
    onSearchChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchText ?: "",
        onValueChange = { newValue ->
            onSearchChange(newValue)
        },
        modifier,
        shape = RoundedCornerShape(100),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_grass),
                contentDescription = "ícone de lupa",
                tint = md_theme_light_primary
            )
        },
        label = {
            Text(text = "Link da imagem")
        },
        placeholder = {
            Text("Link da imagem")
        })
}

@Composable
fun MyAsyncImage(
    model: String?,
    description: String?,
    modifier: Modifier,
    contentScale: ContentScale
) {
    AsyncImage(
        model = model, contentDescription = description, contentScale = contentScale,
        error = painterResource(id = R.drawable.error),
        placeholder = painterResource(id = R.drawable.placeholder), modifier = modifier,
    )
}


@Composable
fun DialogWithImage(
    onDismissRequest: () -> Unit,
    onConfirmation: (newUrl: String?) -> Unit,
    url: String?,
    imageDescription: String,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                var newUrl by remember { mutableStateOf(url) }

                if (!newUrl.isNullOrBlank()) {
                    MyAsyncImage(
                        model = newUrl,
                        description = imageDescription,
                        modifier = Modifier, contentScale = ContentScale.Fit
                    )
                }


                Text(
                    text = "Cole o link da imagem da sua planta",
                    modifier = Modifier.padding(16.dp),
                )

                SearchTextField(
                    searchText = newUrl,
                    onSearchChange = { newUrl = it },
                    modifier = Modifier
                        .height(80.dp)
                        .padding(10.dp)
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
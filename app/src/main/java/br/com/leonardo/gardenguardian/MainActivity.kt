package br.com.leonardo.gardenguardian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import br.com.leonardo.gardenguardian.ui.theme.DarkGreen
import br.com.leonardo.gardenguardian.ui.theme.GardenGuardianTheme
import coil.compose.AsyncImage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GardenGuardianTheme {
                Surface {
                    Adf()
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun Adf() {

    Surface(shape = RoundedCornerShape(15.dp), shadowElevation = 4.dp, modifier = Modifier.padding(16.dp)) {
        Column(modifier = Modifier.fillMaxWidth().heightIn(400.dp, 450.dp)) {

            Box(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(colors = listOf(DarkGreen, Color.White))
                    )
                    .fillMaxWidth()
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(y = 100.dp)
                        .clip(shape = CircleShape)
                        .align(Alignment.BottomCenter)
                        .border(
                            BorderStroke(
                                2.dp,
                                brush = Brush.verticalGradient(listOf(Color.White, DarkGreen))
                            ), CircleShape
                        ),
                    model = "https://img.freepik.com/fotos-gratis/planta-zz-em-um-vaso-cinza_53876-134285.jpg?w=740&t=st=1696877614~exp=1696878214~hmac=6df009433f3cecc5f802bf4e378b07d68e6374cdbdb12a65f54cb71c89508556",
                    contentDescription = null,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background),
                    contentScale = ContentScale.Crop

                )
            }


            Spacer(modifier = Modifier.height(100.dp))
            Text(
                text = LoremIpsum(50).values.first(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(all = 8.dp)
            )

        }
    }


}
package br.com.leonardo.gardenguardian.ui.screens.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import br.com.leonardo.gardenguardian.R
import br.com.leonardo.gardenguardian.ui.GITHUB_LINK
import br.com.leonardo.gardenguardian.ui.LINKEDIN_LINK
import br.com.leonardo.gardenguardian.utils.goToUri
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun AboutHomeScreen() {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(100.dp))


        val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.android))

        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(250.dp)
        )



        Text(
            text = context.getString(R.string.presentationTextAboutScreen),
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            val animationCompositionLinkedin by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(
                    R.raw.linkedin
                )
            )

            LottieAnimation(
                composition = animationCompositionLinkedin,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        goToUri(context = context, address = LINKEDIN_LINK)
                    }
            )

            val animationCompositionGithub by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(
                    R.raw.github
                )
            )

            LottieAnimation(
                composition = animationCompositionGithub,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        goToUri(context = context, address = GITHUB_LINK)
                    }
            )

        }


    }
}
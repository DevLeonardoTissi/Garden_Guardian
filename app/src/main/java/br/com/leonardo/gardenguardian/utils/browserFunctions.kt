package br.com.leonardo.gardenguardian.utils

import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import br.com.leonardo.gardenguardian.R

fun goToUri(address: String, context: Context) {
    val uri = Uri.parse(address)
    val customTabsIntent = CustomTabsIntent.Builder()
        .setDefaultColorSchemeParams(
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(context, R.color.primary))
                .build()
        )
        .setStartAnimations(context, R.anim.enter_from_botton, R.anim.exit_to_bottom)
        .setExitAnimations(context, R.anim.exit_to_bottom, R.anim.enter_from_botton)
        .setUrlBarHidingEnabled(true)
        .setShowTitle(true)
        .build()

    customTabsIntent.launchUrl(context, uri)
}

package br.com.leonardo.gardenguardian

import android.app.Application
import br.com.leonardo.gardenguardian.di.modules.databaseModule
import br.com.leonardo.gardenguardian.di.modules.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class GardenGuardianApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@GardenGuardianApplication)
            modules(
                databaseModule,
                viewModelModule
            )
        }
    }
}
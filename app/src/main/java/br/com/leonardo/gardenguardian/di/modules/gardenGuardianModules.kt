package br.com.leonardo.gardenguardian.di.modules

import android.app.NotificationManager
import android.content.Context
import androidx.room.Room
import br.com.leonardo.gardenguardian.database.AppDatabase
import br.com.leonardo.gardenguardian.database.MIGRATION_1_2
import br.com.leonardo.gardenguardian.notification.NotificationMainChannel
import br.com.leonardo.gardenguardian.repository.PlantRepository
import br.com.leonardo.gardenguardian.ui.screens.homeScreen.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

private const val DATABASE_NAME = "gardenGuardian.db"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).addMigrations(MIGRATION_1_2)
            .build()
    }

    single { get<AppDatabase>().plantDAO }
}

val viewModelModule = module {
    viewModel {
        HomeScreenViewModel(get())
    }
}

val repositoryModule = module {
    single{
        PlantRepository(get())
    }
}

val notificationModule = module {
    single { NotificationMainChannel(get(), get()) }
    single { get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

}
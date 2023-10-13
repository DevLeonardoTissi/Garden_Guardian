package br.com.leonardo.gardenguardian.di.modules

import androidx.room.Room
import br.com.leonardo.gardenguardian.database.AppDatabase
import org.koin.dsl.module

private const val DATABASE_NAME = "gardenGuardian.db"

val databaseModule = module {
    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().plantDAO }
}
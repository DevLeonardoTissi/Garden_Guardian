package br.com.leonardo.gardenguardian.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.leonardo.gardenguardian.database.dao.PlantDAO
import br.com.leonardo.gardenguardian.database.dao.SettingsDAO
import br.com.leonardo.gardenguardian.model.Plant
import br.com.leonardo.gardenguardian.model.Settings

@Database(entities = [Plant::class, Settings::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val plantDAO: PlantDAO
    abstract val settingsDAO : SettingsDAO

}
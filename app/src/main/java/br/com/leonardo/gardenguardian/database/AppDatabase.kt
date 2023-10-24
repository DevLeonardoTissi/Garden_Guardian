package br.com.leonardo.gardenguardian.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.leonardo.gardenguardian.database.dao.PlantDAO
import br.com.leonardo.gardenguardian.model.Plant

@Database(entities = [Plant::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val plantDAO: PlantDAO
}
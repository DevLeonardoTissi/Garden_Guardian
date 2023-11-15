package br.com.leonardo.gardenguardian.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.leonardo.gardenguardian.model.Settings
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(settings: Settings)

    @Query("SELECT * FROM SETTINGS")
    fun search(): Flow<Settings>

}
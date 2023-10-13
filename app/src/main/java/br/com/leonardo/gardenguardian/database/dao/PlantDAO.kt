package br.com.leonardo.gardenguardian.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import br.com.leonardo.gardenguardian.model.Plant

@Dao
interface PlantDAO {
    @Insert(onConflict = REPLACE)
    suspend fun insert(plant:Plant)
}
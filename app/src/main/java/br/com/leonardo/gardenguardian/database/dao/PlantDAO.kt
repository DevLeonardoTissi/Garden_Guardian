package br.com.leonardo.gardenguardian.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import br.com.leonardo.gardenguardian.model.Plant
import kotlinx.coroutines.flow.Flow


@Dao
interface PlantDAO {
    @Insert(onConflict = REPLACE)
     suspend fun insert(plant:Plant)

    @Query("SELECT * FROM PLANT")
    fun search(): Flow<Plant>

}
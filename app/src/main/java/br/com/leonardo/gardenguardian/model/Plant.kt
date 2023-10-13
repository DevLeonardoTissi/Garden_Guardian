package br.com.leonardo.gardenguardian.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Plant(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val img: String? = null
)
package br.com.leonardo.gardenguardian.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plant(
    @PrimaryKey
    val id: Long = 1,
    val img: String? = null
)
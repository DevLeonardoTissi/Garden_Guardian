package br.com.leonardo.gardenguardian.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey
    val id: Long = 1,
    val showNotification: Boolean = true
)
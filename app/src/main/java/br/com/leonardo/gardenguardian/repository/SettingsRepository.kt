package br.com.leonardo.gardenguardian.repository

import br.com.leonardo.gardenguardian.database.dao.SettingsDAO
import br.com.leonardo.gardenguardian.model.Settings

class SettingsRepository(private val dao: SettingsDAO) {

    suspend fun updateShowNotification(showNotification: Boolean) =
        dao.insert(Settings(showNotification = showNotification))

    fun search() = dao.search()
}
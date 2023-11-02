package br.com.leonardo.gardenguardian.repository

import br.com.leonardo.gardenguardian.database.dao.PlantDAO
import br.com.leonardo.gardenguardian.model.Plant

class PlantRepository(private val dao: PlantDAO) {
    suspend fun insertUrl(url: String?) = dao.insert(Plant(img = url))
    fun search() = dao.search()

}
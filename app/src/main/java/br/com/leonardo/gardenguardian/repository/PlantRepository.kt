package br.com.leonardo.gardenguardian.repository

import br.com.leonardo.gardenguardian.database.dao.PlantDAO
import br.com.leonardo.gardenguardian.model.Plant

class PlantRepository(private val dao:PlantDAO) {

    fun insert(plant: Plant) = dao.insert(plant)
    fun search() = dao.search()


}
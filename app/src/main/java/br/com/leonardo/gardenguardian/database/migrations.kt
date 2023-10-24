package br.com.leonardo.gardenguardian.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("INSERT INTO plant (id, img) VALUES (1, '')")

    }

}
package br.com.leonardo.gardenguardian.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

class DatabaseCallback : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        db.execSQL("INSERT INTO `Plant` (`id`, `img`) VALUES (1, NULL)")
        db.execSQL("INSERT INTO `Settings` (`id`, `showNotification`) VALUES (1, 1)")
    }
}
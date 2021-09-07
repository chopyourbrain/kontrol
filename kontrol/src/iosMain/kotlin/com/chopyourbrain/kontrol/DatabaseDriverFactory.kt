package com.chopyourbrain.kontrol

import com.chopyourbrain.kontrol.database.AppDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, "ktor_network.db")
    }
}

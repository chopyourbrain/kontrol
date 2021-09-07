package com.chopyourbrain.kontrol

import android.content.Context
import com.chopyourbrain.kontrol.database.AppDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory(private val context: Context) {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(AppDatabase.Schema, context, "ktor_network.db")
    }
}

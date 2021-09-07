package com.chopyourbrain.sample.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.chopyourbrain.sample.DebugMenuRouter
import com.russhwolf.settings.AndroidSettings
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Napier.base(DebugAntilog())
        val androidSettings = AndroidSettings.Factory(this).create()
        findViewById<AppCompatButton>(R.id.button_default_menu).setOnClickListener {
            DebugMenuRouter.routeToDebugMenu(androidSettings)
        }
    }
}

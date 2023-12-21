package io.chopyourbrain.sample.android

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.russhwolf.settings.SharedPreferencesSettings
import io.chopyourbrain.kontrol.android.R
import io.chopyourbrain.sample.DebugMenuRouter
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Napier.base(DebugAntilog())
        val delegate = getPreferences(Context.MODE_PRIVATE)
        val androidSettings = SharedPreferencesSettings(delegate)
        findViewById<AppCompatButton>(R.id.button_default_menu).setOnClickListener {
            DebugMenuRouter.routeToDebugMenu(androidSettings)
        }
    }
}

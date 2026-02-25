package io.chopyourbrain.sample.android

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.russhwolf.settings.SharedPreferencesSettings
import io.chopyourbrain.sample.DebugMenuRouter
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val rootView = findViewById<View>(R.id.main_view)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
        Napier.base(DebugAntilog())
        val delegate = getPreferences(MODE_PRIVATE)
        val androidSettings = SharedPreferencesSettings(delegate)
        findViewById<AppCompatButton>(R.id.button_default_menu).setOnClickListener {
            DebugMenuRouter.routeToDebugMenu(androidSettings)
        }
    }
}

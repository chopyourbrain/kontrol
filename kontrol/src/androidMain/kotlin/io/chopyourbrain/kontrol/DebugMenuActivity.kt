package io.chopyourbrain.kontrol

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.Menu
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.chopyourbrain.kontrol.android.R
import io.chopyourbrain.kontrol.ktor.NetCall
import io.chopyourbrain.kontrol.network.NetworkFragment
import io.chopyourbrain.kontrol.network.detail.NetworkDetailFragment
import io.chopyourbrain.kontrol.properties.PropertiesFragment

internal class DebugMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kntrl_activity_debug)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        supportActionBar?.title = "Debug menu"

        val container = findViewById<FrameLayout>(R.id.kntrl_container)
        val bottomNav = findViewById<BottomNavigationView>(R.id.kntrl_bottom_nav)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            container.setPadding(0, systemBars.top, 0, 0)
            bottomNav.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }
        bottomNav.menu.add(Menu.NONE, 1, Menu.NONE, "Properties").setIcon(R.drawable.kntrl_ic_properties).itemId
        bottomNav.menu.add(Menu.NONE, 2, Menu.NONE, "Network").setIcon(R.drawable.kntrl_ic_network)
        bottomNav.setOnItemSelectedListener {
            val currentFragment = when (it.itemId) {
                1 -> PropertiesFragment.create()
                2 -> NetworkFragment.create()
                else -> null
            }
            currentFragment?.let { fragment ->
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction().replace(R.id.kntrl_container, fragment).commit()
            }
            return@setOnItemSelectedListener true
        }
        if (supportFragmentManager.fragments.isEmpty())
            supportFragmentManager.beginTransaction().replace(R.id.kntrl_container, PropertiesFragment.create()).commit()
    }

    fun goToNetworkDetail(netCall: NetCall) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.kntrl_container, NetworkDetailFragment.create(netCall.id))
            .addToBackStack("NetworkDetailFragment")
            .commit()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DebugMenuActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
            })
        }
    }
}

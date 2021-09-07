package io.chopyourbrain.kontrol

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.chopyourbrain.kontrol.ktor.NetCall
import io.chopyourbrain.kontrol.network.NetworkFragment
import io.chopyourbrain.kontrol.network.detail.NetworkDetailFragment
import io.chopyourbrain.kontrol.properties.PropertiesFragment

internal class DebugMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)
        supportActionBar?.title = "Debug menu"

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.menu.add(Menu.NONE, 1, Menu.NONE, "Properties").setIcon(R.drawable.ic_properties).itemId
        bottomNav.menu.add(Menu.NONE, 2, Menu.NONE, "Network").setIcon(R.drawable.ic_network)
        bottomNav.setOnItemSelectedListener {
            val currentFragment = when (it.itemId) {
                1 -> PropertiesFragment.create()
                2 -> NetworkFragment.create()
                else -> null
            }
            currentFragment?.let { fragment ->
                supportFragmentManager.popBackStack()
                supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
            }
            return@setOnItemSelectedListener true
        }
        if (supportFragmentManager.fragments.isEmpty())
            supportFragmentManager.beginTransaction().replace(R.id.container, PropertiesFragment.create()).commit()
    }

    fun goToNetworkDetail(netCall: NetCall) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, NetworkDetailFragment.create(netCall.id))
            .addToBackStack("NetworkDetailFragment")
            .commit()
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, DebugMenuActivity::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK
            })
        }
    }
}

package com.chopyourbrain.kontrol

import com.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformController
import com.chopyourbrain.kontrol.repository.DBRepository
import kotlinx.atomicfu.atomic

internal object ServiceLocator {
    val MainDebugScreen = atomic(DebugScreen(null))
    val DBRepository = atomic<DBRepository?>(null)
    val DebugScreenPlatformController = atomic<DebugScreenPlatformController?>(null)
    val KVStorage = atomic<KVStorage?>(null)
}

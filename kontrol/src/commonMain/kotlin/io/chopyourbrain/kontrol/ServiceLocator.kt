package io.chopyourbrain.kontrol

import io.chopyourbrain.kontrol.repository.DBRepository
import kotlinx.atomicfu.atomic

internal object ServiceLocator {
    val MainDebugScreen = atomic(DebugScreen(null))
    val DBRepository = atomic<DBRepository?>(null)
    val DebugScreenPlatformController = atomic<io.chopyourbrain.kontrol.platform_controller.DebugScreenPlatformController?>(null)
    val KVStorage = atomic<KVStorage?>(null)
}

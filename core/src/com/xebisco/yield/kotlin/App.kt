package com.xebisco.yield.kotlin

import com.xebisco.yield.Application
import com.xebisco.yield.ApplicationManager
import com.xebisco.yield.ApplicationPlatform
import com.xebisco.yield.PlatformInit
import com.xebisco.yield.Scene
import kotlin.reflect.KClass

fun <T : Scene> application(
    applicationManager: ApplicationManager,
    initialScene: KClass<T>,
    applicationPlatform: ApplicationPlatform,
    platformInit: PlatformInit
): Application {
    return Application(applicationManager, initialScene.java, applicationPlatform, platformInit)
}
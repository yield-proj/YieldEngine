package com.xebisco.yield.kotlin

import com.xebisco.yield.*
import kotlin.reflect.KClass

fun <T : Scene> application(
    applicationManager: ApplicationManager,
    initialScene: KClass<T>,
    applicationPlatform: ApplicationPlatform,
    platformInit: PlatformInit
): Application {
    return Application(applicationManager, initialScene.java, applicationPlatform, platformInit)
}

fun <T : Scene> Application.changeScene(sceneClass: KClass<T>) {
    changeScene(sceneClass.java)
}

fun <T : Scene> Application.changeScene(sceneClass: KClass<T>, changeSceneTransition: ChangeSceneTransition) {
    changeScene(sceneClass.java, changeSceneTransition)
}
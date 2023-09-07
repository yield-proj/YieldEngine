package com.xebisco.yield.kotlin

import com.xebisco.yield.ComponentBehavior
import com.xebisco.yield.Entity2D
import kotlin.reflect.KClass

fun <T : ComponentBehavior> Entity2D.component(
    componentClass: KClass<T>
): T {
    return component(componentClass.java)
}

fun <T : ComponentBehavior> Entity2D.component(
    componentClass: KClass<T>,
    index: Int
): T {
    return component(componentClass.java, index)
}

fun <T : ComponentBehavior> Entity2D.removeComponent(
    componentClass: KClass<T>
) {
    removeComponent(componentClass.java)
}

fun <T : ComponentBehavior> Entity2D.removeComponent(
    componentClass: KClass<T>,
    index: Int
) {
    removeComponent(componentClass.java, index)
}
package com.xebisco.yield.kotlin

import com.xebisco.yield.ComponentBehavior
import com.xebisco.yield.ComponentCreation
import com.xebisco.yield.ComponentModifier
import kotlin.reflect.KClass

fun <T : ComponentBehavior> componentCreation(componentClass: KClass<T>): ComponentCreation {
    return ComponentCreation(componentClass.java)
}
fun <T : ComponentBehavior> componentCreation(componentClass: KClass<T>, componentModifier: ComponentModifier): ComponentCreation {
    return ComponentCreation(componentClass.java, componentModifier)
}
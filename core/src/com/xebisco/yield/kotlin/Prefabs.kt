package com.xebisco.yield.kotlin

import com.xebisco.yield.*
import kotlin.reflect.KClass

fun <T : ComponentBehavior> componentCreation(componentClass: KClass<T>): ComponentCreation {
    return ComponentCreation(componentClass.java)
}

fun <T : ComponentBehavior> componentCreation(
    componentClass: KClass<T>, componentModifier: ComponentModifier
): ComponentCreation {
    return ComponentCreation(componentClass.java, componentModifier)
}

fun <T : Rectangle> StandardPrefabs.rectangleShape(
    rectangleType: KClass<T>,
    size: Vector2D,
    color: Color,
    filled: Boolean,
    borderThickness: Double,
    tags: Array<String>
): Entity2DPrefab {
    return StandardPrefabs.rectangleShape(rectangleType.java, size, color, filled, borderThickness, tags)
}
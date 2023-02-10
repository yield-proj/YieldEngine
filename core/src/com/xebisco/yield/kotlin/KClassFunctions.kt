/*
 * Copyright [2022] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.kotlin

import com.xebisco.yield.*
import kotlin.reflect.KClass

/**
 * Get the file with the given name, and cast it to the given type.
 *
 * @param name The name of the file.
 * @param type The type of the file you want to get.
 * @return A file of the specified type.
 */
fun <T : RelativeFile> Assets.get(name: String, type: KClass<T>) {
    this.get(name, type.java)
}

/**
 * Search for all the YldSystem instances in this YldScene.
 *
 * @param system The class type of the system that's being searched.
 * @return The system found (null if not found)
 */
fun <S : YldSystem> YldScene.getSystem(system: KClass<S>): S? {
    return this.getSystem(system.java)
}

/**
 * Removes an Entity that corresponds to the given type from its parent and calls onDestroy().
 *
 * @param type The Entity to be destroyed type.
 */

fun <E : Prefab> YldScene.destroy(type: KClass<E>) {
    return this.destroy(type.java)
}

/**
 * Destroys a child of this Entity based on the given class type.
 *
 * @param type The type of the entity that will be destroyed.
 */
fun <E : Prefab> YldScript.destroy(type: KClass<E>) {
    return this.destroy(type.java)
}

/**
 * Search for all the Components instances in this Entity.
 *
 * @param type The class type of the component that's being searched.
 * @return The component found (null if not found)
 */
fun <T : Component> YldScript.getComponent(type: KClass<T>): T? {
    return this.getComponent(type.java)
}

/**
 * Check if any of this Entity children contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If contains the Component.
 */

fun <T : Component> YldScript.getComponentInChildren(type: KClass<T>): T? {
    return this.getComponentInChildren(type.java)
}

/**
 * Check if this Entity parent contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If the Entity parent contains the Component.
 */

fun <T : Component> YldScript.getComponentInParent(type: KClass<T>): T? {
    return this.getComponentInParent(type.java)
}

/**
 * Search for all the Components instances in this Entity.
 *
 * @param type The class type of the component that's being searched.
 * @param index The index of the component.
 * @return The component found (null if not found)
 */
fun <T : Component> YldScript.getComponent(type: KClass<T>, index: Int): T? {
    return this.getComponent(type.java, index)
}

/**
 * Check if this Entity contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If the Entity contains the Component.
 */

fun <T : Component> YldScript.containsComponent(type: KClass<T>): Boolean {
    return this.containsComponent(type.java)
}


/**
 * Search for all the Components instances in this Entity.
 *
 * @param type The class type of the component that's being searched.
 * @return The component found (null if not found)
 */
fun <T : Component> Component.getComponent(type: KClass<T>): T? {
    return this.getComponent(type.java)
}

/**
 * Search for all the Components instances in this Entity.
 *
 * @param type The class type of the component that's being searched.
 * @param index The index of the component.
 * @return The component found (null if not found)
 */
fun <T : Component> Component.getComponent(type: KClass<T>, index: Int): T? {
    return this.getComponent(type.java, index)
}


fun <T : Component> Component.getComponentList(type: KClass<T>): List<T> {
    return this.getComponentList(type.java)
}

fun <T : Component> Component.getComponents(type: KClass<T>): Array<out Component> {
    return this.getComponents(type.java)
}

/**
 * Check if any of this Entity children contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If contains the Component.
 */

fun <T : Component> Component.getComponentInChildren(type: KClass<T>): T? {
    return this.getComponentInChildren(type.java)
}

/**
 * Check if this Entity parent contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If the Entity parent contains the Component.
 */

fun <T : Component> Component.getComponentInParent(type: KClass<T>): T? {
    return this.getComponentInParent(type.java)
}

/**
 * Check if this Entity contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If the Entity contains the Component.
 */

fun <T : Component> Component.containsComponent(type: KClass<T>): Boolean {
    return this.containsComponent(type.java)
}

/**
 * Search for all the Components instances in this Entity.
 *
 * @param type The class type of the component that's being searched.
 * @return The component found (null if not found)
 */
fun <T : Component> Entity.getComponent(type: KClass<T>): T? {
    return this.getComponent(type.java)
}

/**
 * Search for all the Components instances in this Entity.
 *
 * @param type  The class type of the component that's being searched.
 * @param index the index of this component.
 * @return The component found (null if not found)
 */
fun <T : Component> Entity.getComponent(type: KClass<T>, index: Int): T? {
    return this.getComponent(type.java, index)
}

/**
 * This function returns a list of components of the specified type
 *
 * @param type The type of component you want to get.
 * @return A list of components of the specified type.
 */
fun <T : Component> Entity.getComponentList(type: KClass<T>): List<T> {
    return this.getComponentList(type.java)
}

fun <T : Component> Entity.getComponents(type: KClass<T>): Array<out Component> {
    return this.getComponents(type.java)
}

/**
 * Check if this Entity contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If the Entity contains the Component.
 */

fun <T : Component> Entity.containsComponent(type: KClass<T>): Boolean {
    return this.containsComponent(type.java)
}

/**
 * Check if this Entity parent contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If the Entity parent contains the Component.
 */
fun <T : Component> Entity.getComponentInParent(type: KClass<T>): T? {
    return this.getComponentInParent(type.java)
}

/**
 * Check if any of this Entity children contains a Component with the specified class type.
 *
 * @param type The class type of the Component.
 * @return If contains the Component.
 */
fun <T : Component> Entity.getComponentInChildren(type: KClass<T>): T? {
    return this.getComponentInChildren(type.java)
}

/**
 * Destroys a child of this Entity based on the given class type.
 *
 * @param type The type of the entity that will be destroyed.
 */
fun <E : Prefab> Entity.destroy(type: KClass<E>) {
    return this.destroy(type.java)
}

/**
 * Method to set a scene from the type specified as the actual scene.
 *
 * @param type The scene type.
 * @param how  What to do with last scene.
 */
fun <T : YldScene> YldGame.setScene(type: KClass<T>, how: ChangeScene) {
    this.setScene(type.java, how)
}

/**
 * If the hashcode and name of the class of the scene at index i is equal to the hashcode and name of the class of the
 * type parameter, return the scene at index i.
 *
 * @param type The class of the scene you want to get.
 * @return The scene that matches the type.
 */
fun <T : YldScene> YldGame.getScene(type: KClass<T>): YldScene? {
    return this.getScene(type.java)
}

/**
 * Set the scene to the given type, and use the given progress scene to load it.
 *
 * @param type          The scene to change to.
 * @param progressScene The scene that will be displayed while the assets are being loaded.
 * @param how           What to do with last scene.
 */
fun <T : YldScene, P : YldProgressScene> YldGame.setScene(type: KClass<T>, progressScene: KClass<P>, how: ChangeScene) {
    this.setScene(type.java, progressScene.java, how)
}

/**
 * Set the scene to the given type, using the given progress scene, and destroy the last scene.
 *
 * @param type The class of the scene you want to change to.
 * @param progressScene The class of the progress scene to be displayed while the scene is loading.
 */

fun <T : YldScene, P : YldProgressScene> YldGame.setScene(type: KClass<T>, progressScene: KClass<P>) {
    this.setScene(type.java, progressScene.java)
}

/**
 * Method to instantiate a scene and set it as the actual scene, destroying the last one.
 *
 * @param type The scene type to be instantiated.
 */
fun <T : YldScene> YldGame.setScene(type: KClass<T>) {
    return this.setScene(type.java)
}
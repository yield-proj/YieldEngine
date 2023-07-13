/*
 * Copyright [2022-2023] [Xebisco]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xebisco.yield.physics;

import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.collision.Manifold;
import org.jbox2d.dynamics.contacts.Contact;

/**
 * The ContactAdapter class is an abstract class that defines methods for handling contact events between colliders.
 */
public class ContactAdapter implements ContactListener {

    @Override
    public void onContactBegin(Collider collider, Collider colliding) {

    }

    @Override
    public void onContactEnd(Collider collider, Collider colliding) {

    }

    @Override
    public void preSolve(Contact var1, Manifold var2) {

    }

    @Override
    public void postSolve(Contact var1, ContactImpulse var2) {

    }
}

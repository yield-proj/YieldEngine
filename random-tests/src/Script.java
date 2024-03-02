/*
 * Copyright [2022-2024] [Xebisco]
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

import com.xebisco.yield.ComponentBehavior;
import com.xebisco.yield.ContextTime;
import com.xebisco.yield.Global;
import com.xebisco.yield.physics.PhysicsBody;

public class Script extends ComponentBehavior {

    private PhysicsBody pb;

    @Override
    public void onStart() {
        pb = component(PhysicsBody.class);
    }

    @Override
    public void onUpdate(ContextTime time) {
        pb.applyAngularImpulse(application().axis(Global.HORIZONTAL) * 100);
    }
}

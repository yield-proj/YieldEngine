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

package com.xebisco.yield.ux;

import com.xebisco.yield.Color;
import com.xebisco.yield.SampleGraphics;
import com.xebisco.yield.Vector2;

public class UXTitleText extends UXText {

    public UXTitleText(Boolean middle, String contents, Vector2 position, UXMain uxMain) {
        super(middle, contents, position, uxMain);
        setFont(uxMain.getPalette().font2);
    }
}

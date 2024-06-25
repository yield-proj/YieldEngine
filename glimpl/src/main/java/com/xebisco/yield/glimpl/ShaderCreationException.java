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

package com.xebisco.yield.glimpl;

public class ShaderCreationException extends Exception {
    private static final long serialVersionUID = -1074584547758151813L;

    public ShaderCreationException() {
    }

    public ShaderCreationException(String message) {
        super(message);
    }

    public ShaderCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShaderCreationException(Throwable cause) {
        super(cause);
    }

    public ShaderCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

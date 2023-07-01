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

package com.xebisco.yield.script.math;

import com.xebisco.yield.script.ReturnRunnable;
import com.xebisco.yield.script.obj.ImmutableSet;
import com.xebisco.yield.script.obj.ObjectValue;
import com.xebisco.yield.script.obj.StandardGet;

public class MathOperation {
    private final ReturnRunnable left, right;
    private final Operation operation;

    public MathOperation(ReturnRunnable left, ReturnRunnable right, Operation operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public Number result() {
        NumberHierarchy hierarchy = NumberHierarchy.INT;
        Number left = (Number) this.left.run().getValue(), right = (Number) this.right.run().getValue();
        if (left instanceof Double || right instanceof Double)
            hierarchy = NumberHierarchy.DOUBLE;
        else if (left instanceof Float && right instanceof Float)
            hierarchy = NumberHierarchy.FLOAT;
        else if (left instanceof Long && right instanceof Long)
            hierarchy = NumberHierarchy.LONG;
        else if (left instanceof Float || right instanceof Float)
            hierarchy = NumberHierarchy.FLOAT;
        else if (left instanceof Long || right instanceof Long)
            hierarchy = NumberHierarchy.LONG;
        switch (operation) {
            case SUM:
                switch (hierarchy) {
                    case DOUBLE:
                        return left.doubleValue() + right.doubleValue();
                    case FLOAT:
                        return left.floatValue() + right.floatValue();
                    case LONG:
                        return left.longValue() + right.longValue();
                    case INT:
                        return left.intValue() + right.intValue();
                }
            case SUBTRACT:
                switch (hierarchy) {
                    case DOUBLE:
                        return left.doubleValue() - right.doubleValue();
                    case FLOAT:
                        return left.floatValue() - right.floatValue();
                    case LONG:
                        return left.longValue() - right.longValue();
                    case INT:
                        return left.intValue() - right.intValue();
                }
            case MULTIPLY:
                switch (hierarchy) {
                    case DOUBLE:
                        return left.doubleValue() * right.doubleValue();
                    case FLOAT:
                        return left.floatValue() * right.floatValue();
                    case LONG:
                        return left.longValue() * right.longValue();
                    case INT:
                        return left.intValue() * right.intValue();
                }
            case DIVIDE:
                switch (hierarchy) {
                    case DOUBLE:
                        return left.doubleValue() / right.doubleValue();
                    case FLOAT:
                        return left.floatValue() / right.floatValue();
                    case LONG:
                        return left.longValue() / right.longValue();
                    case INT:
                        return left.intValue() / right.intValue();
                }
            case POWER:
                switch (hierarchy) {
                    case DOUBLE:
                        return Math.pow(left.doubleValue(), right.doubleValue());
                    case FLOAT:
                        return (float) Math.pow(left.floatValue(), right.floatValue());
                    case LONG:
                        return (long) Math.pow(left.longValue(), right.longValue());
                    case INT:
                        return (int) Math.pow(left.intValue(), right.intValue());
                }
        }
        throw new ArithmeticException();
    }
}

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


import com.xebisco.yield.concurrency.ASyncFunction;
import com.xebisco.yield.concurrency.IntegerRange;
import com.xebisco.yield.concurrency.ParallelForLoop;
import com.xebisco.yield.concurrency.TimedOutException;

public class ConcTest {
    public static void main(String[] args) throws TimedOutException, InterruptedException {
        ASyncFunction.aSync(() -> {
            try {
                ParallelForLoop.parallelFor(IntegerRange.range(0, 1000), i -> {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(i);
                    return null;
                }).aWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).aWait();
    }
}

/*
 * Copyright 2017-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.function;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class FunctionMapTest {

    @Test
    public void testMapTransform() {
        int inA = 1;
        int inB = 2;
        int outA = 2;
        int outB = 4;

        Map<String, Integer> inputMap = new HashMap<>();
        inputMap.put("a", inA);
        inputMap.put("b", inB);

        Function<Integer, Integer> functioner = mock(Function.class);
        given(functioner.apply(inA)).willReturn(outA);
        given(functioner.apply(inB)).willReturn(outB);

        FunctionMap<String, Integer, Integer> functionByKey = new FunctionMap<>();
        functionByKey.setFunction(functioner);

        Map<String, Integer> outputMap = functionByKey.apply(inputMap);

        assertEquals(outA, (int) outputMap.get("a"));
        assertEquals(outB, (int) outputMap.get("b"));
    }
}

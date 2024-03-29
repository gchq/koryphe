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

package uk.gov.gchq.koryphe.binaryoperator;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class BinaryOperatorMapTest {

    @Test
    public void testMapAggregation() {
        int inA = 1;
        int inB = 2;

        int noInputs = 3;

        Map<String, Integer>[] inputs = new HashMap[noInputs];
        for (int i = 0; i < noInputs; i++) {
            inputs[i] = new HashMap<>();
            inputs[i].put("a", inA);
            inputs[i].put("b", inB);
        }

        // create mock that adds input and state together
        BinaryOperator<Integer> aggregator = mock(BinaryOperator.class);
        for (int i = 0; i < noInputs; i++) {
            Integer expectedA = null;
            Integer expectedB = null;
            if (i > 0) {
                expectedA = i * inA;
                expectedB = i * inB;
            }
            given(aggregator.apply(expectedA, inA)).willReturn(inA + (expectedA == null ? 0 : expectedA));
            given(aggregator.apply(expectedB, inB)).willReturn(inB + (expectedB == null ? 0 : expectedB));
        }

        BinaryOperatorMap<String, Integer> mapBinaryOperator = new BinaryOperatorMap<>();
        mapBinaryOperator.setBinaryOperator(aggregator);

        Map<String, Integer> state = null;
        for (Map<String, Integer> input : inputs) {
            state = mapBinaryOperator.apply(state, input);
        }

        assertThat((int) state.get("a")).isEqualTo(noInputs * inA);
        assertThat((int) state.get("b")).isEqualTo(noInputs * inB);
    }
}

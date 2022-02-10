/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.adapted;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.function.FunctionChain;
import uk.gov.gchq.koryphe.impl.function.ToLong;
import uk.gov.gchq.koryphe.impl.function.ToString;
import uk.gov.gchq.koryphe.util.EqualityTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputAdaptedTest extends EqualityTest<InputAdapted> {

    @Override
    protected InputAdapted getInstance() {
        return new InputAdapted(new ToLong());
    }

    @Override
    protected Iterable<InputAdapted> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new InputAdapted(),
                new InputAdapted(new ToString()),
                new InputAdapted(new FunctionChain(new ToLong()))
        );
    }

    @Test
    public void shouldApplyInputAdapterToInput() {
        // Given
        Integer input = 5;

        // When
        InputAdapted<Object, Long> inputAdapted = new InputAdapted<>(new ToLong());
        Long output = inputAdapted.adaptInput(input);

        // Then
        assertEquals(Long.class, output.getClass());
        assertEquals(5L, output);
    }
}
/*
 * Copyright 2017-2020 Crown Copyright
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

package uk.gov.gchq.koryphe.tuple;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ArrayTupleTest {

    @Test
    public void testConstructorsSize() {
        // When
        final ArrayTuple tuple = new ArrayTuple(3);

        // Then
        assertThat(tuple)
                .containsOnlyNulls()
                .hasSize(3);
    }

    @Test
    public void testInitialArrayConstructors() {
        // Given
        final Object[] initialValues = new String[] {"a", "b", "c", "d", "e"};

        // When
        final ArrayTuple tuple = new ArrayTuple(initialValues);

        // Then
        assertThat(tuple).containsExactlyElementsOf(Arrays.asList(initialValues));
    }
}

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

import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ArrayTupleTest {

    @Test
    public void testConstructorsSize() {
        // Test size constructor
        // When
        final ArrayTuple tuple = new ArrayTuple(3);

        // Then
        for (Object value : tuple) {
            assertNull(value, "Found unexpected non-null value");
        }
        assertEquals(3, Iterables.size(tuple), "Found unexpected number of values");
    }

    @Test
    public void testInitialArrayConstructors() {
        // Test initial array constructor
        // Given
        final Object[] initialValues = new String[] {"a", "b", "c", "d", "e"};

        // When
        final ArrayTuple tuple = new ArrayTuple(initialValues);

        int i = 0;
        for (Object value : tuple) {
            assertEquals(initialValues[i], value, "Found unexpected tuple value");
            i++;
        }
        assertEquals(initialValues.length, Iterables.size(tuple), "Found unexpected number of values");
    }
}

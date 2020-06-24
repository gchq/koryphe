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

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapTupleTest {

    @Test
    public void testConstructorsWithEmptyTuple() {
        // When
        final MapTuple<String> tuple = new MapTuple<>();

        // Then
        assertEquals(0, Iterables.size(tuple), "Tuple should be empty.");
    }

    @Test
    public void testConstructorsWithHashMapTuple() {
        // Given
        final Map<String, Object> initialMap = new HashMap<>();
        initialMap.put("0", 0);
        initialMap.put("1", 1);
        initialMap.put("2", 2);

        // When
        final MapTuple<String> tuple = new MapTuple<>(initialMap);

        // Then
        for (Object value : tuple) {
            assertEquals(value, tuple.get("" + value), "Unexpected value at reference " + value);
        }
        assertEquals(3, Iterables.size(tuple), "Unexpected number of values");
    }
}

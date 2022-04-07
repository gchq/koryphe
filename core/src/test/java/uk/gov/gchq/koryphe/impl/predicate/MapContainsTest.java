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

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MapContainsTest extends PredicateTest<MapContains> {

    private static final String KEY1 = "key1";
    private static final String KEY2 = "key2";

    private final Map<String, Integer> map1 = new HashMap<>();

    @BeforeEach
    public void setup() {
        map1.put(KEY1, 1);
    }

    @Test
    public void shouldAcceptWhenKeyInMap() {
        // Given
        final MapContains filter = new MapContains(KEY1);

        // When / Then
        assertThat(filter).accepts(map1);
    }

    @Test
    public void shouldRejectWhenKeyNotPresent() {
        // Given
        final MapContains filter = new MapContains(KEY2);

        // When / Then
        assertThat(filter).rejects(map1);
    }

    @Test
    public void shouldRejectEmptyMaps() {
        // Given
        final MapContains filter = new MapContains(KEY1);

        // When / Then
        assertThat(filter).rejects(new HashMap<>());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MapContains filter = new MapContains(KEY1);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.MapContains\",%n" +
                "  \"key\" : \"key1\"%n" +
                "}"), json);

        // When 2
        final MapContains deserialisedFilter = JsonSerialiser.deserialise(json, MapContains.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getKey()).isEqualTo(KEY1);
    }

    @Override
    protected MapContains getInstance() {
        return new MapContains(KEY1);
    }

    @Override
    protected Iterable<MapContains> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new MapContains(),
                new MapContains(KEY2)
        );
    }
}

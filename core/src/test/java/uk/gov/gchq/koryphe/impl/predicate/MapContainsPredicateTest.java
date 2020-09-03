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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapContainsPredicateTest extends PredicateTest<MapContainsPredicate> {

    private static final IsEqual KEY_PREDICATE_1 = new IsEqual("key1");
    private static final Regex KEY_PREDICATE_2 = new Regex("key.*");
    private static final IsEqual KEY_PREDICATE_NOT_IN_MAP = new IsEqual("key2");

    private final Map<Object, Integer> map1 = new HashMap<>();

    @BeforeEach
    public void setup() {
        map1.put(KEY_PREDICATE_1.getControlValue(), 1);
    }

    @Test
    public void shouldAcceptWhenKeyEqualsInMap() {
        // Given
        final MapContainsPredicate filter = new MapContainsPredicate(KEY_PREDICATE_1);

        // When
        boolean accepted = filter.test(map1);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldAcceptWhenKeyRegexInMap() {
        // Given
        final MapContainsPredicate filter = new MapContainsPredicate(KEY_PREDICATE_2);

        // When
        boolean accepted = filter.test(map1);

        // Then
        assertTrue(accepted);
    }

    @Test
    public void shouldRejectWhenKeyNotPresent() {
        // Given
        final MapContainsPredicate filter = new MapContainsPredicate(KEY_PREDICATE_NOT_IN_MAP);

        // When
        boolean accepted = filter.test(map1);

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldRejectEmptyMaps() {
        // Given
        final MapContainsPredicate filter = new MapContainsPredicate(KEY_PREDICATE_1);

        // When
        boolean accepted = filter.test(new HashMap());

        // Then
        assertFalse(accepted);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MapContainsPredicate filter = new MapContainsPredicate(KEY_PREDICATE_1);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.MapContainsPredicate\"," +
                "\"keyPredicate\":{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsEqual\"," +
                "\"value\":\"key1\"" +
                "}" +
                "}", json);

        // When 2
        final MapContainsPredicate deserialisedFilter = JsonSerialiser.deserialise(json, MapContainsPredicate.class);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(KEY_PREDICATE_1, deserialisedFilter.getKeyPredicate());
    }

    @Override
    protected MapContainsPredicate getInstance() {
        return new MapContainsPredicate(KEY_PREDICATE_1);
    }

    @Override
    protected Iterable<MapContainsPredicate> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new MapContainsPredicate(),
                new MapContainsPredicate(new IsEqual("differentValue")),
                new MapContainsPredicate(KEY_PREDICATE_2)
        );
    }
}

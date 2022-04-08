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

package uk.gov.gchq.koryphe.predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

/**
 * Tests the MapFilter class can be used to filter {@code FreqMap} frequencies.
 */
public class PredicateMapTest extends PredicateTest<PredicateMap> {

    private static final String KEY1 = "key1";
    private static final Date DATE_KEY = new Date();
    private static final String VALUE1 = "value1";
    private static final String KEY2 = "key2";
    private Map<String, String> map;
    private Predicate<String> predicate;

    @BeforeEach
    public void setup() {
        predicate = mock(Predicate.class);

        map = mock(Map.class);
        given(map.containsKey(KEY1)).willReturn(true);
        given(map.get(KEY1)).willReturn(VALUE1);
    }

    @Test
    public void shouldAcceptWhenNotPredicateGiven() {
        // Given
        final PredicateMap<String> filter = new PredicateMap<>(KEY1, null);

        // When / Then
        assertThat(filter).accepts(map);
    }

    @Test
    public void shouldAcceptWhenPredicateAccepts() {
        // Given
        final PredicateMap<String> filter = new PredicateMap<>(KEY1, predicate);
        given(predicate.test(VALUE1)).willReturn(true);

        // When / Then
        assertThat(filter).accepts(map);
    }

    @Test
    public void shouldRejectWhenPredicateRejects() {
        // Given
        final PredicateMap<String> filter = new PredicateMap<>(KEY1, predicate);
        given(predicate.test(VALUE1)).willReturn(false);

        // When / Then
        assertThat(filter).rejects(map);
    }


    @Test
    public void shouldNotErrorWhenKeyIsNotPresentForPredicate() {
        // Given
        final PredicateMap<String> filter = new PredicateMap<>(KEY2, predicate);
        given(predicate.test(null)).willReturn(false);

        // When / Then
        assertThat(filter).rejects(map);
    }

    @Test
    public void shouldRejectNullMaps() {
        // Given
        final PredicateMap<String> filter = new PredicateMap<>(KEY1, predicate);

        // When / Then
        assertThat(filter).rejects((Map) null);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final PredicateMap<String> filter = new PredicateMap<>(DATE_KEY, new IsA(Map.class));

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{\n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.predicate.PredicateMap\",\n" +
                "  \"predicate\" : {\n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",\n" +
                "    \"type\" : \"java.util.Map\"\n" +
                "  },\n" +
                "  \"key\" : {\n" +
                "    \"java.util.Date\" : " + DATE_KEY.getTime() + "%n" +
                "  }\n" +
                "}"), json);

        // When 2
        final PredicateMap deserialisedFilter = JsonSerialiser.deserialise(json, PredicateMap.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getKey()).isEqualTo(DATE_KEY);
    }

    @Override
    protected PredicateMap getInstance() {
        return new PredicateMap<>(KEY1, new IsA(Map.class));
    }

    @Override
    protected Iterable<PredicateMap> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new PredicateMap(
                        KEY2, new IsA(Map.class)
                ),
                new PredicateMap(
                        KEY1, new IsMoreThan(5)
                ),
                new PredicateMap()
        );
    }
}

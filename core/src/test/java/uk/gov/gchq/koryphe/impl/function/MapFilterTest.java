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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.impl.predicate.AreEqual;
import uk.gov.gchq.koryphe.impl.predicate.IsA;
import uk.gov.gchq.koryphe.impl.predicate.IsIn;
import uk.gov.gchq.koryphe.impl.predicate.IsLessThan;
import uk.gov.gchq.koryphe.impl.predicate.IsMoreThan;
import uk.gov.gchq.koryphe.impl.predicate.IsXLessThanY;
import uk.gov.gchq.koryphe.impl.predicate.Not;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class MapFilterTest extends FunctionTest<MapFilter> {

    @Test
    public void shouldApplyKeyPredicate() {
        // Given
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 10);
        map.put(2, 20);
        map.put(3, 30);

        final Function<Map<Integer, Integer>, Map<Integer, Integer>> predicate =
                new MapFilter<Integer, Integer>()
                        .keyPredicate(new IsLessThan(2));

        // When
        predicate.apply(map);

        // Then
        assertThat(map)
                .containsEntry(1, 10)
                .hasSize(1);
    }

    @Test
    public void shouldApplyValuePredicate() {
        // Given
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 10);
        map.put(2, 20);
        map.put(3, 30);

        final Function<Map<Integer, Integer>, Map<Integer, Integer>> predicate =
                new MapFilter<Integer, Integer>()
                        .valuePredicate(new IsLessThan(20));

        // When
        predicate.apply(map);

        // Then
        assertThat(map)
                .containsEntry(1, 10)
                .hasSize(1);
    }

    @Test
    public void shouldApplyKeyValuePredicate() {
        // Given
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        map.put(2, 20);
        map.put(3, 30);

        final Function<Map<Integer, Integer>, Map<Integer, Integer>> predicate =
                new MapFilter<Integer, Integer>()
                        .keyValuePredicate(new AreEqual());

        // When
        predicate.apply(map);

        // Then
        assertThat(map)
                .containsEntry(1, 1)
                .hasSize(1);
    }

    @Test
    public void shouldApplyPredicates() {
        // Given
        final Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 1);
        map.put(2, 20);
        map.put(3, 3);
        map.put(4, 40);
        map.put(50, 50);
        map.put(60, 600);

        final Function<Map<Integer, Integer>, Map<Integer, Integer>> predicate =
                new MapFilter<Integer, Integer>()
                        .keyPredicate(new IsIn(1))
                        .valuePredicate(new IsLessThan(40))
                        .keyValuePredicate(new AreEqual());

        // When
        predicate.apply(map);

        // Then
        assertThat(map)
                .containsEntry(1, 1)
                .hasSize(1);
    }

    @Override
    protected MapFilter<Object, Object> getInstance() {
        return new MapFilter<>()
                .keyPredicate(new Not<>(new IsA(String.class)))
                .valuePredicate(new IsMoreThan(1))
                .keyValuePredicate(new AreEqual());
    }

    @Override
    protected Iterable<MapFilter> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new MapFilter()
                        .keyPredicate(new IsA(String.class))
                        .valuePredicate(new IsMoreThan(1))
                        .keyValuePredicate(new AreEqual()),
                new MapFilter<>()
                        .keyPredicate(new Not<>(new IsA(String.class)))
                        .valuePredicate(new IsLessThan(5))
                        .keyValuePredicate(new AreEqual()),
                new MapFilter<>()
                        .keyPredicate(new Not<>(new IsA(String.class)))
                        .valuePredicate(new IsMoreThan(1))
                        .keyValuePredicate(new IsXLessThanY())
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Map.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Map.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MapFilter predicate = getInstance();

        // When
        final String json = JsonSerialiser.serialise(predicate);

        // Then
        JsonSerialiser.assertEquals("{" +
                "\"class\":\"uk.gov.gchq.koryphe.impl.function.MapFilter\"," +
                "\"keyPredicate\":{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.Not\",\"predicate\":{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsA\",\"type\":\"java.lang.String\"}}," +
                "\"valuePredicate\":{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.IsMoreThan\",\"orEqualTo\":false,\"value\":1}," +
                "\"keyValuePredicate\":{\"class\":\"uk.gov.gchq.koryphe.impl.predicate.AreEqual\"}" +
                "}", json);

        // When 2
        final MapFilter deserialised = JsonSerialiser.deserialise(json, MapFilter.class);

        // Then 2
        assertThat(deserialised).isNotNull();
        assertThat(deserialised.getKeyPredicate().getClass()).isEqualTo(Not.class);
        assertThat(((Not) deserialised.getKeyPredicate()).getPredicate().getClass()).isEqualTo(IsA.class);
        assertThat(((IsMoreThan) deserialised.getValuePredicate()).getControlValue()).isEqualTo(1);
        assertThat(deserialised.getKeyValuePredicate().getClass()).isEqualTo(AreEqual.class);
    }
}

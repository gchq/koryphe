/*
 * Copyright 2021 Crown Copyright
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
package uk.gov.gchq.koryphe.impl.binaryoperator;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;
import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IterableConcatTest extends BinaryOperatorTest<IterableConcat> {

    @Test
    public void shouldConcatArraysTogether() {
        // Given
        final IterableConcat<Object> aggregator = new IterableConcat<>();

        final ArrayList<Object> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        final ArrayList<Object> list2 = new ArrayList<>(Arrays.asList("3", "4", 5L));

        // When
        final Iterable<Object> result = aggregator.apply(list1, list2);

        // The
        assertEquals(Arrays.asList(1, 2, 3, "3", "4", 5L), Lists.newArrayList(result));
    }

    @Test
    public void shouldAggregateTreeSetsTogether() {
        // Given
        final TreeSet<String> treeSet1 = new TreeSet<>();
        treeSet1.add("string1");

        final TreeSet<String> treeSet2 = new TreeSet<>();
        treeSet2.add("string3");
        treeSet2.add("string2");

        final IterableConcat<String> aggregator = new IterableConcat<>();

        // When
        final Iterable<String> result = aggregator.apply(treeSet1, treeSet2);

        // Then
        final TreeSet<String> expectedResult = new TreeSet<>();
        expectedResult.add("string1");
        expectedResult.add("string2");
        expectedResult.add("string3");
        assertEquals(expectedResult, result);
        assertEquals(TreeSet.class, result.getClass());
    }

    @Test
    public void shouldAggregateHashSetsTogether() {
        // Given
        final HashSet<Integer> hashSet1 = new HashSet<>();
        hashSet1.add(1);

        final HashSet<Integer> hashSet2 = new HashSet<>();
        hashSet2.add(2);
        hashSet2.add(3);

        IterableConcat<Integer> aggregator = new IterableConcat<>();

        // When
        final Iterable<Integer> result = aggregator.apply(hashSet1, hashSet2);

        // Then
        final HashSet<Integer> expectedResult = new HashSet<>();
        expectedResult.add(1);
        expectedResult.add(2);
        expectedResult.add(3);
        assertEquals(expectedResult, result);
        assertEquals(HashSet.class, result.getClass());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableConcat aggregator = new IterableConcat();

        // When 1
        new JsonSerialiser();
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.IterableConcat\"%n" +
                "}"), json);

        // When 2
        final IterableConcat deserialisedAggregator = JsonSerialiser.deserialise(json, IterableConcat.class);

        // Then 2
        assertNotNull(deserialisedAggregator);
    }

    @Override
    protected IterableConcat getInstance() {
        return new IterableConcat();
    }

    @Override
    protected Iterable<IterableConcat> getDifferentInstancesOrNull() {
        return null;
    }
}

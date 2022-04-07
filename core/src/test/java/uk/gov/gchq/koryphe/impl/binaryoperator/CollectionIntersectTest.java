/*
 * Copyright 2018-2020 Crown Copyright
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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionIntersectTest extends BinaryOperatorTest<CollectionIntersect> {

    @Test
    public void shouldIntersectArraysTogether() {
        // Given
        final CollectionIntersect<Object> aggregator = new CollectionIntersect<>();

        final ArrayList<Object> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        final ArrayList<Object> list2 = new ArrayList<>(Arrays.asList("3", "4", 1, 5L, 3));

        // When
        final Collection<Object> result = aggregator.apply(list1, list2);

        // Then
        assertThat(result).containsExactly(1, 3);
    }

    @Test
    public void shouldIntersectArraysTogetherWhenDisjoint() {
        // Given
        final CollectionIntersect<Object> aggregator = new CollectionIntersect<>();

        final ArrayList<Object> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        final ArrayList<Object> list2 = new ArrayList<>(Arrays.asList(4, 5, 6));

        // When
        final Collection<Object> result = aggregator.apply(list1, list2);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    public void shouldAggregateTreeSetsTogether() {
        // Given
        final TreeSet<String> treeSet1 = new TreeSet<>();
        treeSet1.add("string1");

        final TreeSet<String> treeSet2 = new TreeSet<>();
        treeSet2.add("string1");
        treeSet2.add("string2");

        final CollectionIntersect<String> aggregator = new CollectionIntersect<>();

        // When
        final Collection<String> result = aggregator.apply(treeSet1, treeSet2);

        // Then
        assertThat(result)
                .containsExactly("string1")
                .isExactlyInstanceOf(TreeSet.class);
    }

    @Test
    public void shouldAggregateHashSetsTogether() {
        // Given
        final HashSet<Integer> hashSet1 = new HashSet<>();
        hashSet1.add(1);

        final HashSet<Integer> hashSet2 = new HashSet<>();
        hashSet2.add(1);
        hashSet2.add(2);

        final CollectionIntersect<Integer> aggregator = new CollectionIntersect<>();

        // When
        final Collection<Integer> result = aggregator.apply(hashSet1, hashSet2);

        // Then
        assertThat(result)
                .containsExactly(1)
                .isExactlyInstanceOf(HashSet.class);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CollectionIntersect aggregator = new CollectionIntersect();

        // When 1
        new JsonSerialiser();
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.CollectionIntersect\"%n" +
                "}"), json);

        // When 2
        final CollectionIntersect deserialisedAggregator = JsonSerialiser.deserialise(json, CollectionIntersect.class);

        // Then 2
        assertThat(deserialisedAggregator).isNotNull();
    }

    @Override
    protected CollectionIntersect getInstance() {
        return new CollectionIntersect();
    }

    @Override
    protected Iterable<CollectionIntersect> getDifferentInstancesOrNull() {
        return null;
    }

}

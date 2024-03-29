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

public class CollectionConcatTest extends BinaryOperatorTest<CollectionConcat> {

    @Test
    public void shouldConcatArraysTogether() {
        // Given
        final CollectionConcat<Object> aggregator = new CollectionConcat<>();

        final ArrayList<Object> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        final ArrayList<Object> list2 = new ArrayList<>(Arrays.asList("3", "4", 5L));

        // When
        final Collection<Object> result = aggregator.apply(list1, list2);

        // Then
        assertThat(result).containsExactly(1, 2, 3, "3", "4", 5L);
    }

    @Test
    public void shouldAggregateTreeSetsTogether() {
        // Given
        final TreeSet<String> treeSet1 = new TreeSet<>();
        treeSet1.add("string1");

        final TreeSet<String> treeSet2 = new TreeSet<>();
        treeSet2.add("string3");
        treeSet2.add("string2");

        final CollectionConcat<String> aggregator = new CollectionConcat<>();

        // When
        final Collection<String> result = aggregator.apply(treeSet1, treeSet2);

        // Then
        assertThat(result)
                .containsExactly("string1", "string2", "string3")
                .isExactlyInstanceOf(TreeSet.class);
    }

    @Test
    public void shouldAggregateHashSetsTogether() {
        // Given
        final HashSet<Integer> hashSet1 = new HashSet<>();
        hashSet1.add(1);

        final HashSet<Integer> hashSet2 = new HashSet<>();
        hashSet2.add(2);
        hashSet2.add(3);

        CollectionConcat<Integer> aggregator = new CollectionConcat<>();

        // When
        final Collection<Integer> result = aggregator.apply(hashSet1, hashSet2);

        // Then
        assertThat(result)
                .containsExactly(1, 2, 3)
                .isExactlyInstanceOf(HashSet.class);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CollectionConcat aggregator = new CollectionConcat();

        // When 1
        new JsonSerialiser();
        final String json = JsonSerialiser.serialise(aggregator);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.CollectionConcat\"%n" +
                "}"), json);

        // When 2
        final CollectionConcat deserialisedAggregator = JsonSerialiser.deserialise(json, CollectionConcat.class);

        // Then 2
        assertThat(deserialisedAggregator).isNotNull();
    }

    @Override
    protected CollectionConcat getInstance() {
        return new CollectionConcat();
    }

    @Override
    protected Iterable<CollectionConcat> getDifferentInstancesOrNull() {
        return null;
    }
}

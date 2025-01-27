/*
 * Copyright 2025 Crown Copyright
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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.iterable.ChainedIterable;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

class IterableMergeTest extends BinaryOperatorTest<IterableMerge> {

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IterableMerge merger = new IterableMerge<>();

        // When
        final String json = JsonSerialiser.serialise(merger);

        // Then
        JsonSerialiser.assertEquals(
            String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.IterableMerge\"%n" +
                "}"),
            json);

        // When
        final IterableMerge deserialisedMerger = JsonSerialiser.deserialise(json, IterableMerge.class);

        // Then
        assertThat(deserialisedMerger).isNotNull();
    }

    @Test
    void shouldMergeArrays() {
        // Given
        final IterableMerge<Integer> merger = new IterableMerge<>();
        final List<Integer> itr1 = Lists.newArrayList(1, 2, 3, 4);
        final List<Integer> itr2 = Lists.newArrayList(5, 6);

        // When
        final Iterable<Integer> result = merger.apply(itr1, itr2);

        // Then
        assertThat(result)
            .isInstanceOf(ChainedIterable.class)
            .containsExactly(1, 2, 3, 4, 5, 6);
    }

    @Test
    void shouldMergeHashSets() {
        // Given
        final HashSet<Integer> hashSet1 = new HashSet<>();
        hashSet1.add(1);

        final HashSet<Integer> hashSet2 = new HashSet<>();
        hashSet2.add(2);
        hashSet2.add(3);

        IterableMerge<Integer> merger = new IterableMerge<>();
        final Iterable<Integer> result = merger.apply(hashSet1, hashSet2);

        assertThat(result).containsExactly(1, 2, 3);
    }

    @Test
    void shouldMergeTreeSets() {
        // Given
        final TreeSet<String> treeSet1 = new TreeSet<>();
        treeSet1.add("string1");

        final TreeSet<String> treeSet2 = new TreeSet<>();
        treeSet2.add("string3");
        treeSet2.add("string2");

        final IterableMerge<String> merger = new IterableMerge<>();

        // When
        final Iterable<String> result = merger.apply(treeSet1, treeSet2);

        // Then
        assertThat(result).containsExactly("string1", "string2", "string3");
    }

    @Test
    void shouldHandleNullElementsOfIterable() {
        // Given
        final IterableMerge<Integer> merger = new IterableMerge<>();
        final List<Integer> itr1 = Lists.newArrayList(1, 2, null, 4);
        final List<Integer> itr2 = Lists.newArrayList(null, 6);

        // When
        final Iterable<Integer> results = merger.apply(itr1, itr2);

        // Then
        assertThat(results).containsExactly(1, 2, null, 4, null, 6);
    }

    @Override
    protected IterableMerge getInstance() {
        return new IterableMerge<>();
    }

    @Override
    protected Iterable<IterableMerge> getDifferentInstancesOrNull() {
        return null;
    }
}

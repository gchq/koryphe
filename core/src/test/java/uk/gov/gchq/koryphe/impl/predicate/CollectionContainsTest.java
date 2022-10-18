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

package uk.gov.gchq.koryphe.impl.predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.CustomObj;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CollectionContainsTest extends PredicateTest<CollectionContains> {

    private static final CustomObj VALUE1 = new CustomObj();
    private static final String VALUE2 = "value2";

    private final List<Object> list = new ArrayList<>();
    private final Set<Object> set = new HashSet<>();

    @BeforeEach
    public void setup() {
        list.add(VALUE1);
        set.add(VALUE1);
    }

    @Test
    public void shouldAcceptWhenValueInList() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When / Then
        assertThat(filter).accepts(list);
    }

    @Test
    public void shouldAcceptWhenValueInSet() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When / Then
        assertThat(filter).accepts(set);
    }

    @Test
    public void shouldRejectWhenValueNotPresentInList() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE2);

        // When / Then
        assertThat(filter).rejects(list);
    }

    @Test
    public void shouldRejectWhenValueNotPresentInSet() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE2);

        // When / Then
        assertThat(filter).rejects(set);
    }

    @Test
    public void shouldRejectEmptyLists() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When / Then
        assertThat(filter).rejects(new ArrayList<>());
    }

    @Test
    public void shouldRejectEmptySets() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When / Then
        assertThat(filter).rejects(new HashSet<>());
    }

    @Test
    public void shouldRejectNullLists() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When / Then
        assertThat(filter).rejects((ArrayList<?>) null);
    }

    @Test
    public void shouldRejectNullSets() {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When / Then
        assertThat(filter).rejects((HashSet<?>) null);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CollectionContains filter = new CollectionContains(VALUE1);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.CollectionContains\",%n" +
                "  \"value\" : {\"class\":\"uk.gov.gchq.koryphe.util.CustomObj\", \"value\":\"1\"}%n" +
                "}"), json);

        // When 2
        final CollectionContains deserialisedFilter = JsonSerialiser.deserialise(json, CollectionContains.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getValue()).isEqualTo(VALUE1);
    }

    @Override
    protected CollectionContains getInstance() {
        return new CollectionContains(VALUE1);
    }

    @Override
    protected Iterable<CollectionContains> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new CollectionContains(),
                new CollectionContains(VALUE2)
        );
    }
}

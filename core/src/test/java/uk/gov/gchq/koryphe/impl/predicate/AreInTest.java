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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AreInTest extends PredicateTest<AreIn> {

    private static final CustomObj VALUE1 = new CustomObj();
    private static final String VALUE2 = "value2";

    private final List<Object> list = new ArrayList<>();
    private final Set<Object> set = new HashSet<>();

    @BeforeEach
    public void setup() {
        list.add(VALUE1);
        list.add(VALUE2);
        set.add(VALUE1);
        set.add(VALUE2);
    }

    @Test
    public void shouldAcceptWhenValuesAndInputAreNullOrEmpty() {
        // Given
        final AreIn filter = new AreIn();

        // When / Then
        assertThat(filter).accepts((Collection) null);
    }

    @Test
    public void shouldAcceptWhenValuesIsEmpty() {
        // Given
        final AreIn filter = new AreIn();

        // When / Then
        assertThat(filter).accepts(list);
    }

    @Test
    public void shouldRejectWhenValuesIsEmptyAndEmptyNotAllowed() {
        // Given
        final AreIn filter = new AreIn(new ArrayList<>(), false);

        // When / Then
        assertThat(filter).rejects(list);
    }

    @Test
    public void shouldAcceptWhenValuesIsNull() {
        // Given
        final AreIn filter = new AreIn(null, true);

        // When / Then
        assertThat(filter).accepts(list);
    }

    @Test
    public void shouldRejectWhenValuesIsNullAndNullNotAllowed() {
        // Given
        final AreIn filter = new AreIn(null, false);

        // When / Then
        assertThat(filter).rejects(list);
    }

    @Test
    public void shouldRejectWhenInputIsNullAndValuesNotEmpty() {
        // Given
        final AreIn filter = new AreIn(VALUE1, VALUE2);

        // When / Then
        assertThat(filter).rejects((Collection) null);
    }

    @Test
    public void shouldAcceptWhenAllValuesInList() {
        // Given
        final AreIn filter = new AreIn(VALUE1, VALUE2);

        // When / Then
        assertThat(filter).accepts(list);
    }

    @Test
    public void shouldAcceptWhenAllValuesInSet() {
        // Given
        final AreIn filter = new AreIn(VALUE1, VALUE2);

        // When / Then
        assertThat(filter).accepts(set);
    }

    @Test
    public void shouldRejectWhenNotAllValuesPresentInList() {
        // Given
        final AreIn filter = new AreIn(VALUE1);

        // When / Then
        assertThat(filter).rejects(list);
    }

    @Test
    public void shouldRejectWhenNotAllValuesPresentInSet() {
        // Given
        final AreIn filter = new AreIn(VALUE1);

        // When / Then
        assertThat(filter).rejects(set);
    }

    @Test
    public void shouldAcceptEmptyLists() {
        // Given
        final AreIn filter = new AreIn(VALUE1);

        // When / Then
        assertThat(filter).accepts(new ArrayList<>());
    }

    @Test
    public void shouldAcceptEmptySets() {
        // Given
        final AreIn filter = new AreIn(VALUE1);

        // When / Then
        assertThat(filter).accepts(new HashSet<>());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final AreIn filter = new AreIn(VALUE1);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.AreIn\",%n" +
                "  \"values\" : [{\"uk.gov.gchq.koryphe.util.CustomObj\":{\"value\":\"1\"}}]%n" +
                "}"), json);

        // When 2
        final AreIn deserialisedFilter = JsonSerialiser.deserialise(json, AreIn.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getValues().toArray()).isEqualTo(Collections.singleton(VALUE1).toArray());
    }

    @Override
    protected AreIn getInstance() {
        return new AreIn(VALUE1);
    }

    @Override
    protected Iterable<AreIn> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new AreIn(),
                new AreIn(VALUE1, VALUE2),
                new AreIn(VALUE2)
        );
    }
}

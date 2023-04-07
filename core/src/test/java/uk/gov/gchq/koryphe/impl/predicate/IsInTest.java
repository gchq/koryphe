/*
 * Copyright 2017-2023 Crown Copyright
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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.CustomObj;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class IsInTest extends PredicateTest<IsIn> {

    @Test
    public void shouldAcceptWhenValueInList() {
        // Given
        final IsIn filter = new IsIn(Arrays.asList("A", "B", "C"));

        // When / Then
        assertThat(filter).accepts("B");
    }

    @Test
    public void shouldRejectWhenValueNotInList() {
        // Given
        final IsIn filter = new IsIn(Arrays.asList("A", "B", "C"));

        // When / Then
        assertThat(filter).rejects("D");
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Object[] controlData = {1, new CustomObj(), 3};
        final IsIn filter = new IsIn(Arrays.asList(controlData));

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsIn\",%n" +
                "  \"values\" : [ 1, {\"uk.gov.gchq.koryphe.util.CustomObj\":{\"value\":\"1\"}}, 3 ]%n" +
                "}"), json);

        // When 2
        final IsIn deserialisedFilter = JsonSerialiser.deserialise(json, IsIn.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getAllowedValues()).containsExactlyElementsOf(new HashSet<>(Arrays.asList(controlData)));
    }

    @Override
    protected IsIn getInstance() {
        return new IsIn(Collections.singletonList("someValue"));
    }

    @Override
    protected Iterable<IsIn> getDifferentInstancesOrNull() {
        return null;
    }
}

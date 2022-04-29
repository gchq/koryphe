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

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.signature.InputValidatorAssert;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class IsLongerThanTest extends PredicateTest<IsLongerThan> {

    @Test
    public void shouldSetAndGetMinLength() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When
        final int minLength1 = filter.getMinLength();
        filter.setMinLength(10);
        final int minLength2 = filter.getMinLength();

        // Then
        assertThat(minLength1).isEqualTo(5);
        assertThat(minLength2).isEqualTo(10);
    }

    @Test
    public void shouldAcceptTheValueWhenMoreThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When / Then
        assertThat(filter).accepts("123456");
    }

    @Test
    public void shouldRejectTheValueWhenLessThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When / Then
        assertThat(filter).rejects("1234");
    }

    @Test
    public void shouldAcceptTheIterableValueWhenEqualTo() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5, true);

        // When / Then
        assertThat(filter).accepts(Collections.nCopies(5, "item"));
    }

    @Test
    public void shouldRejectTheIterableValueWhenNotEqualTo() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5, false);

        // When / Then
        assertThat(filter).rejects(Collections.nCopies(5, "item"));
    }

    @Test
    public void shouldAcceptTheIterableValueWhenMoreThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5, true);

        // When / Then
        assertThat(filter).accepts(Collections.nCopies(6, "item"));
    }

    @Test
    public void shouldRejectTheIterableValueWhenLessThan() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When / Then
        assertThat(filter).rejects(Collections.nCopies(4, "item"));
    }

    @Test
    public void shouldRejectTheValueWhenEqual() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When / Then
        assertThat(filter).rejects("12345");
    }

    @Test
    public void shouldThrowExceptionWhenTheValueWhenUnknownType() {
        // Given
        final IsLongerThan filter = new IsLongerThan(5);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> filter.test(4));
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final int min = 5;
        final IsLongerThan filter = new IsLongerThan(min);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsLongerThan\",%n" +
                "  \"orEqualTo\" : false,%n" +
                "  \"minLength\" : 5%n" +
                "}"), json);

        // When 2
        final IsLongerThan deserialisedFilter = JsonSerialiser.deserialise(json, IsLongerThan.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getMinLength()).isEqualTo(min);
    }

    @Test
    public void shouldCheckInputClass() {
        // When
        final IsLongerThan predicate = new IsLongerThan(10);

        // Then
        InputValidatorAssert.assertThat(predicate)
                .acceptsInput(String.class)
                .acceptsInput(Object[].class)
                .acceptsInput(Integer[].class)
                .acceptsInput(Collection.class)
                .acceptsInput(List.class)
                .acceptsInput(Map.class)
                .acceptsInput(HashMap.class)
                .rejectsInput(String.class, HashMap.class)
                .rejectsInput(Double.class)
                .rejectsInput(Integer.class, Integer.class);
    }

    @Override
    protected IsLongerThan getInstance() {
        return new IsLongerThan(5);
    }

    @Override
    protected Iterable<IsLongerThan> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new IsLongerThan(),
                new IsLongerThan(10)
        );
    }
}

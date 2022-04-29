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

public class IsShorterThanTest extends PredicateTest<IsShorterThan> {

    @Test
    public void shouldSetAndGetMaxLength() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5);

        // When
        final int maxLength1 = filter.getMaxLength();
        filter.setMaxLength(10);
        final int maxLength2 = filter.getMaxLength();

        // Then
        assertThat(maxLength1).isEqualTo(5);
        assertThat(maxLength2).isEqualTo(10);
    }

    @Test
    public void shouldAcceptTheValueWhenLessThan() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5);

        // When / Then
        assertThat(filter).accepts("1234");
    }

    @Test
    public void shouldRejectTheValueWhenMoreThan() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5);

        // When / Then
        assertThat(filter).rejects("123456");
    }

    @Test
    public void shouldAcceptTheIterableValueWhenEqualTo() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5, true);

        // When / Then
        assertThat(filter).accepts(Collections.nCopies(5, "item"));
    }

    @Test
    public void shouldRejectTheIterableValueWhenNotEqualTo() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5, false);

        // When / Then
        assertThat(filter).rejects(Collections.nCopies(5, "item"));
    }

    @Test
    public void shouldAcceptTheIterableValueWhenLessThan() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5, true);

        // When / Then
        assertThat(filter).accepts(Collections.nCopies(4, "item"));
    }

    @Test
    public void shouldRejectTheIterableValueWhenMoreThan() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5);

        // When / Then
        assertThat(filter).rejects(Collections.nCopies(6, "item"));
    }

    @Test
    public void shouldRejectTheValueWhenEqual() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5);

        // When / Then
        assertThat(filter).rejects("12345");
    }

    @Test
    public void shouldThrowExceptionWhenTheValueWhenUnknownType() {
        // Given
        final IsShorterThan filter = new IsShorterThan(5);

        // When / Then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> filter.test(4));
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final int max = 5;
        final IsShorterThan filter = new IsShorterThan(max);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsShorterThan\",%n" +
                "  \"orEqualTo\" : false,%n" +
                "  \"maxLength\" : 5%n" +
                "}"), json);

        // When 2
        final IsShorterThan deserialisedFilter = JsonSerialiser.deserialise(json, IsShorterThan.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(deserialisedFilter.getMaxLength()).isEqualTo(max);
    }

    @Test
    public void shouldCheckInputClass() {
        // When
        final IsShorterThan predicate = new IsShorterThan(10);

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
    protected IsShorterThan getInstance() {
        return new IsShorterThan(5);
    }

    @Override
    protected Iterable<IsShorterThan> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new IsShorterThan(10),
                new IsShorterThan()
        );
    }
}

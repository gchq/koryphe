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
import uk.gov.gchq.koryphe.signature.Signature;
import uk.gov.gchq.koryphe.signature.SignatureAssert;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NotTest extends PredicateTest<Not> {

    @Test
    public void shouldAcceptTheValueWhenTheWrappedFunctionReturnsFalse() {
        // Given
        final Predicate<String> function = mock(Predicate.class);
        final Not<String> filter = new Not<>(function);
        given(function.test("some value")).willReturn(false);

        // When / Then
        assertThat(filter).accepts("some value");
        verify(function).test("some value");
    }

    @Test
    public void shouldRejectTheValueWhenTheWrappedFunctionReturnsTrue() {
        // Given
        final Predicate<String> function = mock(Predicate.class);
        final Not<String> filter = new Not<>(function);
        given(function.test("some value")).willReturn(true);

        // When / Then
        assertThat(filter).rejects("some value");
        verify(function).test("some value");
    }

    @Test
    public void shouldRejectTheValueWhenNullFunction() {
        // Given
        final Not<String> filter = new Not<>();

        // When / Then
        assertThat(filter).rejects("some value");
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final IsA isA = new IsA(String.class);
        final Not<Object> filter = new Not<>(isA);

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.Not\",%n" +
                "  \"predicate\" : {%n" +
                "    \"class\" : \"uk.gov.gchq.koryphe.impl.predicate.IsA\",%n" +
                "    \"type\" : \"java.lang.String\"%n" +
                "  }%n" +
                "}"), json);

        // When 2
        final Not deserialisedFilter = JsonSerialiser.deserialise(json, Not.class);

        // Then 2
        assertThat(deserialisedFilter).isNotNull();
        assertThat(((IsA) deserialisedFilter.getPredicate()).getType()).isEqualTo(String.class.getName());
    }

    @Override
    protected Not<Object> getInstance() {
        return new Not<>(new IsA(String.class));
    }

    @Override
    protected Iterable<Not> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new Not<>(),
                new Not<>(new IsEqual("test")),
                new Not<>(new IsA(Long.class))
        );
    }

    @Test
    public void shouldCheckInputClass() {
        // Given
        Predicate predicate = new Not<>(new IsMoreThan(1));

        // When
        Signature input = Signature.getInputSignature(predicate);

        // Then
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class)
                .isNotAssignableFrom(Double.class)
                .isNotAssignableFrom(Integer.class, Integer.class);

        // Given 2
        predicate = new Not<>(new IsXLessThanY());

        // When 2
        input = Signature.getInputSignature(predicate);

        // Then 2
        SignatureAssert.assertThat(input)
                .isAssignableFrom(Integer.class, Integer.class)
                .isAssignableFrom(Double.class, Double.class)
                .isNotAssignableFrom(Integer.class)
                .isNotAssignableFrom(Double.class, Integer.class);
    }
}

/*
 * Copyright 2020-2022 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

public class IncrementTest extends FunctionTest<Increment> {
    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] { Number.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] { Number.class };
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        String json = "{ \"class\": \"uk.gov.gchq.koryphe.impl.function.Increment\" }";
        Increment increment = new Increment();

        // When
        String serialised = JsonSerialiser.serialise(increment);
        Increment deserialised = JsonSerialiser.deserialise(json, Increment.class);

        // Then
        JsonSerialiser.assertEquals(json, serialised);
        assertThat(deserialised).isEqualTo(increment);
    }

    @Test
    public void shouldReturnIncrementIfInputIsNull() {
        // Given
        Increment increment = new Increment(5);

        // When
        Number output = increment.apply(null);

        // Then
        assertThat(output).isEqualTo(5);
    }

    @Test
    public void shouldReturnInputIfIncrementIsNull() {
        // Given
        Increment increment = new Increment();

        // When
        Number output = increment.apply(8);

        // Then
        assertThat(output).isEqualTo(8);
    }

    @Test
    public void shouldMatchOutputTypeWithIncrementType() {
        // Given
        Increment increment = new Increment(10L);

        // When
        Number output = increment.apply(2);

        // Then
        assertThat(output)
                .isEqualTo(12L)
                .isExactlyInstanceOf(Long.class)
                .returns(12, from(Number::intValue));
    }

    @Test
    public void shouldBeAbleToHandleDifferentInputAndIncrementTypes() {
        // Given
        Increment increment = new Increment(10.5);

        // When
        Number output = increment.apply(2);

        // Then
        assertThat(output).isEqualTo(12.5);
    }

    @Override
    protected Increment getInstance() {
        return new Increment(3);
    }

    @Override
    protected Iterable<Increment> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new Increment(3L),
                new Increment(5),
                new Increment());
    }
}

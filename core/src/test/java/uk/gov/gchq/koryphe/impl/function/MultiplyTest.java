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

package uk.gov.gchq.koryphe.impl.function;

import org.junit.jupiter.api.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiplyTest extends FunctionTest<Multiply> {

    @Test
    public void shouldMultiply2() {
        // Given
        final Multiply function = new Multiply();

        // When
        final int output = function.apply(4, 2);

        // Then
        assertThat(output).isEqualTo(8);
    }

    @Test
    public void shouldMultiply1IfSecondIsNull() {
        // Given
        final Multiply function = new Multiply();

        // When
        final int output = function.apply(9, null);

        // Then
        assertThat(output).isEqualTo(9);
    }

    @Test
    public void shouldMultiply1IfFirstIsNull() {
        // Given
        final Multiply function = new Multiply();

        // When
        final Integer output = function.apply(null, 9);

        // Then
        assertThat(output).isNull();
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Multiply function = new Multiply();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.Multiply\"" +
                "}"), json);

        // When 2
        final Multiply deserialisedMultiply = JsonSerialiser.deserialise(json, Multiply.class);

        // Then 2
        assertThat(deserialisedMultiply).isNotNull();
    }

    @Override
    protected Multiply getInstance() {
        return new Multiply();
    }

    @Override
    protected Iterable<Multiply> getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Integer.class, Integer.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Integer.class};
    }
}

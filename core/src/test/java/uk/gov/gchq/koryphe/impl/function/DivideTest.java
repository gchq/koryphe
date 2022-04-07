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
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class DivideTest extends FunctionTest<Divide> {

    @Test
    public void shouldDivide2() {
        // Given
        final Divide function = new Divide();

        // When
        Tuple2<Integer, Integer> output = function.apply(4, 2);

        // Then
        assertThat(output).isEqualTo(new Tuple2<>(2, 0));
    }

    @Test
    public void shouldDivideBy2WithRemainder() {
        // Given
        final Divide function = new Divide();

        // When
        Tuple2<Integer, Integer> output = function.apply(5, 2);

        // Then
        assertThat(output).isEqualTo(new Tuple2<>(2, 1));
    }

    @Test
    public void shouldDivide1IfNull() {
        // Given
        final Divide function = new Divide();

        // When
        Tuple2<Integer, Integer> output = function.apply(9, null);

        // Then
        assertThat(output).isEqualTo(new Tuple2<>(9, 0));
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final Divide function = new Divide();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.Divide\"" +
                "}"), json);

        // When 2
        final Divide deserialisedDivide = JsonSerialiser.deserialise(json, Divide.class);

        // Then 2
        assertThat(deserialisedDivide).isNotNull();
    }

    @Override
    protected Divide getInstance() {
        return new Divide();
    }

    @Override
    protected Iterable getDifferentInstancesOrNull() {
        return null;
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Integer.class, Integer.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Integer.class, Integer.class};
    }
}

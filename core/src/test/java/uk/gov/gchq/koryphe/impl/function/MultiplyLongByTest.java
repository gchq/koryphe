/*
 * Copyright 2020 Crown Copyright
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

public class MultiplyLongByTest extends FunctionTest<MultiplyLongBy> {

    @Test
    public void shouldMultiplyBy2() {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy(2L);

        // When
        long output = function.apply(4L);

        // Then
        assertThat(output).isEqualTo(8L);
    }

    @Test
    public void shouldMultiplyBy1IfByIsNotSet() {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy();

        // When
        long output = function.apply(9L);

        // Then
        assertThat(output).isEqualTo(9L);
    }

    @Test
    public void shouldReturnNullIfInputIsNull() {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy(2L);

        // When
        Long output = function.apply(null);

        // Then
        assertThat(output).isNull();
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final MultiplyLongBy function = new MultiplyLongBy(4L);

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.MultiplyLongBy\",%n" +
                "  \"by\" : 4%n" +
                "}"), json);

        // When 2
        final MultiplyLongBy deserialisedMultiplyLongBy = JsonSerialiser.deserialise(json, MultiplyLongBy.class);

        // Then 2
        assertThat(deserialisedMultiplyLongBy)
                .isNotNull()
                .returns(4L, from(MultiplyLongBy::getBy));
    }

    @Override
    protected MultiplyLongBy getInstance() {
        return new MultiplyLongBy(3L);
    }

    @Override
    protected Iterable<MultiplyLongBy> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new MultiplyLongBy(),
                new MultiplyLongBy(100L)
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[]{ Long.class };
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[]{ Long.class };
    }
}
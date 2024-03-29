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
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

public class ConcatTest extends FunctionTest<Concat> {

    @Test
    public void shouldConcatStringsWithDefaultSeparator() {
        // Given
        final Concat concat = new Concat();

        // When
        String output = concat.apply("1", "2");

        // Then
        assertThat(output).isEqualTo("1,2");
    }

    @Test
    public void shouldConcatStringsWithGivenSeparator() {
        // Given
        final Concat concat = new Concat();
        concat.setSeparator(" ");

        // When
        final String output = concat.apply("1", "2");

        // Then
        assertThat(output).isEqualTo("1 2");
    }

    @Test
    public void shouldConvertNullValuesToEmptyStringWhenConcatenating() {
        // Given
        final Concat concat = new Concat();

        // When
        final String output = concat.apply("1", null);

        // Then
        assertThat(output).isEqualTo("1");
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final Concat concat = new Concat();

        // When
        final String output = concat.apply(null, null);

        assertThat(output).isNull();
    }

    @Override
    protected Concat getInstance() {
        return new Concat();
    }

    @Override
    protected Iterable<Concat> getDifferentInstancesOrNull() {
        return Collections.singletonList(new Concat(" "));
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final String separator = "-";
        final Concat concat = new Concat();
        concat.setSeparator(separator);

        // When
        final String json = JsonSerialiser.serialise(concat);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.function.Concat\",%n" +
                "  \"separator\" : \"-\"%n" +
                "}"), json);

        // When 2
        final Concat deserialisedConcat = JsonSerialiser.deserialise(json, Concat.class);

        // Then 2
        assertThat(deserialisedConcat)
                .isNotNull()
                .returns(separator, from(Concat::getSeparator));
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {Object.class, Object.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {String.class};
    }
}

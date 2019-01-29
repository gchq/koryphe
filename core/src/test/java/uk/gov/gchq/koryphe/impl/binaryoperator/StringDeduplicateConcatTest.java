/*
 * Copyright 2017-2019 Crown Copyright
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
package uk.gov.gchq.koryphe.impl.binaryoperator;

import org.junit.Test;

import uk.gov.gchq.koryphe.binaryoperator.BinaryOperatorTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class StringDeduplicateConcatTest extends BinaryOperatorTest {

    @Test
    public void shouldRemoveDuplicate() {
        // Given
        final StringDeduplicateConcat sdc = new StringDeduplicateConcat();

        // When
        String output = sdc._apply("test,string", "test,success");

        // Then
        assertEquals("test,string,success", output);
    }

    @Test
    public void shouldHandleTrailingDelimiter() {
        // Given
        final StringDeduplicateConcat sdc = new StringDeduplicateConcat();

        // When
        String output = sdc._apply("test,for,", "trailing,delimiters,");

        // Then
        assertEquals("test,for,trailing,delimiters", output);
    }

    @Test
    public void shouldHandleLeadingDelimiter() {
        // Given
        final StringDeduplicateConcat sdc = new StringDeduplicateConcat();

        // When
        String output = sdc._apply(",test,for", ",leading,delimiters");

        // Then
        assertEquals("test,for,leading,delimiters", output);
    }

    @Test
    public void shouldHandleNullSubstrings() {

        // Given
        final StringDeduplicateConcat sdc = new StringDeduplicateConcat();

        // When 1
        String nullString = sdc.apply(null, "test,first");

        // Then 1
        assertEquals("test,first", nullString);

        // When 2
        String stringNull = sdc.apply("test,second", null);

        // Then 2
        assertEquals("test,second", stringNull);

        // When 3
        String doubleNull = sdc.apply(null, null);

        // Then 3
        assertEquals(null, doubleNull);
    }

    @Test
    public void shouldHandleDefinedDelimiter() {
        // Given
        final StringDeduplicateConcat sdc = new StringDeduplicateConcat();
        sdc.setSeparator(";");

        // When
        String output = sdc._apply("test;string", "with;delimiter");

        // Then
        assertEquals("test;string;with;delimiter", output);
    }

    @Override
    protected StringDeduplicateConcat getInstance() {
        return new StringDeduplicateConcat();
    }

    @Override
    protected Class<StringDeduplicateConcat> getFunctionClass() {
        return StringDeduplicateConcat.class;
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final StringDeduplicateConcat sdc = new StringDeduplicateConcat();
        sdc.setSeparator(";");

        // When 1
        final String json = JsonSerialiser.serialise(sdc);

        // Then 1
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"uk.gov.gchq.koryphe.impl.binaryoperator.StringDeduplicateConcat\",%n" +
                "  \"separator\" : \";\"%n" +
                "}"), json);

        // When 2
        final StringDeduplicateConcat deserialisedOperator =
                JsonSerialiser.deserialise(json, getFunctionClass());

        // Then 2
        assertEquals(";", deserialisedOperator.getSeparator());
    }
}

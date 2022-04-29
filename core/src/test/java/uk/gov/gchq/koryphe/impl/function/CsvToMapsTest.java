/*
 * Copyright 2019-2020 Crown Copyright
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
import java.util.Map;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.InstanceOfAssertFactories.MAP;

public class CsvToMapsTest extends FunctionTest<CsvToMaps> {
    @Override
    protected CsvToMaps getInstance() {
        return new CsvToMaps();
    }

    @Override
    protected Iterable<CsvToMaps> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new CsvToMaps().delimiter('\t'),
                new CsvToMaps().firstRow(8),
                new CsvToMaps().quoteChar('\''),
                new CsvToMaps().quoted(),
                new CsvToMaps().header("myHeader")
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {String.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Iterable.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final CsvToMaps function = new CsvToMaps()
                .header("header1", "header2", "header3")
                .delimiter('|')
                .firstRow(2)
                .quoteChar('\'')
                .quoted();

        // When
        final String json = JsonSerialiser.serialise(function);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "   \"class\" : \"uk.gov.gchq.koryphe.impl.function.CsvToMaps\",%n" +
                "   \"header\" : [\"header1\", \"header2\", \"header3\"]," +
                "   \"delimiter\" : \"|\"," +
                "   \"firstRow\" : 2," +
                "   \"quoteChar\" : \"'\"," +
                "   \"quoted\" : true" +
                "}"), json);
    }

    @Test
    public void shouldParseCsvWithHeader() {
        // Given
        final CsvToMaps function = new CsvToMaps().header("header1", "header2", "header3").firstRow(1);
        String input = "header1,header2,header3\nvalue1,value2,value3";

        // When
        Iterable<Map<String, Object>> result = function.apply(input);

        // Then
        assertThat(result)
                .hasSize(1)
                .first(as(MAP))
                .containsOnly(
                        entry("header1", "value1"),
                        entry("header2", "value2"),
                        entry("header3", "value3"));
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final CsvToMaps function = new CsvToMaps();

        // When
        Object result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }
}

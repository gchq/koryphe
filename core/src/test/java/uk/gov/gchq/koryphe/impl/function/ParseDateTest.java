/*
 * Copyright 2019 Crown Copyright
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

import org.junit.Test;

import uk.gov.gchq.koryphe.function.FunctionTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ParseDateTest extends FunctionTest {
    @Override
    protected Function getInstance() {
        return new ParseDate();
    }

    @Override
    protected Class<? extends Function> getFunctionClass() {
        return ParseDate.class;
    }

    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ParseDate function = new ParseDate().format("yyyy dd").timeZone("GMT");

        // When
        final String json = JsonSerialiser.serialise(function);
        final ParseDate deserialised = JsonSerialiser.deserialise(json, ParseDate.class);

        // Then
        JsonSerialiser.assertEquals("{\"class\":\"uk.gov.gchq.koryphe.impl.function.ParseDate\",\"format\":\"yyyy dd\",\"timeZone\":\"GMT\"}", json);
        assertEquals(function.getFormat(), deserialised.getFormat());
        assertEquals(function.getTimeZone(), deserialised.getTimeZone());
    }

    @Test
    public void shouldParseDate() throws ParseException {
        // Given
        final ParseDate function = new ParseDate();
        final String input = "2000-01-02 03:04:05.006";

        // When
        Date result = function.apply(input);

        // Then
        assertEquals(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(input), result);
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final ParseDate function = new ParseDate();
        final String input = null;

        // When
        Object result = function.apply(input);

        // Then
        assertNull(result);
    }
}

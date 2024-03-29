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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.from;

public class ParseDateTest extends FunctionTest<ParseDate> {
    @Override
    protected ParseDate getInstance() {
        return new ParseDate().timeZone("UTC");
    }

    @Override
    protected Iterable<ParseDate> getDifferentInstancesOrNull() {
        return Arrays.asList(
                new ParseDate().timeZone("EST"),
                new ParseDate().format("dd-mmm-yy HH:mm:ss")
        );
    }

    @Override
    protected Class[] getExpectedSignatureInputClasses() {
        return new Class[] {String.class};
    }

    @Override
    protected Class[] getExpectedSignatureOutputClasses() {
        return new Class[] {Date.class};
    }

    @Test
    @Override
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final ParseDate function = new ParseDate().format("yyyy dd").timeZone("GMT");

        // When
        final String json = JsonSerialiser.serialise(function);
        final ParseDate deserialised = JsonSerialiser.deserialise(json, ParseDate.class);

        // Then
        JsonSerialiser.assertEquals("{\"class\":\"uk.gov.gchq.koryphe.impl.function.ParseDate\",\"format\":\"yyyy dd\",\"timeZone\":\"GMT\",\"microseconds\":false}", json);
        assertThat(deserialised)
                .returns(function.getFormat(), from(ParseDate::getFormat))
                .returns(function.getTimeZone(), from(ParseDate::getTimeZone));
    }

    @Test
    public void shouldParseDate() throws ParseException {
        // Given
        final ParseDate function = new ParseDate();
        final String input = "2000-01-02 03:04:05.006";

        // When
        final Date result = function.apply(input);

        // Then
        assertThat(result)
                .isEqualTo(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS").parse(input))
                .isEqualTo("2000-01-02 03:04:05.006");
    }

    @Test
    public void shouldParseDateWithFormat() throws ParseException {
        // Given
        final ParseDate function = new ParseDate().format("yyyy-MM hh:mm:ss.SSS");
        final String input = "2000-01 03:04:05.006";

        // When
        final Date result = function.apply(input);

        // Then
        assertThat(result)
                .isEqualTo(new SimpleDateFormat("yyyy-MM hh:mm:ss.SSS").parse(input));
    }

    @Test
    public void shouldParseTimestampInMilliseconds() throws ParseException {
        // Given
        final ParseDate function = new ParseDate();
        final String input = "946782245006";

        // When
        final Date result = function.apply(input);

        // Then
        assertThat(result).isEqualTo(new Date(Long.parseLong(input)));
    }

    @Test
    public void shouldParseAndFloorLowTimestampInMicroseconds() throws ParseException {
        // Given
        final ParseDate function = new ParseDate();
        function.setMicroseconds(true);
        final String input = "946782245006123";
        final String flooredInput = "946782245006000";

        // When
        final Date result = function.apply(input);

        // Then
        // Date has millisecond precision, therefore result is floored
        assertThat(ChronoUnit.MICROS.between(Instant.EPOCH, result.toInstant()))
                .isEqualTo(Long.parseLong(flooredInput));
    }

    @Test
    public void shouldParseAndFloorHighTimestampInMicroseconds() throws ParseException {
        // Given
        final ParseDate function = new ParseDate();
        function.setMicroseconds(true);
        final String input = "946782245006999";
        final String flooredInput = "946782245006000";

        // When
        final Date result = function.apply(input);

        // Then
        // Date has millisecond precision, therefore result is floored
        assertThat(ChronoUnit.MICROS.between(Instant.EPOCH, result.toInstant()))
                .isEqualTo(Long.parseLong(flooredInput));
        }

    @Test
    public void shouldParseTimestampInMicrosecondsToMilliseconds() throws ParseException {
        // Given
        final ParseDate function = new ParseDate();
        function.setMicroseconds(true);
        final String input = "946782245006000";

        // When
        final Date result = function.apply(input);

        // Then
        // Date has millisecond precision, therefore result is in milliseconds
        assertThat(result).isEqualTo(new Date(Long.parseLong(input.substring(0, input.length() - 3))));
    }

    @Test
    public void shouldReturnNullForNullInput() {
        // Given
        final ParseDate function = new ParseDate();

        // When
        final Object result = function.apply(null);

        // Then
        assertThat(result).isNull();
    }
}

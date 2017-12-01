/*
 * Copyright 2017 Crown Copyright
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

package uk.gov.gchq.koryphe.impl.predicate.range;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static uk.gov.gchq.koryphe.impl.predicate.range.InRangeDualTimeBased.DAYS_TO_MILLISECONDS;
import static uk.gov.gchq.koryphe.impl.predicate.range.InRangeDualTimeBased.HOURS_TO_MILLISECONDS;

public abstract class InRangeTimeBasedTest<T extends Comparable<T>> extends InRangeTest<T> {
    @Test
    public void shouldAcceptValuesInRangeDayOffset() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(-7)
                .endOffsetInDays(-2)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> validValues = Arrays.asList(
                now - 7 * DAYS_TO_MILLISECONDS + 5000,
                now - 3 * DAYS_TO_MILLISECONDS,
                now - 2 * DAYS_TO_MILLISECONDS
        );

        final List<Long> invalidValues = Arrays.asList(
                now - 8 * DAYS_TO_MILLISECONDS + 5000,
                now - DAYS_TO_MILLISECONDS,
                now
        );

        // When / Then
        testValues(true, validValues, filter);
        testValues(false, invalidValues, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeDayOffsetFromStart() throws IOException {
        // Given
        final long start = System.currentTimeMillis() - 100 * DAYS_TO_MILLISECONDS;
        final long end = System.currentTimeMillis() - 60 * DAYS_TO_MILLISECONDS;
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .start(convert(start))
                .startOffsetInDays(-7)
                .end(convert(end))
                .endOffsetInDays(-2)
                .build();

        final List<Long> validValues = Arrays.asList(
                start - 7 * DAYS_TO_MILLISECONDS,
                end - 3 * DAYS_TO_MILLISECONDS,
                end - 2 * DAYS_TO_MILLISECONDS
        );

        final List<Long> invalidValues = Arrays.asList(
                start - 7 * DAYS_TO_MILLISECONDS - 1,
                end - 2 * DAYS_TO_MILLISECONDS + 1,
                end
        );

        // When / Then
        testValues(true, validValues, filter);
        testValues(false, invalidValues, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeHourOffset() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInHours(-100L)
                .endOffsetInHours(-10L)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> validValues = Arrays.asList(
                now - 100 * HOURS_TO_MILLISECONDS + 5000,
                now - 50 * HOURS_TO_MILLISECONDS,
                now - 10 * HOURS_TO_MILLISECONDS
        );
        final List<Long> invalidValues = Arrays.asList(
                now - 110 * HOURS_TO_MILLISECONDS + 5000,
                now - 5 * HOURS_TO_MILLISECONDS,
                now
        );

        // When / Then
        testValues(true, validValues, filter);
        testValues(false, invalidValues, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeOffset() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInMillis(-100000L)
                .endOffsetInMillis(-10000L)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> validValues = Arrays.asList(
                now - 100000L + 5000,
                now - 50000L,
                now - 10000L
        );
        final List<Long> invalidValues = Arrays.asList(
                now - 110000L + 5000,
                now - 100L,
                now
        );

        // When / Then
        testValues(true, validValues, filter);
        testValues(false, invalidValues, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeHoursAndDaysOffset() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(-1)
                .endOffsetInHours(-4L)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> values = Arrays.asList(
                now - DAYS_TO_MILLISECONDS + 5000,
                now - 10 * HOURS_TO_MILLISECONDS,
                now - 4 * HOURS_TO_MILLISECONDS
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeDayOffsetLowerUnbounded() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .endOffsetInDays(-2)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> values = Arrays.asList(
                now - 10 * DAYS_TO_MILLISECONDS + 5000,
                now - 3 * DAYS_TO_MILLISECONDS,
                now - 2 * DAYS_TO_MILLISECONDS
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeDayOffsetUpperUnbounded() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(-7)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> values = Arrays.asList(
                now - 7 * DAYS_TO_MILLISECONDS + 5000,
                now - 3 * DAYS_TO_MILLISECONDS,
                now,
                now + 1000
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldConstructFromOffsetMillis() throws IOException {
        // When
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInMillis(-10000L)
                .endOffsetInMillis(1000L)
                .build();

        // Then
        assertEquals(-10000L, (long) filter.getStartOffsetInMillis());
        assertEquals(1000L, (long) filter.getEndOffsetInMillis());
        assertEquals(-10000L, (long) filter.getStartOffset());
        assertEquals(1000L, (long) filter.getEndOffset());
    }

    @Test
    public void shouldConstructFromOffsetHours() throws IOException {
        // When
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInHours(-1000L)
                .endOffsetInHours(100L)
                .build();

        // Then
        assertEquals(-1000L, (long) filter.getStartOffsetInHours());
        assertEquals(100L, (long) filter.getEndOffsetInHours());
        assertEquals(-1000L * HOURS_TO_MILLISECONDS, (long) filter.getStartOffset());
        assertEquals(100L * HOURS_TO_MILLISECONDS, (long) filter.getEndOffset());
    }


    @Test
    public void shouldConstructFromOffsetDays() throws IOException {
        // When
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(7)
                .endOffsetInDays(2)
                .build();

        // Then
        assertEquals(7, (int) filter.getStartOffsetInDays());
        assertEquals(2, (int) filter.getEndOffsetInDays());
        assertEquals(7 * DAYS_TO_MILLISECONDS, (long) filter.getStartOffset());
        assertEquals(2 * DAYS_TO_MILLISECONDS, (long) filter.getEndOffset());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithOffsets() throws IOException {
        // Given
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(7)
                .endOffsetInDays(0)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInDays\" : 7,%n" +
                "  \"endOffsetInDays\" : 0%n" +
                "}"), json);

        // When 2
        final InRangeTimeBased<T> deserialisedFilter = (InRangeTimeBased<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(7, (int) deserialisedFilter.getStartOffsetInDays());
        assertEquals(0, (int) deserialisedFilter.getEndOffsetInDays());
        assertEquals(DAYS_TO_MILLISECONDS * 7, (long) deserialisedFilter.getStartOffset());
        assertEquals(0, (long) deserialisedFilter.getEndOffset());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInMillis() throws IOException {
        // Given
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInMillis\" : " + 10000 + ",%n" +
                "  \"endOffsetInMillis\" : " + 1000 + "%n" +
                "}");

        // When
        final InRangeTimeBased<T> deserialisedFilter = (InRangeTimeBased<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(10000L, (long) deserialisedFilter.getStartOffsetInMillis());
        assertEquals(1000L, (long) deserialisedFilter.getEndOffsetInMillis());
        assertEquals(10000L, (long) deserialisedFilter.getStartOffset());
        assertEquals(1000L, (long) deserialisedFilter.getEndOffset());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInHours() throws IOException {
        // Given
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInHours\" : " + 1000 + ",%n" +
                "  \"endOffsetInHours\" : " + 100 + "%n" +
                "}");

        // When
        final InRangeTimeBased<T> deserialisedFilter = (InRangeTimeBased<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(1000L, (long) deserialisedFilter.getStartOffsetInHours());
        assertEquals(100L, (long) deserialisedFilter.getEndOffsetInHours());
        assertEquals(1000 * HOURS_TO_MILLISECONDS, (long) deserialisedFilter.getStartOffset());
        assertEquals(100 * HOURS_TO_MILLISECONDS, (long) deserialisedFilter.getEndOffset());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInDays() throws IOException {
        // Given
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInDays\" : " + 7 + ",%n" +
                "  \"endOffsetInDays\" : " + 2 + "%n" +
                "}");

        // When
        final InRangeTimeBased<T> deserialisedFilter = (InRangeTimeBased<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(7, (int) deserialisedFilter.getStartOffsetInDays());
        assertEquals(2, (int) deserialisedFilter.getEndOffsetInDays());
        assertEquals(7 * DAYS_TO_MILLISECONDS, (long) deserialisedFilter.getStartOffset());
        assertEquals(2 * DAYS_TO_MILLISECONDS, (long) deserialisedFilter.getEndOffset());
    }


    @Test
    public void shouldDeserialiseWithDateStrings() throws IOException, ParseException {
        // Given
        final String startString = "2017-01-02 00:30:45";
        final String endString = "2016-02-03 00:45:30";
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startString\" : \"" + startString + "\",%n" +
                "  \"endString\" : \"" + endString + "\"%n" +
                "}");

        // When
        final InRangeTimeBased<T> deserialisedFilter = (InRangeTimeBased<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(convert(DateUtils.parseDate(startString, Locale.getDefault(), "yyyy-MM-dd HH:mm:ss").getTime()), deserialisedFilter.getStart());
        assertEquals(convert(DateUtils.parseDate(endString, Locale.getDefault(), "yyyy-MM-dd HH:mm:ss").getTime()), deserialisedFilter.getEnd());
        assertEquals(startString, deserialisedFilter.getStartString());
        assertEquals(endString, deserialisedFilter.getEndString());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithDateStrings() throws IOException, ParseException {
        // Given
        final String startString = "2017-01-02 00:30:45";
        final String endString = "2016-02-03 00:45:30";
        final InRangeTimeBased<T> filter = createBuilderWithTimeOffsets()
                .startString(startString)
                .endString(endString)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startString\" : \"" + startString + "\",%n" +
                "  \"endString\" : \"" + endString + "\"%n" +
                "}"), json);

        // When 2
        final InRangeTimeBased<T> deserialisedFilter = (InRangeTimeBased<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(convert(DateUtils.parseDate(startString, Locale.getDefault(), "yyyy-MM-dd HH:mm:ss").getTime()), deserialisedFilter.getStart());
        assertEquals(convert(DateUtils.parseDate(endString, Locale.getDefault(), "yyyy-MM-dd HH:mm:ss").getTime()), deserialisedFilter.getEnd());
        assertEquals(startString, deserialisedFilter.getStartString());
        assertEquals(endString, deserialisedFilter.getEndString());
    }

    @Override
    protected String getStartJson(final T value) {
        final Long unconvert = unconvert(value);
        return null != unconvert ? unconvert.toString() : null;
    }

    @Override
    protected String getEndJson(final T value) {
        final Long unconvert = unconvert(value);
        return null != unconvert ? unconvert.toString() : null;
    }

    protected abstract InRangeTimeBased.BaseBuilder<?, ? extends InRangeTimeBased<T>, T> createBuilderWithTimeOffsets();

    @Override
    protected InRangeTimeBased.BaseBuilder createBuilder() {
        return createBuilderWithTimeOffsets();
    }

    protected Long unconvert(final T value) {
        return (Long) value;
    }
}

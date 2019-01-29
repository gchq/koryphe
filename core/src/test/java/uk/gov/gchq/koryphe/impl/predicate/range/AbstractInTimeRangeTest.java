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

package uk.gov.gchq.koryphe.impl.predicate.range;

import org.junit.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.TimeUnit;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static uk.gov.gchq.koryphe.util.DateUtil.DAYS_TO_MILLISECONDS;
import static uk.gov.gchq.koryphe.util.DateUtil.HOURS_TO_MILLISECONDS;
import static uk.gov.gchq.koryphe.util.DateUtil.MINUTES_TO_MILLISECONDS;

public abstract class AbstractInTimeRangeTest<T extends Comparable<T>> extends PredicateTest {
    @Test
    public void shouldAcceptValuesInRange() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .start("1")
                .end("10")
                .build();

        final List<Long> values = Arrays.asList(
                1L, 5L, 10L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInUpperUnboundedRange() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .start("1")
                .build();

        final List<Long> values = Arrays.asList(
                1L, 5L, 10L, 20L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInLowerUnboundedRange() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .end("10")
                .build();

        final List<Long> values = Arrays.asList(
                -10L, 0L, 1L, 5L, 10L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectNullValue() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .start("1")
                .end("10")
                .build();

        final List<Long> values = Collections.singletonList(null);

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldRejectValuesNotInRange() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .start("1")
                .end("10")
                .build();

        final List<Long> values = Arrays.asList(
                -5L, -1L, 0L, 11L, 100L
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldRejectValuesNotInExclusiveRange() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .start("1")
                .end("10")
                .startInclusive(false)
                .endInclusive(false)
                .build();

        final List<Long> values = Arrays.asList(
                -5L, -1L, 0L, 1L, 10L, 11L, 100L
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithExclusive() throws IOException {
        // Given
        final String start = "1000";
        final String end = "1010";
        final Predicate<T> filter = createBuilder()
                .start(start)
                .end(end)
                .startInclusive(false)
                .endInclusive(false)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : \"" + start + "\",%n" +
                "  \"end\" : \"" + end + "\",%n" +
                "  \"startInclusive\" : false,%n" +
                "  \"endInclusive\" : false%n" +
                "}"), json);

        // When 2
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
        assertFalse(deserialisedFilter.isStartInclusive());
        assertFalse(deserialisedFilter.isEndInclusive());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final String start = "1000";
        final String end = "1010";
        final AbstractInTimeRange filter = createBuilder()
                .start(start)
                .end(end)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : \"" + start + "\",%n" +
                "  \"end\" : \"" + end + "\"%n" +
                "}"), json);

        // When 2
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    @Test
    public void shouldAcceptValuesInRangeDayOffset() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-7L)
                .endOffset(-2L)
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
        final AbstractInTimeRange<T> filter = createBuilder()
                .start(Long.toString(start))
                .startOffset(-7L)
                .end(Long.toString(end))
                .endOffset(-2L)
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
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-100L)
                .endOffset(-10L)
                .offsetUnit(TimeUnit.HOUR)
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
    public void shouldAcceptValuesInRangeMinuteOffset() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-100L)
                .endOffset(-10L)
                .offsetUnit(TimeUnit.MINUTE)
                .build();

        final long now = System.currentTimeMillis();
        final List<Long> validValues = Arrays.asList(
                now - 100 * MINUTES_TO_MILLISECONDS + 5000,
                now - 50 * MINUTES_TO_MILLISECONDS,
                now - 10 * MINUTES_TO_MILLISECONDS
        );
        final List<Long> invalidValues = Arrays.asList(
                now - 110 * MINUTES_TO_MILLISECONDS + 5000,
                now - 5 * MINUTES_TO_MILLISECONDS,
                now
        );

        // When / Then
        testValues(true, validValues, filter);
        testValues(false, invalidValues, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeSecondOffset() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-100L)
                .endOffset(-10L)
                .offsetUnit(TimeUnit.SECOND)
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
    public void shouldAcceptValuesInRangeMilliOffset() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-100000L)
                .endOffset(-10000L)
                .offsetUnit(TimeUnit.MILLISECOND)
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
    public void shouldAcceptValuesInRangeDayOffsetLowerUnbounded() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .endOffset(-2L)
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
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-7L)
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
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-10000L)
                .endOffset(1000L)
                .offsetUnit(TimeUnit.MILLISECOND)
                .build();

        // Then
        assertEquals(-10000L, (long) filter.getStartOffset());
        assertEquals(1000L, (long) filter.getEndOffset());
        assertEquals(TimeUnit.MILLISECOND, filter.getOffsetUnit());
    }


    @Test
    public void shouldConstructFromOffsetSecond() throws IOException {
        // When
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(10000L)
                .endOffset(1000L)
                .offsetUnit(TimeUnit.SECOND)
                .build();

        // Then
        assertEquals(10000L, (long) filter.getStartOffset());
        assertEquals(1000L, (long) filter.getEndOffset());
        assertEquals(TimeUnit.SECOND, filter.getOffsetUnit());
    }

    @Test
    public void shouldConstructFromOffsetMinute() throws IOException {
        // When
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(10000L)
                .endOffset(1000L)
                .offsetUnit(TimeUnit.MINUTE)
                .build();

        // Then
        assertEquals(10000L, (long) filter.getStartOffset());
        assertEquals(1000L, (long) filter.getEndOffset());
        assertEquals(TimeUnit.MINUTE, filter.getOffsetUnit());
    }

    @Test
    public void shouldConstructFromOffsetHours() throws IOException {
        // When
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(-1000L)
                .endOffset(100L)
                .offsetUnit(TimeUnit.HOUR)
                .build();

        // Then
        assertEquals(-1000L, (long) filter.getStartOffset());
        assertEquals(100L, (long) filter.getEndOffset());
        assertEquals(TimeUnit.HOUR, filter.getOffsetUnit());
    }


    @Test
    public void shouldConstructFromOffsetDays() throws IOException {
        // When
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(7L)
                .endOffset(2L)
                .build();

        // Then
        assertEquals(7L, (long) filter.getStartOffset());
        assertEquals(2L, (long) filter.getEndOffset());
        // default is DAY
        assertNull(filter.getOffsetUnit());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithOffsets() throws IOException {
        // Given
        final AbstractInTimeRange<T> filter = createBuilder()
                .startOffset(7L)
                .endOffset(0L)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffset\" : 7,%n" +
                "  \"endOffset\" : 0%n" +
                "}"), json);

        // When 2
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(7L, (long) deserialisedFilter.getStartOffset());
        assertEquals(0L, (long) deserialisedFilter.getEndOffset());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInMillis() throws IOException {
        // Given
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffset\" : " + 10000 + ",%n" +
                "  \"endOffset\" : " + 1000 + "%n" +
                "}");

        // When
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(10000L, (long) deserialisedFilter.getStartOffset());
        assertEquals(1000L, (long) deserialisedFilter.getEndOffset());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInHours() throws IOException {
        // Given
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffset\" : " + 1000 + ",%n" +
                "  \"endOffset\" : " + 100 + "%n" +
                "}");

        // When
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(1000L, (long) deserialisedFilter.getStartOffset());
        assertEquals(100L, (long) deserialisedFilter.getEndOffset());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInDays() throws IOException {
        // Given
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffset\" : " + 7 + ",%n" +
                "  \"endOffset\" : " + 2 + "%n" +
                "}");

        // When
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(7L, (long) deserialisedFilter.getStartOffset());
        assertEquals(2L, (long) deserialisedFilter.getEndOffset());
    }


    @Test
    public void shouldDeserialiseWithDateStrings() throws IOException, ParseException {
        // Given
        final String start = "2017-01-02 00:30:45";
        final String end = "2016-02-03 00:45:30";
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : \"" + start + "\",%n" +
                "  \"end\" : \"" + end + "\"%n" +
                "}");

        // When
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithDateStrings() throws IOException, ParseException {
        // Given
        final String start = "2017-01-02 00:30:45";
        final String end = "2016-02-03 00:45:30";
        final AbstractInTimeRange<T> filter = createBuilder()
                .start(start)
                .end(end)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : \"" + start + "\",%n" +
                "  \"end\" : \"" + end + "\"%n" +
                "}"), json);

        // When 2
        final AbstractInTimeRange<T> deserialisedFilter = (AbstractInTimeRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    protected Class<? extends AbstractInTimeRange> getPredicateClass() {
        return getInstance().getClass();
    }

    protected AbstractInTimeRange<T> getInstance() {
        return createBuilder()
                .start("1000")
                .end("1010")
                .build();
    }

    protected T convert(final Long value) {
        return (T) value;
    }

    protected void testValues(final boolean expectedResult, final List<Long> values, final AbstractInTimeRange<T> filter) {
        final List<Boolean> results = new ArrayList<>(values.size());
        for (final Long value : values) {
            results.add(filter.test(convert(value)));
        }

        // Then
        for (int i = 0; i < values.size(); i++) {
            assertEquals("Failed for value: " + values.get(i), expectedResult, results.get(i));
        }
    }

    protected abstract AbstractInTimeRange.BaseBuilder<?, ? extends AbstractInTimeRange<T>, T> createBuilder();
}

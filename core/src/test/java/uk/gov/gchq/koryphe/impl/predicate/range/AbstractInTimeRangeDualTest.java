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

import org.junit.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;
import uk.gov.gchq.koryphe.util.TimeUnit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public abstract class AbstractInTimeRangeDualTest<T extends Comparable<T>> extends PredicateTest {
    @Test
    public void shouldAcceptValuesInRange() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start("1")
                .end("10")
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1L, 10L),
                new Tuple2<>(2L, 8L),
                new Tuple2<>(1L, 1L),
                new Tuple2<>(10L, 10L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInUpperUnboundedRange() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start("1")
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1L, 10L),
                new Tuple2<>(2L, 8L),
                new Tuple2<>(1L, 1L),
                new Tuple2<>(10L, 10L),
                new Tuple2<>(10L, 20L),
                new Tuple2<>(20L, 30L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInLowerUnboundedRange() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .end("10")
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(-20L, -10L),
                new Tuple2<>(-10L, 1L),
                new Tuple2<>(1L, 10L),
                new Tuple2<>(2L, 8L),
                new Tuple2<>(1L, 1L),
                new Tuple2<>(10L, 10L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectNullValue() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start("1")
                .end("10")
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(5L, null),
                new Tuple2<>(null, 5L),
                new Tuple2<>(null, null)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldRejectValuesNotInRange() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start("1")
                .end("10")
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(-20L, -10L),
                new Tuple2<>(-10L, 1L),
                new Tuple2<>(0L, 0L),
                new Tuple2<>(1L, 11L),
                new Tuple2<>(10L, 11L),
                new Tuple2<>(11L, 11L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldRejectValuesNotInExclusiveRange() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start("1")
                .end("10")
                .startInclusive(false)
                .endInclusive(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(-20L, -10L),
                new Tuple2<>(-10L, 1L),
                new Tuple2<>(1L, 1L),
                new Tuple2<>(1L, 11L),
                new Tuple2<>(10L, 11L),
                new Tuple2<>(10L, 10L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialisWithExclusive() throws IOException {
        // Given
        final String start = "1000";
        final String end = "1010";
        final Predicate filter = createBuilder()
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
        final AbstractInTimeRangeDual<T> deserialisedFilter = (AbstractInTimeRangeDual<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final String start = "1000";
        final String end = "1010";
        final Predicate filter = createBuilder()
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
        final AbstractInTimeRangeDual<T> deserialisedFilter = (AbstractInTimeRangeDual<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    @Test
    public void shouldConstructFromOffsetMillis() throws IOException {
        // When
        final AbstractInTimeRangeDual<T> filter = createBuilderWithTimeOffsets()
                .startOffset(10000L)
                .endOffset(1000L)
                .offsetUnit(TimeUnit.MILLISECOND)
                .build();

        // Then
        assertEquals(10000L, (long) filter.getStartOffset());
        assertEquals(1000L, (long) filter.getEndOffset());
        assertEquals(TimeUnit.MILLISECOND, filter.getOffsetUnit());
    }

    @Test
    public void shouldConstructFromOffsetSecond() throws IOException {
        // When
        final AbstractInTimeRangeDual<T> filter = createBuilderWithTimeOffsets()
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
        final AbstractInTimeRangeDual<T> filter = createBuilderWithTimeOffsets()
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
        final AbstractInTimeRangeDual<T> filter = createBuilderWithTimeOffsets()
                .startOffset(1000L)
                .endOffset(100L)
                .offsetUnit(TimeUnit.HOUR)
                .build();

        // Then
        assertEquals(1000L, (long) filter.getStartOffset());
        assertEquals(100L, (long) filter.getEndOffset());
        assertEquals(TimeUnit.HOUR, filter.getOffsetUnit());
    }

    @Test
    public void shouldConstructFromOffsetDays() throws IOException {
        // When
        final AbstractInTimeRangeDual<T> filter = createBuilderWithTimeOffsets()
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
        final AbstractInTimeRangeDual<T> filter = createBuilderWithTimeOffsets()
                .startOffset(7L)
                .endOffset(0L)
                .build();
        final String startValue = filter.getStart();
        final String endValue = filter.getEnd();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffset\" : 7,%n" +
                "  \"endOffset\" : 0%n" +
                "}"), json);

        // When 2
        final AbstractInTimeRangeDual<T> deserialisedFilter = (AbstractInTimeRangeDual<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(startValue, deserialisedFilter.getStart());
        assertEquals(endValue, deserialisedFilter.getEnd());
    }

    @Override
    protected Class<? extends Predicate> getPredicateClass() {
        return getInstance().getClass();
    }

    @Override
    protected Predicate getInstance() {
        return createBuilder()
                .start("1000")
                .end("1010")
                .build();
    }

    protected abstract AbstractInTimeRangeDual.BaseBuilder<?, ? extends AbstractInTimeRangeDual<T>, T> createBuilderWithTimeOffsets();

    protected AbstractInTimeRangeDual.BaseBuilder createBuilder() {
        return createBuilderWithTimeOffsets();
    }

    protected void testValues(final boolean expectedResult, final List<Tuple2<Long, Long>> values, final Predicate filter) {
        final List<Boolean> results = new ArrayList<>(values.size());
        for (final Tuple2<Long, Long> tuple : values) {
            results.add(((KoryphePredicate2) filter).test(convert(tuple.get0()), convert(tuple.get1())));
        }

        // Then
        for (int i = 0; i < values.size(); i++) {
            assertEquals("Failed for value: " + values.get(i), expectedResult, results.get(i));
        }
    }

    protected T convert(final Long value) {
        return (T) value;
    }
}

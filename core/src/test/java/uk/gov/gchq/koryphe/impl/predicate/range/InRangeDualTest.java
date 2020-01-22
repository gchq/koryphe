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

package uk.gov.gchq.koryphe.impl.predicate.range;

import org.junit.Test;

import uk.gov.gchq.koryphe.predicate.PredicateTest;
import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.tuple.predicate.KoryphePredicate2;
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InRangeDualTest<T extends Comparable<T>> extends PredicateTest {
    @Test
    public void shouldAcceptValuesInRange() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startFullyContained(true)
                .endFullyContained(true)
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
                .start(convert(1L))
                .startFullyContained(true)
                .endFullyContained(true)
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
                .end(convert(10L))
                .startFullyContained(true)
                .endFullyContained(true)
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
                .start(convert(1L))
                .end(convert(10L))
                .startFullyContained(true)
                .endFullyContained(true)
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
                .start(convert(1L))
                .end(convert(10L))
                .startFullyContained(true)
                .endFullyContained(true)
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
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(true)
                .endFullyContained(true)
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
    public void shouldAcceptValuesInStartAndEndPartiallyContained() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(false)
                .endFullyContained(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1L, 10L),
                new Tuple2<>(1L, 5L),
                new Tuple2<>(5L, 10L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInStartPartiallyContained() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(false)
                .endFullyContained(true)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1L, 5L),
                new Tuple2<>(0L, 5L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectValuesInStartPartiallyContained() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(false)
                .endFullyContained(true)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1L, 10L),
                new Tuple2<>(5L, 10L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldAcceptValuesInEndPartiallyContained() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(true)
                .endFullyContained(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(5L, 10L),
                new Tuple2<>(5L, 11L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectValuesInEndPartiallyContained() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(true)
                .endFullyContained(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1L, 5L),
                new Tuple2<>(1L, 10L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldAcceptValuesInStartAndEndPartiallyContainedInclusive() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(true)
                .endInclusive(true)
                .startFullyContained(false)
                .endFullyContained(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(0L, 10L),
                new Tuple2<>(0L, 5L),
                new Tuple2<>(5L, 11L),
                new Tuple2<>(10L, 10L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectValuesInStartAndEndPartiallyContained() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(false)
                .endFullyContained(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(0L, 1L),
                new Tuple2<>(10L, 11L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldRejectValuesInStartAndEndPartiallyContainedInclusive() throws IOException {
        // Given
        final Predicate filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .startInclusive(true)
                .endInclusive(true)
                .startFullyContained(false)
                .endFullyContained(false)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(-1L, 0L),
                new Tuple2<>(11L, 12L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialisWithOtherFields() throws IOException {
        // Given
        final T start = convert(1000L);
        final T end = convert(1010L);
        final Predicate filter = createBuilder()
                .start(start)
                .end(end)
                .startInclusive(false)
                .endInclusive(false)
                .startFullyContained(false)
                .endFullyContained(false)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : " + getStartJson(start) + ",%n" +
                "  \"end\" : " + getEndJson(end) + ",%n" +
                "  \"startInclusive\" : false,%n" +
                "  \"endInclusive\" : false,%n" +
                "  \"startFullyContained\" : false,%n" +
                "  \"endFullyContained\" : false%n" +
                "}"), json);

        // When 2
        final InRangeDual<T> deserialisedFilter = (InRangeDual<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final T start = convert(1000L);
        final T end = convert(1010L);
        final Predicate filter = createBuilder()
                .start(start)
                .end(end)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : " + getStartJson(start) + ",%n" +
                "  \"end\" : " + getEndJson(end) + "%n" +
                "}"), json);

        // When 2
        final InRangeDual<T> deserialisedFilter = (InRangeDual<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(start, deserialisedFilter.getStart());
        assertEquals(end, deserialisedFilter.getEnd());
    }

    protected String getStartJson(final T value) {
        return "{\"" + getTClass().getName() + "\": " + value + "}";
    }

    protected String getEndJson(final T value) {
        return "{\"" + getTClass().getName() + "\": " + value + "}";
    }

    @Override
    protected Class<? extends Predicate> getPredicateClass() {
        return getInstance().getClass();
    }

    @Override
    protected Predicate getInstance() {
        return createBuilder()
                .start(convert(1000L))
                .end(convert(1010L))
                .build();
    }

    protected T convert(final Long value) {
        return (T) value;
    }

    protected Class<T> getTClass() {
        return (Class<T>) Long.class;
    }

    protected InRangeDual.BaseBuilder<?, ? extends InRangeDual<T>, T> createBuilder() {
        return new InRangeDual.Builder<>();
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
}

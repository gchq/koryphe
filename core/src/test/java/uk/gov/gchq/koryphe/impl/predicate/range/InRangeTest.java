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
import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class InRangeTest<T extends Comparable<T>> extends PredicateTest {

    @Test
    public void shouldAcceptValuesInRange() throws IOException {
        // Given
        final InRange<T> filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
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
        final InRange<T> filter = createBuilder()
                .start(convert(1L))
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
        final InRange<T> filter = createBuilder()
                .end(convert(10L))
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
        final InRange<T> filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
                .build();

        final List<Long> values = Collections.singletonList(null);

        // When / Then
        testValues(false, values, filter);
    }

    @Test
    public void shouldRejectValuesNotInRange() throws IOException {
        // Given
        final InRange<T> filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
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
        final InRange<T> filter = createBuilder()
                .start(convert(1L))
                .end(convert(10L))
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
    public void shouldJsonSerialiseAndDeserialisWithExclusive() throws IOException {
        // Given
        final Predicate<T> filter = createBuilder()
                .start(convert(1000L))
                .end(convert(1010L))
                .startInclusive(false)
                .endInclusive(false)
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : " + getStartJson(convert(1000L)) + ",%n" +
                "  \"end\" : " + getEndJson(convert(1010L)) + ",%n" +
                "  \"startInclusive\" : false,%n" +
                "  \"endInclusive\" : false%n" +
                "}"), json);

        // When 2
        final InRange<T> deserialisedFilter = (InRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(convert(1000L), deserialisedFilter.getStart());
        assertEquals(convert(1010L), deserialisedFilter.getEnd());
        assertFalse(deserialisedFilter.isStartInclusive());
        assertFalse(deserialisedFilter.isEndInclusive());
    }

    @Test
    public void shouldJsonSerialiseAndDeserialise() throws IOException {
        // Given
        final InRange<T> filter = createBuilder()
                .start(convert(1000L))
                .end(convert(1010L))
                .build();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : " + getStartJson(convert(1000L)) + ",%n" +
                "  \"end\" : " + getEndJson(convert(1010L)) + "%n" +
                "}"), json);

        // When 2
        final InRange<T> deserialisedFilter = (InRange<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(convert(1000L), deserialisedFilter.getStart());
        assertEquals(convert(1010L), deserialisedFilter.getEnd());
    }

    protected String getStartJson(final T value) {
        return "{\"" + getTClass().getName() + "\": " + value + "}";
    }

    protected String getEndJson(final T value) {
        return "{\"" + getTClass().getName() + "\": " + value + "}";
    }

    @Override
    protected Class<? extends InRange> getPredicateClass() {
        return getInstance().getClass();
    }

    @Override
    protected InRange<T> getInstance() {
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

    protected InRange.BaseBuilder<?, ? extends InRange<T>, T> createBuilder() {
        return new InRange.Builder<>();
    }

    protected void testValues(final boolean expectedResult, final List<Long> values, final Predicate filter) {
        final List<Boolean> results = new ArrayList<>(values.size());
        for (final Long value : values) {
            results.add(filter.test(convert(value)));
        }

        // Then
        for (int i = 0; i < values.size(); i++) {
            assertEquals("Failed for value: " + values.get(i), expectedResult, results.get(i));
        }
    }
}

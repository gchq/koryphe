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

import uk.gov.gchq.koryphe.util.JsonSerialiser;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public abstract class InRangeWithTimeOffsetsTest<T extends Comparable<T>> extends InRangeTest<T> {
    @Test
    public void shouldConstructFromOffsetMillis() throws IOException {
        // Given
        final long initialTime = System.currentTimeMillis();

        // When
        final InRangeWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInMillis(10000L)
                .endOffsetInMillis(1000L)
                .build();

        // Then
        assertOffset(filter, 10000L, 1000L, initialTime);
    }

    @Test
    public void shouldConstructFromOffsetHours() throws IOException {
        // Given
        final long initialTime = System.currentTimeMillis();

        // When
        final InRangeWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInHours(1000L)
                .endOffsetInHours(100L)
                .build();

        // Then
        assertOffset(filter, InTimeRange.HOURS_TO_MILLISECONDS * 1000L, InTimeRange.HOURS_TO_MILLISECONDS * 100L, initialTime);
    }


    @Test
    public void shouldConstructFromOffsetDays() throws IOException {
        // Given
        final long initialTime = System.currentTimeMillis();

        // When
        final InRangeWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(7)
                .endOffsetInDays(2)
                .build();

        // Then
        assertOffset(filter, InTimeRange.DAYS_TO_MILLISECONDS * 7, InTimeRange.DAYS_TO_MILLISECONDS * 2, initialTime);
    }

    @Test
    public void shouldJsonSerialiseAndDeserialiseWithOffsets() throws IOException {
        // Given
        final InRangeWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInDays(7)
                .endOffsetInDays(0)
                .build();
        final T startValue = filter.getStart();
        final T endValue = filter.getEnd();

        // When
        final String json = JsonSerialiser.serialise(filter);

        // Then
        JsonSerialiser.assertEquals(String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"start\" : " + getStartJson(startValue) + ",%n" +
                "  \"end\" : " + getEndJson(endValue) + "%n" +
                "}"), json);

        // When 2
        final InRangeWithTimeOffsets<T> deserialisedFilter = (InRangeWithTimeOffsets<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(startValue, deserialisedFilter.getStart());
        assertEquals(endValue, deserialisedFilter.getEnd());
    }

    @Test
    public void shouldDeserialiseWithOffsetsInMillis() throws IOException {
        // Given
        final long initialTime = System.currentTimeMillis();
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInMillis\" : " + 10000 + ",%n" +
                "  \"endOffsetInMillis\" : " + 1000 + "%n" +
                "}");

        // When
        final InRangeWithTimeOffsets<T> deserialisedFilter = (InRangeWithTimeOffsets<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertOffset(deserialisedFilter, 10000, 1000, initialTime);
    }

    @Test
    public void shouldDeserialiseWithOffsetsInHours() throws IOException {
        // Given
        final long initialTime = System.currentTimeMillis();
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInHours\" : " + 1000 + ",%n" +
                "  \"endOffsetInHours\" : " + 100 + "%n" +
                "}");

        // When
        final InRangeWithTimeOffsets<T> deserialisedFilter = (InRangeWithTimeOffsets<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertOffset(deserialisedFilter, InTimeRange.HOURS_TO_MILLISECONDS * 1000, InTimeRange.HOURS_TO_MILLISECONDS * 100, initialTime);
    }

    @Test
    public void shouldDeserialiseWithOffsetsInDays() throws IOException {
        // Given
        final long initialTime = System.currentTimeMillis();
        final String json = String.format("{%n" +
                "  \"class\" : \"" + getPredicateClass().getName() + "\",%n" +
                "  \"startOffsetInDays\" : " + 7 + ",%n" +
                "  \"endOffsetInDays\" : " + 2 + "%n" +
                "}");

        // When
        final InRangeWithTimeOffsets<T> deserialisedFilter = (InRangeWithTimeOffsets<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertOffset(deserialisedFilter, InTimeRange.DAYS_TO_MILLISECONDS * 7, InTimeRange.DAYS_TO_MILLISECONDS * 2, initialTime);
    }

    @Override
    protected String getStartJson(final T value) {
        return unconvert(value).toString();
    }

    @Override
    protected String getEndJson(final T value) {
        return unconvert(value).toString();
    }

    protected abstract InRangeWithTimeOffsets.BaseBuilder<?, ? extends InRangeWithTimeOffsets<T>, T> createBuilderWithTimeOffsets();

    @Override
    protected InRangeWithTimeOffsets.BaseBuilder<?, ? extends InRangeWithTimeOffsets<T>, T> createBuilder() {
        return createBuilderWithTimeOffsets();
    }

    protected void assertOffset(final InRangeWithTimeOffsets<T> filter, final long startOffset, final long endOffset, final long initialTime) {
        // Check the offset is approximately correct - we don't know the exact time that was used.
        final long currentTime = System.currentTimeMillis();
        final long startOffsetNow = currentTime - unconvert(filter.getStart());
        assertTrue("startOffsetNow was " + startOffsetNow, startOffsetNow >= startOffset && startOffsetNow <= (startOffset + (currentTime - initialTime)));
        final long endOffsetNow = currentTime - unconvert(filter.getEnd());
        assertTrue("endOffsetNow was " + endOffsetNow, endOffsetNow >= endOffset && endOffsetNow <= (endOffset + (currentTime - initialTime)));
    }

    protected Long unconvert(final T value) {
        return (Long) value;
    }
}

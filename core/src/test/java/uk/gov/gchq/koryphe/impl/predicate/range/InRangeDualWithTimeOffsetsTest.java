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
import static uk.gov.gchq.koryphe.impl.predicate.range.InRangeDualWithTimeOffsets.DAYS_TO_MILLISECONDS;
import static uk.gov.gchq.koryphe.impl.predicate.range.InRangeDualWithTimeOffsets.HOURS_TO_MILLISECONDS;

public abstract class InRangeDualWithTimeOffsetsTest<T extends Comparable<T>> extends InRangeDualTest<T> {
    @Test
    public void shouldConstructFromOffsetMillis() throws IOException {
        // When
        final InRangeDualWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInMillis(10000L)
                .endOffsetInMillis(1000L)
                .build();

        // Then
        assertEquals(10000L, (long) filter.getStartOffsetInMillis());
        assertEquals(1000L, (long) filter.getEndOffsetInMillis());
        assertEquals(10000L, (long) filter.getStartOffset());
        assertEquals(1000L, (long) filter.getEndOffset());
    }

    @Test
    public void shouldConstructFromOffsetHours() throws IOException {
        // When
        final InRangeDualWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
                .startOffsetInHours(1000L)
                .endOffsetInHours(100L)
                .build();

        // Then
        assertEquals(1000L, (long) filter.getStartOffsetInHours());
        assertEquals(100L, (long) filter.getEndOffsetInHours());
        assertEquals(1000L * HOURS_TO_MILLISECONDS, (long) filter.getStartOffset());
        assertEquals(100L * HOURS_TO_MILLISECONDS, (long) filter.getEndOffset());
    }


    @Test
    public void shouldConstructFromOffsetDays() throws IOException {
        // When
        final InRangeDualWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
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
        final InRangeDualWithTimeOffsets<T> filter = createBuilderWithTimeOffsets()
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
                "  \"startOffsetInDays\" : 7,%n" +
                "  \"endOffsetInDays\" : 0%n" +
                "}"), json);

        // When 2
        final InRangeDualWithTimeOffsets<T> deserialisedFilter = (InRangeDualWithTimeOffsets<T>) deserialise(json);

        // Then 2
        assertNotNull(deserialisedFilter);
        assertEquals(startValue, deserialisedFilter.getStart());
        assertEquals(endValue, deserialisedFilter.getEnd());
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

    protected abstract InRangeDualWithTimeOffsets.BaseBuilder<?, ? extends InRangeDualWithTimeOffsets<T>, T> createBuilderWithTimeOffsets();

    @Override
    protected InRangeDualWithTimeOffsets.BaseBuilder<?, ? extends InRangeDualWithTimeOffsets<T>, T> createBuilder() {
        return createBuilderWithTimeOffsets();
    }

    protected Long unconvert(final T value) {
        return (Long) value;
    }
}

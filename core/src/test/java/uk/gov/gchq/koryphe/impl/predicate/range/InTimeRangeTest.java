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

import uk.gov.gchq.koryphe.util.TimeUnit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InTimeRangeTest extends AbstractInTimeRangeTest<Long> {
    @Test
    public void shouldAcceptValuesInRangeUsingMicroseconds() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("1")
                .end("10")
                .timeUnit(TimeUnit.MICROSECOND)
                .build();

        final List<Long> values = Arrays.asList(
                1000L, 5000L, 10000L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeUsingSeconds() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("1000")
                .end("10000")
                .timeUnit(TimeUnit.SECOND)
                .build();

        final List<Long> values = Arrays.asList(
                1L, 5L, 10L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeUsingMinutes() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("60000")
                .end("600000")
                .timeUnit(TimeUnit.MINUTE)
                .build();

        final List<Long> values = Arrays.asList(
                1L, 5L, 10L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeUsingHours() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("3600000")
                .end("36000000")
                .timeUnit(TimeUnit.HOUR)
                .build();

        final List<Long> values = Arrays.asList(
                1L, 5L, 10L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeUsingDays() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("86400000")
                .end("864000000")
                .timeUnit(TimeUnit.DAY)
                .build();

        final List<Long> values = Arrays.asList(
                1L, 5L, 10L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeWithOffsetUsingMicroseconds() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("1")
                .end("10")
                .startOffset(1L)
                .endOffset(20L)
                .offsetUnit(TimeUnit.MILLISECOND)
                .timeUnit(TimeUnit.MICROSECOND)
                .build();

        final List<Long> values = Arrays.asList(
                2000L, 5000L, 30000L
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectValuesInRangeWithOffsetUsingMicroseconds() throws IOException {
        // Given
        final InTimeRange filter = createBuilder()
                .start("1")
                .end("10")
                .startOffset(1L)
                .endOffset(20L)
                .offsetUnit(TimeUnit.MILLISECOND)
                .timeUnit(TimeUnit.MICROSECOND)
                .build();

        final List<Long> values = Arrays.asList(
                1999L, 30001L
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Override
    protected InTimeRange.Builder createBuilder() {
        return new InTimeRange.Builder();
    }
}

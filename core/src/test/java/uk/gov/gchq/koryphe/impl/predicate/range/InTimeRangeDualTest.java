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

import uk.gov.gchq.koryphe.tuple.n.Tuple2;
import uk.gov.gchq.koryphe.util.TimeUnit;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InTimeRangeDualTest extends AbstractInTimeRangeDualTest<Long> {

    @Test
    public void shouldAcceptValuesInRangeUsingMicroseconds() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("1")
                .end("10")
                .timeUnit(TimeUnit.MICROSECOND)
                .startFullyContained(true)
                .endFullyContained(true)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1000L, 10000L),
                new Tuple2<>(2000L, 8000L),
                new Tuple2<>(1000L, 1000L),
                new Tuple2<>(10000L, 10000L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldAcceptValuesInRangeUsingSeconds() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("1000")
                .end("10000")
                .timeUnit(TimeUnit.SECOND)
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
    public void shouldAcceptValuesInRangeUsingMinutes() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("60000")
                .end("600000")
                .timeUnit(TimeUnit.MINUTE)
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
    public void shouldAcceptValuesInRangeUsingHours() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("3600000")
                .end("36000000")
                .timeUnit(TimeUnit.HOUR)
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
    public void shouldAcceptValuesInRangeUsingDays() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("86400000")
                .end("864000000")
                .timeUnit(TimeUnit.DAY)
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
    public void shouldAcceptValuesInRangeWithOffsetUsingMicroseconds() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("1")
                .end("10")
                .startOffset(1L)
                .endOffset(20L)
                .offsetUnit(TimeUnit.MILLISECOND)
                .timeUnit(TimeUnit.MICROSECOND)
                .startFullyContained(true)
                .endFullyContained(true)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(2000L, 30000L),
                new Tuple2<>(2000L, 8000L),
                new Tuple2<>(2000L, 2000L),
                new Tuple2<>(30000L, 30000L)
        );

        // When / Then
        testValues(true, values, filter);
    }

    @Test
    public void shouldRejectValuesInRangeWithOffsetUsingMicroseconds() throws IOException {
        // Given
        final InTimeRangeDual filter = createBuilderWithTimeOffsets()
                .start("1")
                .end("10")
                .startOffset(1L)
                .endOffset(20L)
                .offsetUnit(TimeUnit.MILLISECOND)
                .timeUnit(TimeUnit.MICROSECOND)
                .startFullyContained(true)
                .endFullyContained(true)
                .build();

        final List<Tuple2<Long, Long>> values = Arrays.asList(
                new Tuple2<>(1999L, 30001L),
                new Tuple2<>(1999L, 1999L),
                new Tuple2<>(30001L, 30001L)
        );

        // When / Then
        testValues(false, values, filter);
    }

    @Override
    protected InTimeRangeDual.Builder createBuilderWithTimeOffsets() {
        return new InTimeRangeDual.Builder();
    }
}

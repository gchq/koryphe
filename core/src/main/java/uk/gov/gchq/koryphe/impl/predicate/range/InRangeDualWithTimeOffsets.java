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

import java.util.function.Function;

/**
 * <p>
 * An <code>AbstractDualTimeRange</code> is a {@link InRangeDual}
 * {@link java.util.function.Predicate} that can be configured using time offsets
 * from the current system time.
 * </p>
 * <p>
 * So you can set the start range bound using
 * the start value as normal, or using startOffsetInMillis, startOffsetInHours
 * or startOffsetInDays. At the point of this class being instantiated the
 * current system time is used to calculate the start value based on:
 * System.currentTimeMillis() - offset.
 * </p>
 * <p>
 * Similarly with the end range bound, this can be set using end, endOffsetInMillis,
 * endOffsetInHours or endOffsetInDays.
 * </p>
 *
 * @see InRangeDual
 */
public abstract class InRangeDualWithTimeOffsets<T extends Comparable<T>> extends InRangeDual<T> {
    public InRangeDualWithTimeOffsets(final T start,
                                      final Long startOffsetInMillis,
                                      final Long startOffsetInHours,
                                      final Integer startOffsetInDays,
                                      final T end,
                                      final Long endOffsetInMillis,
                                      final Long endOffsetInHours,
                                      final Integer endOffsetInDays,
                                      final Boolean startInclusive,
                                      final Boolean endInclusive) {
        this(start, startOffsetInMillis, startOffsetInHours, startOffsetInDays, end, endOffsetInMillis, endOffsetInHours, endOffsetInDays, startInclusive, endInclusive, t -> (T) t);
    }

    public InRangeDualWithTimeOffsets(final T start,
                                      final Long startOffsetInMillis,
                                      final Long startOffsetInHours,
                                      final Integer startOffsetInDays,
                                      final T end,
                                      final Long endOffsetInMillis,
                                      final Long endOffsetInHours,
                                      final Integer endOffsetInDays,
                                      final Boolean startInclusive,
                                      final Boolean endInclusive,
                                      final Function<Long, T> mapper) {
        super(
                InRangeWithTimeOffsets.getFromOffset(start, startOffsetInMillis, startOffsetInHours, startOffsetInDays, "start", mapper),
                InRangeWithTimeOffsets.getFromOffset(end, endOffsetInMillis, endOffsetInHours, endOffsetInDays, "end", mapper),
                startInclusive,
                endInclusive
        );
    }

    public abstract static class Builder<B extends Builder<B, R, T>, R extends InRangeDualWithTimeOffsets<T>, T extends Comparable<T>> extends InRangeWithTimeOffsets.BaseBuilder<Builder<B, R, T>, R, T> {
    }
}

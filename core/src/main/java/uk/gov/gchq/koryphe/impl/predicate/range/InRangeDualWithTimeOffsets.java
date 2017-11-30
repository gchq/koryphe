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
import java.util.function.Predicate;

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
    public static final long HOURS_TO_MILLISECONDS = 60L * 60L * 1000L;
    public static final long DAYS_TO_MILLISECONDS = 24L * HOURS_TO_MILLISECONDS;

    private final Long startOffsetInMillis;
    private final Long startOffsetInHours;
    private final Integer startOffsetInDays;
    private final Long endOffsetInMillis;
    private final Long endOffsetInHours;
    private final Integer endOffsetInDays;
    private final Function<Long, T> toT;
    private final Function<T, Long> toLong;

    private final Long startOffset;
    private final Long endOffset;

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
        this(
                start, startOffsetInMillis, startOffsetInHours, startOffsetInDays,
                end, endOffsetInMillis, endOffsetInHours, endOffsetInDays,
                startInclusive, endInclusive,
                t -> (T) t,
                l -> (Long) l
        );
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
                                      final Function<Long, T> toT,
                                      final Function<T, Long> toLong) {
        super(start, end, startInclusive, endInclusive);
        this.startOffsetInMillis = startOffsetInMillis;
        this.startOffsetInHours = startOffsetInHours;
        this.startOffsetInDays = startOffsetInDays;
        this.endOffsetInMillis = endOffsetInMillis;
        this.endOffsetInHours = endOffsetInHours;
        this.endOffsetInDays = endOffsetInDays;
        this.toT = toT;
        this.toLong = toLong;

        this.startOffset = calculateOffset(startOffsetInMillis, startOffsetInHours, startOffsetInDays, "start");
        this.endOffset = calculateOffset(endOffsetInMillis, endOffsetInHours, endOffsetInDays, "end");
    }

    public static Long calculateOffset(final Long valueOffsetInMillis, final Long valueOffsetInHours, final Integer valueOffsetInDays, final String variableName) {
        final boolean isValueInvalid;
        if (null != valueOffsetInMillis) {
            isValueInvalid = null != valueOffsetInHours || null != valueOffsetInDays;
        } else {
            isValueInvalid = null != valueOffsetInHours && null != valueOffsetInDays;
        }
        if (isValueInvalid) {
            throw new IllegalArgumentException(String.format("Only set one of the following: %sOffsetInMillis, %sOffsetInHours or %sOffsetInDays", variableName, variableName, variableName));
        }

        final Long offset;
        if (null != valueOffsetInHours) {
            offset = HOURS_TO_MILLISECONDS * valueOffsetInHours;
        } else if (null != valueOffsetInDays) {
            offset = DAYS_TO_MILLISECONDS * valueOffsetInDays;
        } else {
            offset = valueOffsetInMillis;
        }

        return offset;
    }

    protected T getValueFromOffset(final T value, final Long offset) {
        if (null == offset) {
            return value;
        }

        final long base = null != value ? toLong.apply(value) : System.currentTimeMillis();
        return toT.apply(base - offset);
    }

    @Override
    protected boolean testAgainstRange(final Comparable<T> startValue, final Comparable<T> endValue,
                                       final T rangeStart, final T rangeEnd) {
        return super.testAgainstRange(
                startValue,
                endValue,
                getValueFromOffset(rangeStart, startOffset),
                getValueFromOffset(rangeEnd, endOffset)
        );
    }

    public Long getStartOffsetInMillis() {
        return startOffsetInMillis;
    }

    public Long getStartOffsetInHours() {
        return startOffsetInHours;
    }

    public Integer getStartOffsetInDays() {
        return startOffsetInDays;
    }

    public Long getEndOffsetInMillis() {
        return endOffsetInMillis;
    }

    public Long getEndOffsetInHours() {
        return endOffsetInHours;
    }

    public Integer getEndOffsetInDays() {
        return endOffsetInDays;
    }

    protected Long getStartOffset() {
        return startOffset;
    }

    protected Long getEndOffset() {
        return endOffset;
    }

    public abstract static class BaseBuilder<
            B extends BaseBuilder<B, R, T>,
            R extends Predicate,
            T extends Comparable<T>>
            extends InRangeDual.BaseBuilder<B, R, T> {
        protected Long startOffsetInMillis;
        protected Long startOffsetInHours;
        protected Integer startOffsetInDays;

        protected Long endOffsetInMillis;
        protected Long endOffsetInHours;
        protected Integer endOffsetInDays;

        public B startOffsetInMillis(final Long startOffsetInMillis) {
            this.startOffsetInMillis = startOffsetInMillis;
            return getSelf();
        }

        public B startOffsetInHours(final Long startOffsetInHours) {
            this.startOffsetInHours = startOffsetInHours;
            return getSelf();
        }

        public B startOffsetInDays(final Integer startOffsetInDays) {
            this.startOffsetInDays = startOffsetInDays;
            return getSelf();
        }

        public B endOffsetInMillis(final Long endOffsetInMillis) {
            this.endOffsetInMillis = endOffsetInMillis;
            return getSelf();
        }

        public B endOffsetInHours(final Long endOffsetInHours) {
            this.endOffsetInHours = endOffsetInHours;
            return getSelf();
        }

        public B endOffsetInDays(final Integer endOffsetInDays) {
            this.endOffsetInDays = endOffsetInDays;
            return getSelf();
        }
    }
}

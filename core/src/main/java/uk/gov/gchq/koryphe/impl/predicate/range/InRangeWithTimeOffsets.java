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

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * An <code>InRangeWithTimeOffsets</code> is a {@link InRange}
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
 * @see InRange
 */
public abstract class InRangeWithTimeOffsets<T extends Comparable<T>> extends InRange<T> {
    public static final long HOURS_TO_MILLISECONDS = 60L * 60L * 1000L;
    public static final long DAYS_TO_MILLISECONDS = 24L * HOURS_TO_MILLISECONDS;

    public InRangeWithTimeOffsets(final T start,
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
                t -> (T) t
        );
    }

    public InRangeWithTimeOffsets(final T start,
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
                getFromOffset(start, startOffsetInMillis, startOffsetInHours, startOffsetInDays, "start", mapper),
                getFromOffset(end, endOffsetInMillis, endOffsetInHours, endOffsetInDays, "end", mapper),
                startInclusive,
                endInclusive
        );
    }

    public static <T> T getFromOffset(final T value, final Long valueOffsetInMillis, final Long valueOffsetInHours, final Integer valueOffsetInDays, final String variableName, final Function<Long, T> mapper) {
        final boolean isValueInvalid;
        if (null != value) {
            isValueInvalid = null != valueOffsetInMillis || null != valueOffsetInHours || null != valueOffsetInDays;
        } else if (null != valueOffsetInMillis) {
            isValueInvalid = null != valueOffsetInHours || null != valueOffsetInDays;
        } else {
            isValueInvalid = null != valueOffsetInHours && null != valueOffsetInDays;
        }
        if (isValueInvalid) {
            throw new IllegalArgumentException(String.format("Only set one of the following: %s, %sOffset, %sOffsetInHours or %sOffsetInDays", variableName, variableName, variableName, variableName));
        }

        final T resolvedValue;
        if (null != valueOffsetInHours) {
            resolvedValue = mapper.apply(System.currentTimeMillis() - (HOURS_TO_MILLISECONDS * valueOffsetInHours));
        } else if (null != valueOffsetInDays) {
            resolvedValue = mapper.apply(System.currentTimeMillis() - (DAYS_TO_MILLISECONDS * valueOffsetInDays));
        } else if (null != valueOffsetInMillis) {
            resolvedValue = mapper.apply(System.currentTimeMillis() - valueOffsetInMillis);
        } else {
            resolvedValue = value;
        }

        return resolvedValue;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .toString();
    }

    public abstract static class BaseBuilder<B extends BaseBuilder<B, R, T>, R extends Predicate, T extends Comparable<T>>
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

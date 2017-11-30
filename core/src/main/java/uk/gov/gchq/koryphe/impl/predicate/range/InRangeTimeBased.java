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

import com.fasterxml.jackson.annotation.JsonGetter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>
 * An <code>InRangeTimeBased</code> is a {@link InRange}
 * {@link java.util.function.Predicate} that can be configured using time offsets
 * from the current system time or a provided start/end time.
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
 * <p>
 * You can also configure the start and end times using startString and endString. These strings should be in one of the following formats:
 * </p>
 * <ul>
 * <li>yyyy</li>
 * <li>yyyy-MM</li>
 * <li>yyyy-MM-dd</li>
 * <li>yyyy-MM-dd-HH</li>
 * <li>yyyy-MM-dd-HH:mm</li>
 * <li>yyyy-MM-dd-HH:mm:ss</li>
 * </ul>
 *
 * @see InRange
 */
public abstract class InRangeTimeBased<T extends Comparable<T>> extends InRange<T> {
    protected InRangeTimeBased(final InRangeDualTimeBased<T> predicate) {
        super(predicate);
    }

    public Long getStartOffsetInMillis() {
        return getPredicate().getStartOffsetInMillis();
    }

    public Long getStartOffsetInHours() {
        return getPredicate().getStartOffsetInHours();
    }

    public Integer getStartOffsetInDays() {
        return getPredicate().getStartOffsetInDays();
    }

    public Long getEndOffsetInMillis() {
        return getPredicate().getEndOffsetInMillis();
    }

    public Long getEndOffsetInHours() {
        return getPredicate().getEndOffsetInHours();
    }

    public Integer getEndOffsetInDays() {
        return getPredicate().getEndOffsetInDays();
    }

    public String getStartDateString() {
        return getPredicate().getStartDateString();
    }

    public String getEndDateString() {
        return getPredicate().getEndDateString();
    }

    @JsonGetter("start")
    T getStartForJson() {
        return getPredicate().getStartForJson();
    }

    @JsonGetter("end")
    T getEndForJson() {
        return getPredicate().getEndForJson();
    }

    protected Long getStartOffset() {
        return getPredicate().getStartOffset();
    }

    protected Long getEndOffset() {
        return getPredicate().getEndOffset();
    }

    @SuppressFBWarnings(value = "BC_UNCONFIRMED_CAST_OF_RETURN_VALUE", justification = "The predicate will always be of this type.")
    @Override
    protected InRangeDualTimeBased<T> getPredicate() {
        return (InRangeDualTimeBased<T>) super.getPredicate();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .toString();
    }

    public abstract static class BaseBuilder<
            B extends BaseBuilder<B, R, T>,
            R extends InRangeTimeBased<T>,
            T extends Comparable<T>>
            extends InRange.BaseBuilder<B, R, T> {

        public BaseBuilder(final R predicate) {
            super(predicate);
        }

        public B startDateString(final String startDateString) {
            getPredicate().getPredicate().setStartDateString(startDateString);
            return getSelf();
        }

        public B startOffsetInMillis(final Long startOffsetInMillis) {
            getPredicate().getPredicate().setStartOffsetInMillis(startOffsetInMillis);
            return getSelf();
        }

        public B startOffsetInHours(final Long startOffsetInHours) {
            getPredicate().getPredicate().setStartOffsetInHours(startOffsetInHours);
            return getSelf();
        }

        public B startOffsetInDays(final Integer startOffsetInDays) {
            getPredicate().getPredicate().setStartOffsetInDays(startOffsetInDays);
            return getSelf();
        }

        public B endDateString(final String endDateString) {
            getPredicate().getPredicate().setEndDateString(endDateString);
            return getSelf();
        }

        public B endOffsetInMillis(final Long endOffsetInMillis) {
            getPredicate().getPredicate().setEndOffsetInMillis(endOffsetInMillis);
            return getSelf();
        }

        public B endOffsetInHours(final Long endOffsetInHours) {
            getPredicate().getPredicate().setEndOffsetInHours(endOffsetInHours);
            return getSelf();
        }

        public B endOffsetInDays(final Integer endOffsetInDays) {
            getPredicate().getPredicate().setEndOffsetInDays(endOffsetInDays);
            return getSelf();
        }

        @Override
        public R build() {
            getPredicate().getPredicate().calculateOffsets();
            return super.build();
        }
    }
}

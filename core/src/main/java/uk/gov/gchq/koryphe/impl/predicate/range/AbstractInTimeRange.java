/*
 * Copyright 2017-2018 Crown Copyright
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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import uk.gov.gchq.koryphe.predicate.KoryphePredicate;
import uk.gov.gchq.koryphe.util.TimeUnit;

import java.util.TimeZone;

/**
 * <p>
 * An <code>AbstractInTimeRange</code> is a {@link java.util.function.Predicate}
 * that tests if a {@link Comparable} is within a provided range [start, end].
 * By default the range is inclusive, you can toggle this using the startInclusive
 * and endInclusive booleans.
 * </p>
 * <p>
 * If the start is not set then this will be treated as unbounded.
 * Similarly with the end.
 * </p>
 * <p>
 * If the test value is null then the predicate will return false.
 * </p>
 * <p>
 * This range predicate takes a single value to test, if you want to test
 * a startValue and endValue lies within a range then you can use the
 * {@link AbstractInTimeRangeDual} predicate.
 * </p>
 * <p>
 * The range can also be configured using time offsets
 * from the current system time or a provided start/end time.
 * You can set the start and end offsets using startOffset and endOffset.
 * By default the offset is measured in Days, this can be changed to
 * DAY, HOUR, MINUTE, SECOND and MILLISECOND using the offsetUnit field.
 * <p>
 * At the point when test is called on the class the
 * current system time is used to calculate the start and end values based on:
 * System.currentTimeMillis() + offset.
 * </p>
 * <p>
 * You can configure the start and end time strings using one of the following formats:
 * </p>
 * <ul>
 * <li>timestamp in milliseconds</li>
 * <li>yyyy/MM</li>
 * <li>yyyy/MM/dd</li>
 * <li>yyyy/MM/dd HH</li>
 * <li>yyyy/MM/dd HH:mm</li>
 * <li>yyyy/MM/dd HH:mm:ss</li>
 * </ul>
 * You can use a space, '-', '/', '_', ':', '|', or '.' to separate the parts.
 */
public abstract class AbstractInTimeRange<T extends Comparable<T>> extends KoryphePredicate<T> {
    private final AbstractInTimeRangeDual<T> predicate;

    protected AbstractInTimeRange(final AbstractInTimeRangeDual<T> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(final T value) {
        return predicate.test(value, value);
    }

    public String getStart() {
        return predicate.getStart();
    }

    public Long getStartOffset() {
        return predicate.getStartOffset();
    }

    public Boolean isStartInclusive() {
        return predicate.isStartInclusive();
    }

    public String getEnd() {
        return predicate.getEnd();
    }

    public Long getEndOffset() {
        return predicate.getEndOffset();
    }

    public Boolean isEndInclusive() {
        return predicate.isEndInclusive();
    }

    public TimeUnit getOffsetUnit() {
        return predicate.getOffsetUnit();
    }

    @JsonInclude(Include.NON_DEFAULT)
    public TimeUnit getTimeUnit() {
        return predicate.getTimeUnit();
    }

    public TimeZone getTimeZone() {
        return predicate.getTimeZone();
    }

    @JsonGetter("timeZone")
    public String getTimeZoneId() {
        return predicate.getTimeZoneId();
    }

    protected void setTimeZone(final TimeZone timeZone) {
        predicate.setTimeZone(timeZone);
    }

    protected AbstractInTimeRangeDual<T> getPredicate() {
        return predicate;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (null == obj || !getClass().equals(obj.getClass())) {
            return false;
        }

        final AbstractInTimeRange otherPredicate = (AbstractInTimeRange) obj;
        return new EqualsBuilder()
                .append(predicate, otherPredicate.predicate)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 31)
                .appendSuper(predicate.hashCode())
                .append(predicate)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(predicate.toString())
                .toString();
    }

    @JsonPOJOBuilder(withPrefix = "")
    public abstract static class BaseBuilder<B extends BaseBuilder<B, R, T>, R extends AbstractInTimeRange<T>, T extends Comparable<T>> {
        protected final R predicate;

        public BaseBuilder(final R predicate) {
            this.predicate = predicate;
        }

        public B start(final String start) {
            predicate.getPredicate().setStart(start);
            return getSelf();
        }

        public B startOffset(final Long startOffset) {
            predicate.getPredicate().setStartOffset(startOffset);
            return getSelf();
        }

        public B startInclusive(final boolean startInclusive) {
            predicate.getPredicate().setStartInclusive(startInclusive);
            return getSelf();
        }

        public B end(final String end) {
            predicate.getPredicate().setEnd(end);
            return getSelf();
        }

        public B endOffset(final Long endOffset) {
            predicate.getPredicate().setEndOffset(endOffset);
            return getSelf();
        }

        public B endInclusive(final boolean endInclusive) {
            predicate.getPredicate().setEndInclusive(endInclusive);
            return getSelf();
        }

        public B offsetUnit(final TimeUnit offsetUnit) {
            predicate.getPredicate().setOffsetUnit(offsetUnit);
            return getSelf();
        }

        public B timeUnit(final TimeUnit timeUnit) {
            predicate.getPredicate().setTimeUnit(timeUnit);
            return getSelf();
        }

        public B timeZone(final TimeZone timeZone) {
            predicate.setTimeZone(timeZone);
            return getSelf();
        }

        @JsonSetter("timeZone")
        public B timeZone(final String timeZone) {
            predicate.setTimeZone(TimeZone.getTimeZone(timeZone));
            return getSelf();
        }


        public R build() {
            predicate.getPredicate().initialise();
            return predicate;
        }

        protected B getSelf() {
            return (B) this;
        }
    }
}
